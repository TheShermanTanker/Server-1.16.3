package io.netty.handler.codec.http.multipart;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import io.netty.handler.codec.http.HttpConstants;
import java.io.FileInputStream;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.EmptyArrays;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.io.FileOutputStream;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.channels.FileChannel;
import java.io.File;
import io.netty.util.internal.logging.InternalLogger;

public abstract class AbstractDiskHttpData extends AbstractHttpData {
    private static final InternalLogger logger;
    private File file;
    private boolean isRenamed;
    private FileChannel fileChannel;
    
    protected AbstractDiskHttpData(final String name, final Charset charset, final long size) {
        super(name, charset, size);
    }
    
    protected abstract String getDiskFilename();
    
    protected abstract String getPrefix();
    
    protected abstract String getBaseDirectory();
    
    protected abstract String getPostfix();
    
    protected abstract boolean deleteOnExit();
    
    private File tempFile() throws IOException {
        final String diskFilename = this.getDiskFilename();
        String newpostfix;
        if (diskFilename != null) {
            newpostfix = '_' + diskFilename;
        }
        else {
            newpostfix = this.getPostfix();
        }
        File tmpFile;
        if (this.getBaseDirectory() == null) {
            tmpFile = File.createTempFile(this.getPrefix(), newpostfix);
        }
        else {
            tmpFile = File.createTempFile(this.getPrefix(), newpostfix, new File(this.getBaseDirectory()));
        }
        if (this.deleteOnExit()) {
            tmpFile.deleteOnExit();
        }
        return tmpFile;
    }
    
    @Override
    public void setContent(final ByteBuf buffer) throws IOException {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        try {
            this.checkSize(this.size = buffer.readableBytes());
            if (this.definedSize > 0L && this.definedSize < this.size) {
                throw new IOException(new StringBuilder().append("Out of size: ").append(this.size).append(" > ").append(this.definedSize).toString());
            }
            if (this.file == null) {
                this.file = this.tempFile();
            }
            if (buffer.readableBytes() == 0) {
                if (!this.file.createNewFile()) {
                    if (this.file.length() == 0L) {
                        return;
                    }
                    if (!this.file.delete() || !this.file.createNewFile()) {
                        throw new IOException(new StringBuilder().append("file exists already: ").append(this.file).toString());
                    }
                }
                return;
            }
            final FileOutputStream outputStream = new FileOutputStream(this.file);
            try {
                FileChannel localfileChannel;
                ByteBuffer byteBuffer;
                int written;
                for (localfileChannel = outputStream.getChannel(), byteBuffer = buffer.nioBuffer(), written = 0; written < this.size; written += localfileChannel.write(byteBuffer)) {}
                buffer.readerIndex(buffer.readerIndex() + written);
                localfileChannel.force(false);
            }
            finally {
                outputStream.close();
            }
            this.setCompleted();
        }
        finally {
            buffer.release();
        }
    }
    
    @Override
    public void addContent(final ByteBuf buffer, final boolean last) throws IOException {
        if (buffer != null) {
            try {
                final int localsize = buffer.readableBytes();
                this.checkSize(this.size + localsize);
                if (this.definedSize > 0L && this.definedSize < this.size + localsize) {
                    throw new IOException(new StringBuilder().append("Out of size: ").append(this.size + localsize).append(" > ").append(this.definedSize).toString());
                }
                final ByteBuffer byteBuffer = (buffer.nioBufferCount() == 1) ? buffer.nioBuffer() : buffer.copy().nioBuffer();
                int written = 0;
                if (this.file == null) {
                    this.file = this.tempFile();
                }
                if (this.fileChannel == null) {
                    final FileOutputStream outputStream = new FileOutputStream(this.file);
                    this.fileChannel = outputStream.getChannel();
                }
                while (written < localsize) {
                    written += this.fileChannel.write(byteBuffer);
                }
                this.size += localsize;
                buffer.readerIndex(buffer.readerIndex() + written);
            }
            finally {
                buffer.release();
            }
        }
        if (last) {
            if (this.file == null) {
                this.file = this.tempFile();
            }
            if (this.fileChannel == null) {
                final FileOutputStream outputStream2 = new FileOutputStream(this.file);
                this.fileChannel = outputStream2.getChannel();
            }
            this.fileChannel.force(false);
            this.fileChannel.close();
            this.fileChannel = null;
            this.setCompleted();
        }
        else if (buffer == null) {
            throw new NullPointerException("buffer");
        }
    }
    
    @Override
    public void setContent(final File file) throws IOException {
        if (this.file != null) {
            this.delete();
        }
        this.file = file;
        this.checkSize(this.size = file.length());
        this.isRenamed = true;
        this.setCompleted();
    }
    
    @Override
    public void setContent(final InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new NullPointerException("inputStream");
        }
        if (this.file != null) {
            this.delete();
        }
        this.file = this.tempFile();
        final FileOutputStream outputStream = new FileOutputStream(this.file);
        int written = 0;
        try {
            final FileChannel localfileChannel = outputStream.getChannel();
            final byte[] bytes = new byte[16384];
            final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            for (int read = inputStream.read(bytes); read > 0; read = inputStream.read(bytes)) {
                byteBuffer.position(read).flip();
                written += localfileChannel.write(byteBuffer);
                this.checkSize(written);
            }
            localfileChannel.force(false);
        }
        finally {
            outputStream.close();
        }
        this.size = written;
        if (this.definedSize > 0L && this.definedSize < this.size) {
            if (!this.file.delete()) {
                AbstractDiskHttpData.logger.warn("Failed to delete: {}", this.file);
            }
            this.file = null;
            throw new IOException(new StringBuilder().append("Out of size: ").append(this.size).append(" > ").append(this.definedSize).toString());
        }
        this.isRenamed = true;
        this.setCompleted();
    }
    
    @Override
    public void delete() {
        if (this.fileChannel != null) {
            try {
                this.fileChannel.force(false);
                this.fileChannel.close();
            }
            catch (IOException e) {
                AbstractDiskHttpData.logger.warn("Failed to close a file.", (Throwable)e);
            }
            this.fileChannel = null;
        }
        if (!this.isRenamed) {
            if (this.file != null && this.file.exists() && !this.file.delete()) {
                AbstractDiskHttpData.logger.warn("Failed to delete: {}", this.file);
            }
            this.file = null;
        }
    }
    
    @Override
    public byte[] get() throws IOException {
        if (this.file == null) {
            return EmptyArrays.EMPTY_BYTES;
        }
        return readFrom(this.file);
    }
    
    @Override
    public ByteBuf getByteBuf() throws IOException {
        if (this.file == null) {
            return Unpooled.EMPTY_BUFFER;
        }
        final byte[] array = readFrom(this.file);
        return Unpooled.wrappedBuffer(array);
    }
    
    @Override
    public ByteBuf getChunk(final int length) throws IOException {
        if (this.file == null || length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        if (this.fileChannel == null) {
            final FileInputStream inputStream = new FileInputStream(this.file);
            this.fileChannel = inputStream.getChannel();
        }
        int read = 0;
        final ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        while (read < length) {
            final int readnow = this.fileChannel.read(byteBuffer);
            if (readnow == -1) {
                this.fileChannel.close();
                this.fileChannel = null;
                break;
            }
            read += readnow;
        }
        if (read == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        byteBuffer.flip();
        final ByteBuf buffer = Unpooled.wrappedBuffer(byteBuffer);
        buffer.readerIndex(0);
        buffer.writerIndex(read);
        return buffer;
    }
    
    @Override
    public String getString() throws IOException {
        return this.getString(HttpConstants.DEFAULT_CHARSET);
    }
    
    @Override
    public String getString(final Charset encoding) throws IOException {
        if (this.file == null) {
            return "";
        }
        if (encoding == null) {
            final byte[] array = readFrom(this.file);
            return new String(array, HttpConstants.DEFAULT_CHARSET.name());
        }
        final byte[] array = readFrom(this.file);
        return new String(array, encoding.name());
    }
    
    @Override
    public boolean isInMemory() {
        return false;
    }
    
    @Override
    public boolean renameTo(final File dest) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnonnull       15
        //     4: new             Ljava/lang/NullPointerException;
        //     7: dup            
        //     8: ldc_w           "dest"
        //    11: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //    14: athrow         
        //    15: aload_0         /* this */
        //    16: getfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.file:Ljava/io/File;
        //    19: ifnonnull       33
        //    22: new             Ljava/io/IOException;
        //    25: dup            
        //    26: ldc_w           "No file defined so cannot be renamed"
        //    29: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //    32: athrow         
        //    33: aload_0         /* this */
        //    34: getfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.file:Ljava/io/File;
        //    37: aload_1         /* dest */
        //    38: invokevirtual   java/io/File.renameTo:(Ljava/io/File;)Z
        //    41: ifne            455
        //    44: aconst_null    
        //    45: astore_2        /* exception */
        //    46: aconst_null    
        //    47: astore_3        /* inputStream */
        //    48: aconst_null    
        //    49: astore          outputStream
        //    51: ldc2_w          8196
        //    54: lstore          chunkSize
        //    56: lconst_0       
        //    57: lstore          position
        //    59: new             Ljava/io/FileInputStream;
        //    62: dup            
        //    63: aload_0         /* this */
        //    64: getfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.file:Ljava/io/File;
        //    67: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    70: astore_3        /* inputStream */
        //    71: new             Ljava/io/FileOutputStream;
        //    74: dup            
        //    75: aload_1         /* dest */
        //    76: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //    79: astore          outputStream
        //    81: aload_3         /* inputStream */
        //    82: invokevirtual   java/io/FileInputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //    85: astore          in
        //    87: aload           outputStream
        //    89: invokevirtual   java/io/FileOutputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //    92: astore          out
        //    94: lload           position
        //    96: aload_0         /* this */
        //    97: getfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.size:J
        //   100: lcmp           
        //   101: ifge            145
        //   104: lload           chunkSize
        //   106: aload_0         /* this */
        //   107: getfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.size:J
        //   110: lload           position
        //   112: lsub           
        //   113: lcmp           
        //   114: ifge            126
        //   117: aload_0         /* this */
        //   118: getfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.size:J
        //   121: lload           position
        //   123: lsub           
        //   124: lstore          chunkSize
        //   126: lload           position
        //   128: aload           in
        //   130: lload           position
        //   132: lload           chunkSize
        //   134: aload           out
        //   136: invokevirtual   java/nio/channels/FileChannel.transferTo:(JJLjava/nio/channels/WritableByteChannel;)J
        //   139: ladd           
        //   140: lstore          position
        //   142: goto            94
        //   145: aload_3         /* inputStream */
        //   146: ifnull          181
        //   149: aload_3         /* inputStream */
        //   150: invokevirtual   java/io/FileInputStream.close:()V
        //   153: goto            181
        //   156: astore          e
        //   158: aload_2         /* exception */
        //   159: ifnonnull       168
        //   162: aload           e
        //   164: astore_2        /* exception */
        //   165: goto            181
        //   168: getstatic       io/netty/handler/codec/http/multipart/AbstractDiskHttpData.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   171: ldc_w           "Multiple exceptions detected, the following will be suppressed {}"
        //   174: aload           e
        //   176: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   181: aload           outputStream
        //   183: ifnull          383
        //   186: aload           outputStream
        //   188: invokevirtual   java/io/FileOutputStream.close:()V
        //   191: goto            383
        //   194: astore          e
        //   196: aload_2         /* exception */
        //   197: ifnonnull       206
        //   200: aload           e
        //   202: astore_2        /* exception */
        //   203: goto            219
        //   206: getstatic       io/netty/handler/codec/http/multipart/AbstractDiskHttpData.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   209: ldc_w           "Multiple exceptions detected, the following will be suppressed {}"
        //   212: aload           e
        //   214: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   219: goto            383
        //   222: astore          e
        //   224: aload           e
        //   226: astore_2        /* exception */
        //   227: aload_3         /* inputStream */
        //   228: ifnull          263
        //   231: aload_3         /* inputStream */
        //   232: invokevirtual   java/io/FileInputStream.close:()V
        //   235: goto            263
        //   238: astore          e
        //   240: aload_2         /* exception */
        //   241: ifnonnull       250
        //   244: aload           e
        //   246: astore_2        /* exception */
        //   247: goto            263
        //   250: getstatic       io/netty/handler/codec/http/multipart/AbstractDiskHttpData.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   253: ldc_w           "Multiple exceptions detected, the following will be suppressed {}"
        //   256: aload           e
        //   258: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   263: aload           outputStream
        //   265: ifnull          383
        //   268: aload           outputStream
        //   270: invokevirtual   java/io/FileOutputStream.close:()V
        //   273: goto            383
        //   276: astore          e
        //   278: aload_2         /* exception */
        //   279: ifnonnull       288
        //   282: aload           e
        //   284: astore_2        /* exception */
        //   285: goto            301
        //   288: getstatic       io/netty/handler/codec/http/multipart/AbstractDiskHttpData.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   291: ldc_w           "Multiple exceptions detected, the following will be suppressed {}"
        //   294: aload           e
        //   296: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   301: goto            383
        //   304: astore          11
        //   306: aload_3         /* inputStream */
        //   307: ifnull          342
        //   310: aload_3         /* inputStream */
        //   311: invokevirtual   java/io/FileInputStream.close:()V
        //   314: goto            342
        //   317: astore          e
        //   319: aload_2         /* exception */
        //   320: ifnonnull       329
        //   323: aload           e
        //   325: astore_2        /* exception */
        //   326: goto            342
        //   329: getstatic       io/netty/handler/codec/http/multipart/AbstractDiskHttpData.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   332: ldc_w           "Multiple exceptions detected, the following will be suppressed {}"
        //   335: aload           e
        //   337: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   342: aload           outputStream
        //   344: ifnull          380
        //   347: aload           outputStream
        //   349: invokevirtual   java/io/FileOutputStream.close:()V
        //   352: goto            380
        //   355: astore          e
        //   357: aload_2         /* exception */
        //   358: ifnonnull       367
        //   361: aload           e
        //   363: astore_2        /* exception */
        //   364: goto            380
        //   367: getstatic       io/netty/handler/codec/http/multipart/AbstractDiskHttpData.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   370: ldc_w           "Multiple exceptions detected, the following will be suppressed {}"
        //   373: aload           e
        //   375: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   380: aload           11
        //   382: athrow         
        //   383: aload_2         /* exception */
        //   384: ifnull          389
        //   387: aload_2         /* exception */
        //   388: athrow         
        //   389: lload           position
        //   391: aload_0         /* this */
        //   392: getfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.size:J
        //   395: lcmp           
        //   396: ifne            435
        //   399: aload_0         /* this */
        //   400: getfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.file:Ljava/io/File;
        //   403: invokevirtual   java/io/File.delete:()Z
        //   406: ifne            423
        //   409: getstatic       io/netty/handler/codec/http/multipart/AbstractDiskHttpData.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   412: ldc             "Failed to delete: {}"
        //   414: aload_0         /* this */
        //   415: getfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.file:Ljava/io/File;
        //   418: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Object;)V
        //   423: aload_0         /* this */
        //   424: aload_1         /* dest */
        //   425: putfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.file:Ljava/io/File;
        //   428: aload_0         /* this */
        //   429: iconst_1       
        //   430: putfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.isRenamed:Z
        //   433: iconst_1       
        //   434: ireturn        
        //   435: aload_1         /* dest */
        //   436: invokevirtual   java/io/File.delete:()Z
        //   439: ifne            453
        //   442: getstatic       io/netty/handler/codec/http/multipart/AbstractDiskHttpData.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   445: ldc             "Failed to delete: {}"
        //   447: aload_1         /* dest */
        //   448: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Object;)V
        //   453: iconst_0       
        //   454: ireturn        
        //   455: aload_0         /* this */
        //   456: aload_1         /* dest */
        //   457: putfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.file:Ljava/io/File;
        //   460: aload_0         /* this */
        //   461: iconst_1       
        //   462: putfield        io/netty/handler/codec/http/multipart/AbstractDiskHttpData.isRenamed:Z
        //   465: iconst_1       
        //   466: ireturn        
        //    Exceptions:
        //  throws java.io.IOException
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  dest  
        //    StackMapTable: 00 1F 0F 11 FF 00 3C 00 09 07 00 02 07 00 3F 07 00 24 07 01 0D 07 00 86 04 04 07 00 93 07 00 93 00 00 1F F9 00 12 4A 07 00 24 FC 00 0B 07 00 24 FA 00 0C 4C 07 00 24 FC 00 0B 07 00 24 FA 00 0C 42 07 00 24 4F 07 00 24 FC 00 0B 07 00 24 FA 00 0C 4C 07 00 24 FC 00 0B 07 00 24 FA 00 0C 42 07 00 A8 FF 00 0C 00 0A 07 00 02 07 00 3F 07 00 24 07 01 0D 07 00 86 04 04 00 00 07 00 A8 00 01 07 00 24 FC 00 0B 07 00 24 FA 00 0C 4C 07 00 24 FC 00 0B 07 00 24 FA 00 0C F8 00 02 05 21 0B 11 FF 00 01 00 02 07 00 02 07 00 3F 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  149    153    156    181    Ljava/io/IOException;
        //  186    191    194    222    Ljava/io/IOException;
        //  59     145    222    304    Ljava/io/IOException;
        //  231    235    238    263    Ljava/io/IOException;
        //  268    273    276    304    Ljava/io/IOException;
        //  59     145    304    383    Any
        //  222    227    304    383    Any
        //  310    314    317    342    Ljava/io/IOException;
        //  347    352    355    380    Ljava/io/IOException;
        //  304    306    304    383    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.assembler.metadata.TypeReference.equals(TypeReference.java:118)
        //     at com.strobel.core.Comparer.equals(Comparer.java:31)
        //     at com.strobel.assembler.ir.FrameValue.equals(FrameValue.java:72)
        //     at com.strobel.core.Comparer.equals(Comparer.java:31)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:338)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:254)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2206)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
        //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
        //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
        //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
        //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
        //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:408)
        //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
        //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.base/java.lang.Thread.run(Thread.java:832)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static byte[] readFrom(final File src) throws IOException {
        final long srcsize = src.length();
        if (srcsize > 2147483647L) {
            throw new IllegalArgumentException("File too big to be loaded in memory");
        }
        final FileInputStream inputStream = new FileInputStream(src);
        final byte[] array = new byte[(int)srcsize];
        try {
            final FileChannel fileChannel = inputStream.getChannel();
            final ByteBuffer byteBuffer = ByteBuffer.wrap(array);
            for (int read = 0; read < srcsize; read += fileChannel.read(byteBuffer)) {}
        }
        finally {
            inputStream.close();
        }
        return array;
    }
    
    @Override
    public File getFile() throws IOException {
        return this.file;
    }
    
    @Override
    public HttpData touch() {
        return this;
    }
    
    @Override
    public HttpData touch(final Object hint) {
        return this;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractDiskHttpData.class);
    }
}

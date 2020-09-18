package com.google.common.io;

import com.google.common.collect.ImmutableSet;
import com.google.common.base.Optional;
import java.util.Collections;
import java.util.Arrays;
import com.google.common.base.Predicate;
import java.util.Iterator;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import com.google.common.base.Splitter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.List;
import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import com.google.common.base.Preconditions;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.io.File;
import com.google.common.collect.TreeTraverser;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public final class Files {
    private static final int TEMP_DIR_ATTEMPTS = 10000;
    private static final TreeTraverser<File> FILE_TREE_TRAVERSER;
    
    private Files() {
    }
    
    public static BufferedReader newReader(final File file, final Charset charset) throws FileNotFoundException {
        Preconditions.<File>checkNotNull(file);
        Preconditions.<Charset>checkNotNull(charset);
        return new BufferedReader((Reader)new InputStreamReader((InputStream)new FileInputStream(file), charset));
    }
    
    public static BufferedWriter newWriter(final File file, final Charset charset) throws FileNotFoundException {
        Preconditions.<File>checkNotNull(file);
        Preconditions.<Charset>checkNotNull(charset);
        return new BufferedWriter((Writer)new OutputStreamWriter((OutputStream)new FileOutputStream(file), charset));
    }
    
    public static ByteSource asByteSource(final File file) {
        return new FileByteSource(file);
    }
    
    static byte[] readFile(final InputStream in, final long expectedSize) throws IOException {
        if (expectedSize > 2147483647L) {
            throw new OutOfMemoryError(new StringBuilder().append("file is too large to fit in a byte array: ").append(expectedSize).append(" bytes").toString());
        }
        return (expectedSize == 0L) ? ByteStreams.toByteArray(in) : ByteStreams.toByteArray(in, (int)expectedSize);
    }
    
    public static ByteSink asByteSink(final File file, final FileWriteMode... modes) {
        return new FileByteSink(file, modes);
    }
    
    public static CharSource asCharSource(final File file, final Charset charset) {
        return asByteSource(file).asCharSource(charset);
    }
    
    public static CharSink asCharSink(final File file, final Charset charset, final FileWriteMode... modes) {
        return asByteSink(file, modes).asCharSink(charset);
    }
    
    private static FileWriteMode[] modes(final boolean append) {
        return append ? new FileWriteMode[] { FileWriteMode.APPEND } : new FileWriteMode[0];
    }
    
    public static byte[] toByteArray(final File file) throws IOException {
        return asByteSource(file).read();
    }
    
    public static String toString(final File file, final Charset charset) throws IOException {
        return asCharSource(file, charset).read();
    }
    
    public static void write(final byte[] from, final File to) throws IOException {
        asByteSink(to).write(from);
    }
    
    public static void copy(final File from, final OutputStream to) throws IOException {
        asByteSource(from).copyTo(to);
    }
    
    public static void copy(final File from, final File to) throws IOException {
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
        asByteSource(from).copyTo(asByteSink(to));
    }
    
    public static void write(final CharSequence from, final File to, final Charset charset) throws IOException {
        asCharSink(to, charset).write(from);
    }
    
    public static void append(final CharSequence from, final File to, final Charset charset) throws IOException {
        write(from, to, charset, true);
    }
    
    private static void write(final CharSequence from, final File to, final Charset charset, final boolean append) throws IOException {
        asCharSink(to, charset, modes(append)).write(from);
    }
    
    public static void copy(final File from, final Charset charset, final Appendable to) throws IOException {
        asCharSource(from, charset).copyTo(to);
    }
    
    public static boolean equal(final File file1, final File file2) throws IOException {
        Preconditions.<File>checkNotNull(file1);
        Preconditions.<File>checkNotNull(file2);
        if (file1 == file2 || file1.equals(file2)) {
            return true;
        }
        final long len1 = file1.length();
        final long len2 = file2.length();
        return (len1 == 0L || len2 == 0L || len1 == len2) && asByteSource(file1).contentEquals(asByteSource(file2));
    }
    
    public static File createTempDir() {
        final File baseDir = new File(System.getProperty("java.io.tmpdir"));
        final String baseName = new StringBuilder().append(System.currentTimeMillis()).append("-").toString();
        for (int counter = 0; counter < 10000; ++counter) {
            final File tempDir = new File(baseDir, baseName + counter);
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + 9999 + ')');
    }
    
    public static void touch(final File file) throws IOException {
        Preconditions.<File>checkNotNull(file);
        if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
            throw new IOException(new StringBuilder().append("Unable to update modification time of ").append(file).toString());
        }
    }
    
    public static void createParentDirs(final File file) throws IOException {
        Preconditions.<File>checkNotNull(file);
        final File parent = file.getCanonicalFile().getParentFile();
        if (parent == null) {
            return;
        }
        parent.mkdirs();
        if (!parent.isDirectory()) {
            throw new IOException(new StringBuilder().append("Unable to create parent directories of ").append(file).toString());
        }
    }
    
    public static void move(final File from, final File to) throws IOException {
        Preconditions.<File>checkNotNull(from);
        Preconditions.<File>checkNotNull(to);
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
        if (!from.renameTo(to)) {
            copy(from, to);
            if (!from.delete()) {
                if (!to.delete()) {
                    throw new IOException(new StringBuilder().append("Unable to delete ").append(to).toString());
                }
                throw new IOException(new StringBuilder().append("Unable to delete ").append(from).toString());
            }
        }
    }
    
    public static String readFirstLine(final File file, final Charset charset) throws IOException {
        return asCharSource(file, charset).readFirstLine();
    }
    
    public static List<String> readLines(final File file, final Charset charset) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* charset */
        //     2: new             Lcom/google/common/io/Files$1;
        //     5: dup            
        //     6: invokespecial   com/google/common/io/Files$1.<init>:()V
        //     9: invokestatic    com/google/common/io/Files.readLines:(Ljava/io/File;Ljava/nio/charset/Charset;Lcom/google/common/io/LineProcessor;)Ljava/lang/Object;
        //    12: checkcast       Ljava/util/List;
        //    15: areturn        
        //    Exceptions:
        //  throws java.io.IOException
        //    Signature:
        //  (Ljava/io/File;Ljava/nio/charset/Charset;)Ljava/util/List<Ljava/lang/String;>;
        //    MethodParameters:
        //  Name     Flags  
        //  -------  -----
        //  file     
        //  charset  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.shouldInferVariableType(TypeAnalysis.java:613)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:918)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @CanIgnoreReturnValue
    public static <T> T readLines(final File file, final Charset charset, final LineProcessor<T> callback) throws IOException {
        return asCharSource(file, charset).<T>readLines(callback);
    }
    
    @CanIgnoreReturnValue
    public static <T> T readBytes(final File file, final ByteProcessor<T> processor) throws IOException {
        return asByteSource(file).<T>read(processor);
    }
    
    public static HashCode hash(final File file, final HashFunction hashFunction) throws IOException {
        return asByteSource(file).hash(hashFunction);
    }
    
    public static MappedByteBuffer map(final File file) throws IOException {
        Preconditions.<File>checkNotNull(file);
        return map(file, FileChannel.MapMode.READ_ONLY);
    }
    
    public static MappedByteBuffer map(final File file, final FileChannel.MapMode mode) throws IOException {
        Preconditions.<File>checkNotNull(file);
        Preconditions.<FileChannel.MapMode>checkNotNull(mode);
        if (!file.exists()) {
            throw new FileNotFoundException(file.toString());
        }
        return map(file, mode, file.length());
    }
    
    public static MappedByteBuffer map(final File file, final FileChannel.MapMode mode, final long size) throws FileNotFoundException, IOException {
        Preconditions.<File>checkNotNull(file);
        Preconditions.<FileChannel.MapMode>checkNotNull(mode);
        final Closer closer = Closer.create();
        try {
            final RandomAccessFile raf = closer.<RandomAccessFile>register(new RandomAccessFile(file, (mode == FileChannel.MapMode.READ_ONLY) ? "r" : "rw"));
            return map(raf, mode, size);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    private static MappedByteBuffer map(final RandomAccessFile raf, final FileChannel.MapMode mode, final long size) throws IOException {
        final Closer closer = Closer.create();
        try {
            final FileChannel channel = closer.<FileChannel>register(raf.getChannel());
            return channel.map(mode, 0L, size);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public static String simplifyPath(final String pathname) {
        Preconditions.<String>checkNotNull(pathname);
        if (pathname.length() == 0) {
            return ".";
        }
        final Iterable<String> components = Splitter.on('/').omitEmptyStrings().split((CharSequence)pathname);
        final List<String> path = (List<String>)new ArrayList();
        for (final String component : components) {
            if (component.equals(".")) {
                continue;
            }
            if (component.equals("..")) {
                if (path.size() > 0 && !((String)path.get(path.size() - 1)).equals("..")) {
                    path.remove(path.size() - 1);
                }
                else {
                    path.add("..");
                }
            }
            else {
                path.add(component);
            }
        }
        String result = Joiner.on('/').join(path);
        if (pathname.charAt(0) == '/') {
            result = "/" + result;
        }
        while (result.startsWith("/../")) {
            result = result.substring(3);
        }
        if (result.equals("/..")) {
            result = "/";
        }
        else if ("".equals(result)) {
            result = ".";
        }
        return result;
    }
    
    public static String getFileExtension(final String fullName) {
        Preconditions.<String>checkNotNull(fullName);
        final String fileName = new File(fullName).getName();
        final int dotIndex = fileName.lastIndexOf(46);
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
    
    public static String getNameWithoutExtension(final String file) {
        Preconditions.<String>checkNotNull(file);
        final String fileName = new File(file).getName();
        final int dotIndex = fileName.lastIndexOf(46);
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }
    
    public static TreeTraverser<File> fileTreeTraverser() {
        return Files.FILE_TREE_TRAVERSER;
    }
    
    public static Predicate<File> isDirectory() {
        return FilePredicate.IS_DIRECTORY;
    }
    
    public static Predicate<File> isFile() {
        return FilePredicate.IS_FILE;
    }
    
    static {
        FILE_TREE_TRAVERSER = new TreeTraverser<File>() {
            @Override
            public Iterable<File> children(final File file) {
                if (file.isDirectory()) {
                    final File[] files = file.listFiles();
                    if (files != null) {
                        return (Iterable<File>)Collections.unmodifiableList(Arrays.asList((Object[])files));
                    }
                }
                return (Iterable<File>)Collections.emptyList();
            }
            
            public String toString() {
                return "Files.fileTreeTraverser()";
            }
        };
    }
    
    private static final class FileByteSource extends ByteSource {
        private final File file;
        
        private FileByteSource(final File file) {
            this.file = Preconditions.<File>checkNotNull(file);
        }
        
        public FileInputStream openStream() throws IOException {
            return new FileInputStream(this.file);
        }
        
        @Override
        public Optional<Long> sizeIfKnown() {
            if (this.file.isFile()) {
                return Optional.<Long>of(this.file.length());
            }
            return Optional.<Long>absent();
        }
        
        @Override
        public long size() throws IOException {
            if (!this.file.isFile()) {
                throw new FileNotFoundException(this.file.toString());
            }
            return this.file.length();
        }
        
        @Override
        public byte[] read() throws IOException {
            final Closer closer = Closer.create();
            try {
                final FileInputStream in = closer.<FileInputStream>register(this.openStream());
                return Files.readFile((InputStream)in, in.getChannel().size());
            }
            catch (Throwable e) {
                throw closer.rethrow(e);
            }
            finally {
                closer.close();
            }
        }
        
        public String toString() {
            return new StringBuilder().append("Files.asByteSource(").append(this.file).append(")").toString();
        }
    }
    
    private static final class FileByteSink extends ByteSink {
        private final File file;
        private final ImmutableSet<FileWriteMode> modes;
        
        private FileByteSink(final File file, final FileWriteMode... modes) {
            this.file = Preconditions.<File>checkNotNull(file);
            this.modes = ImmutableSet.<FileWriteMode>copyOf(modes);
        }
        
        public FileOutputStream openStream() throws IOException {
            return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
        }
        
        public String toString() {
            return new StringBuilder().append("Files.asByteSink(").append(this.file).append(", ").append(this.modes).append(")").toString();
        }
    }
    
    private enum FilePredicate implements Predicate<File> {
        IS_DIRECTORY {
            public boolean apply(final File file) {
                return file.isDirectory();
            }
            
            public String toString() {
                return "Files.isDirectory()";
            }
        }, 
        IS_FILE {
            public boolean apply(final File file) {
                return file.isFile();
            }
            
            public String toString() {
                return "Files.isFile()";
            }
        };
    }
}

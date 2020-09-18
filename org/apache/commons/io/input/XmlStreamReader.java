package org.apache.commons.io.input;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.Locale;
import java.text.MessageFormat;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.regex.Pattern;
import org.apache.commons.io.ByteOrderMark;
import java.io.Reader;

public class XmlStreamReader extends Reader {
    private static final int BUFFER_SIZE = 4096;
    private static final String UTF_8 = "UTF-8";
    private static final String US_ASCII = "US-ASCII";
    private static final String UTF_16BE = "UTF-16BE";
    private static final String UTF_16LE = "UTF-16LE";
    private static final String UTF_32BE = "UTF-32BE";
    private static final String UTF_32LE = "UTF-32LE";
    private static final String UTF_16 = "UTF-16";
    private static final String UTF_32 = "UTF-32";
    private static final String EBCDIC = "CP1047";
    private static final ByteOrderMark[] BOMS;
    private static final ByteOrderMark[] XML_GUESS_BYTES;
    private final Reader reader;
    private final String encoding;
    private final String defaultEncoding;
    private static final Pattern CHARSET_PATTERN;
    public static final Pattern ENCODING_PATTERN;
    private static final String RAW_EX_1 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch";
    private static final String RAW_EX_2 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM";
    private static final String HTTP_EX_1 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL";
    private static final String HTTP_EX_2 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch";
    private static final String HTTP_EX_3 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME";
    
    public String getDefaultEncoding() {
        return this.defaultEncoding;
    }
    
    public XmlStreamReader(final File file) throws IOException {
        this((InputStream)new FileInputStream(file));
    }
    
    public XmlStreamReader(final InputStream is) throws IOException {
        this(is, true);
    }
    
    public XmlStreamReader(final InputStream is, final boolean lenient) throws IOException {
        this(is, lenient, null);
    }
    
    public XmlStreamReader(final InputStream is, final boolean lenient, final String defaultEncoding) throws IOException {
        this.defaultEncoding = defaultEncoding;
        final BOMInputStream bom = new BOMInputStream((InputStream)new BufferedInputStream(is, 4096), false, XmlStreamReader.BOMS);
        final BOMInputStream pis = new BOMInputStream((InputStream)bom, true, XmlStreamReader.XML_GUESS_BYTES);
        this.encoding = this.doRawStream(bom, pis, lenient);
        this.reader = (Reader)new InputStreamReader((InputStream)pis, this.encoding);
    }
    
    public XmlStreamReader(final URL url) throws IOException {
        this(url.openConnection(), null);
    }
    
    public XmlStreamReader(final URLConnection conn, final String defaultEncoding) throws IOException {
        this.defaultEncoding = defaultEncoding;
        final boolean lenient = true;
        final String contentType = conn.getContentType();
        final InputStream is = conn.getInputStream();
        final BOMInputStream bom = new BOMInputStream((InputStream)new BufferedInputStream(is, 4096), false, XmlStreamReader.BOMS);
        final BOMInputStream pis = new BOMInputStream((InputStream)bom, true, XmlStreamReader.XML_GUESS_BYTES);
        if (conn instanceof HttpURLConnection || contentType != null) {
            this.encoding = this.doHttpStream(bom, pis, contentType, true);
        }
        else {
            this.encoding = this.doRawStream(bom, pis, true);
        }
        this.reader = (Reader)new InputStreamReader((InputStream)pis, this.encoding);
    }
    
    public XmlStreamReader(final InputStream is, final String httpContentType) throws IOException {
        this(is, httpContentType, true);
    }
    
    public XmlStreamReader(final InputStream is, final String httpContentType, final boolean lenient, final String defaultEncoding) throws IOException {
        this.defaultEncoding = defaultEncoding;
        final BOMInputStream bom = new BOMInputStream((InputStream)new BufferedInputStream(is, 4096), false, XmlStreamReader.BOMS);
        final BOMInputStream pis = new BOMInputStream((InputStream)bom, true, XmlStreamReader.XML_GUESS_BYTES);
        this.encoding = this.doHttpStream(bom, pis, httpContentType, lenient);
        this.reader = (Reader)new InputStreamReader((InputStream)pis, this.encoding);
    }
    
    public XmlStreamReader(final InputStream is, final String httpContentType, final boolean lenient) throws IOException {
        this(is, httpContentType, lenient, null);
    }
    
    public String getEncoding() {
        return this.encoding;
    }
    
    public int read(final char[] buf, final int offset, final int len) throws IOException {
        return this.reader.read(buf, offset, len);
    }
    
    public void close() throws IOException {
        this.reader.close();
    }
    
    private String doRawStream(final BOMInputStream bom, final BOMInputStream pis, final boolean lenient) throws IOException {
        final String bomEnc = bom.getBOMCharsetName();
        final String xmlGuessEnc = pis.getBOMCharsetName();
        final String xmlEnc = getXmlProlog((InputStream)pis, xmlGuessEnc);
        try {
            return this.calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc);
        }
        catch (XmlStreamReaderException ex) {
            if (lenient) {
                return this.doLenientDetection(null, ex);
            }
            throw ex;
        }
    }
    
    private String doHttpStream(final BOMInputStream bom, final BOMInputStream pis, final String httpContentType, final boolean lenient) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/apache/commons/io/input/BOMInputStream.getBOMCharsetName:()Ljava/lang/String;
        //     4: astore          bomEnc
        //     6: aload_2         /* pis */
        //     7: invokevirtual   org/apache/commons/io/input/BOMInputStream.getBOMCharsetName:()Ljava/lang/String;
        //    10: astore          xmlGuessEnc
        //    12: aload_2         /* pis */
        //    13: aload           xmlGuessEnc
        //    15: invokestatic    org/apache/commons/io/input/XmlStreamReader.getXmlProlog:(Ljava/io/InputStream;Ljava/lang/String;)Ljava/lang/String;
        //    18: astore          xmlEnc
        //    20: aload_0         /* this */
        //    21: aload_3         /* httpContentType */
        //    22: aload           bomEnc
        //    24: aload           xmlGuessEnc
        //    26: aload           xmlEnc
        //    28: iload           lenient
        //    30: invokevirtual   org/apache/commons/io/input/XmlStreamReader.calculateHttpEncoding:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;
        //    33: areturn        
        //    34: astore          ex
        //    36: iload           lenient
        //    38: ifeq            49
        //    41: aload_0         /* this */
        //    42: aload_3         /* httpContentType */
        //    43: aload           ex
        //    45: invokespecial   org/apache/commons/io/input/XmlStreamReader.doLenientDetection:(Ljava/lang/String;Lorg/apache/commons/io/input/XmlStreamReaderException;)Ljava/lang/String;
        //    48: areturn        
        //    49: aload           ex
        //    51: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //    MethodParameters:
        //  Name             Flags  
        //  ---------------  -----
        //  bom              
        //  pis              
        //  httpContentType  
        //  lenient          
        //    StackMapTable: 00 02 FF 00 22 00 08 07 00 02 07 00 5F 07 00 5F 07 00 95 01 07 00 95 07 00 95 07 00 95 00 01 07 00 B2 FC 00 0E 07 00 B2
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                  
        //  -----  -----  -----  -----  ------------------------------------------------------
        //  20     33     34     52     Lorg/apache/commons/io/input/XmlStreamReaderException;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:540)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
        //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:278)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperType(MetadataHelper.java:200)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:369)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:273)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private String doLenientDetection(String httpContentType, XmlStreamReaderException ex) throws IOException {
        if (httpContentType != null && httpContentType.startsWith("text/html")) {
            httpContentType = httpContentType.substring("text/html".length());
            httpContentType = "text/xml" + httpContentType;
            try {
                return this.calculateHttpEncoding(httpContentType, ex.getBomEncoding(), ex.getXmlGuessEncoding(), ex.getXmlEncoding(), true);
            }
            catch (XmlStreamReaderException ex2) {
                ex = ex2;
            }
        }
        String encoding = ex.getXmlEncoding();
        if (encoding == null) {
            encoding = ex.getContentTypeEncoding();
        }
        if (encoding == null) {
            encoding = ((this.defaultEncoding == null) ? "UTF-8" : this.defaultEncoding);
        }
        return encoding;
    }
    
    String calculateRawEncoding(final String bomEnc, final String xmlGuessEnc, final String xmlEnc) throws IOException {
        if (bomEnc == null) {
            if (xmlGuessEnc == null || xmlEnc == null) {
                return (this.defaultEncoding == null) ? "UTF-8" : this.defaultEncoding;
            }
            if (xmlEnc.equals("UTF-16") && (xmlGuessEnc.equals("UTF-16BE") || xmlGuessEnc.equals("UTF-16LE"))) {
                return xmlGuessEnc;
            }
            return xmlEnc;
        }
        else if (bomEnc.equals("UTF-8")) {
            if (xmlGuessEnc != null && !xmlGuessEnc.equals("UTF-8")) {
                final String msg = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            if (xmlEnc != null && !xmlEnc.equals("UTF-8")) {
                final String msg = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            return bomEnc;
        }
        else if (bomEnc.equals("UTF-16BE") || bomEnc.equals("UTF-16LE")) {
            if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
                final String msg = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            if (xmlEnc != null && !xmlEnc.equals("UTF-16") && !xmlEnc.equals(bomEnc)) {
                final String msg = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            return bomEnc;
        }
        else {
            if (!bomEnc.equals("UTF-32BE") && !bomEnc.equals("UTF-32LE")) {
                final String msg = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
                final String msg = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            if (xmlEnc != null && !xmlEnc.equals("UTF-32") && !xmlEnc.equals(bomEnc)) {
                final String msg = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            return bomEnc;
        }
    }
    
    String calculateHttpEncoding(final String httpContentType, final String bomEnc, final String xmlGuessEnc, final String xmlEnc, final boolean lenient) throws IOException {
        if (lenient && xmlEnc != null) {
            return xmlEnc;
        }
        final String cTMime = getContentTypeMime(httpContentType);
        final String cTEnc = getContentTypeEncoding(httpContentType);
        final boolean appXml = isAppXml(cTMime);
        final boolean textXml = isTextXml(cTMime);
        if (!appXml && !textXml) {
            final String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
            throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
        }
        if (cTEnc == null) {
            if (appXml) {
                return this.calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc);
            }
            return (this.defaultEncoding == null) ? "US-ASCII" : this.defaultEncoding;
        }
        else if (cTEnc.equals("UTF-16BE") || cTEnc.equals("UTF-16LE")) {
            if (bomEnc != null) {
                final String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
                throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
            }
            return cTEnc;
        }
        else if (cTEnc.equals("UTF-16")) {
            if (bomEnc != null && bomEnc.startsWith("UTF-16")) {
                return bomEnc;
            }
            final String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
            throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
        }
        else if (cTEnc.equals("UTF-32BE") || cTEnc.equals("UTF-32LE")) {
            if (bomEnc != null) {
                final String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
                throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
            }
            return cTEnc;
        }
        else {
            if (!cTEnc.equals("UTF-32")) {
                return cTEnc;
            }
            if (bomEnc != null && bomEnc.startsWith("UTF-32")) {
                return bomEnc;
            }
            final String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
            throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
        }
    }
    
    static String getContentTypeMime(final String httpContentType) {
        String mime = null;
        if (httpContentType != null) {
            final int i = httpContentType.indexOf(";");
            if (i >= 0) {
                mime = httpContentType.substring(0, i);
            }
            else {
                mime = httpContentType;
            }
            mime = mime.trim();
        }
        return mime;
    }
    
    static String getContentTypeEncoding(final String httpContentType) {
        String encoding = null;
        if (httpContentType != null) {
            final int i = httpContentType.indexOf(";");
            if (i > -1) {
                final String postMime = httpContentType.substring(i + 1);
                final Matcher m = XmlStreamReader.CHARSET_PATTERN.matcher((CharSequence)postMime);
                encoding = (m.find() ? m.group(1) : null);
                encoding = ((encoding != null) ? encoding.toUpperCase(Locale.US) : null);
            }
        }
        return encoding;
    }
    
    private static String getXmlProlog(final InputStream is, final String guessedEnc) throws IOException {
        String encoding = null;
        if (guessedEnc != null) {
            final byte[] bytes = new byte[4096];
            is.mark(4096);
            int offset;
            int max;
            int c;
            int firstGT;
            String xmlProlog;
            for (offset = 0, max = 4096, c = is.read(bytes, offset, max), firstGT = -1, xmlProlog = ""; c != -1 && firstGT == -1 && offset < 4096; offset += c, max -= c, c = is.read(bytes, offset, max), xmlProlog = new String(bytes, 0, offset, guessedEnc), firstGT = xmlProlog.indexOf(62)) {}
            if (firstGT == -1) {
                if (c == -1) {
                    throw new IOException("Unexpected end of XML stream");
                }
                throw new IOException(new StringBuilder().append("XML prolog or ROOT element not found on first ").append(offset).append(" bytes").toString());
            }
            else {
                final int bytesRead = offset;
                if (bytesRead > 0) {
                    is.reset();
                    final BufferedReader bReader = new BufferedReader((Reader)new StringReader(xmlProlog.substring(0, firstGT + 1)));
                    final StringBuffer prolog = new StringBuffer();
                    for (String line = bReader.readLine(); line != null; line = bReader.readLine()) {
                        prolog.append(line);
                    }
                    final Matcher m = XmlStreamReader.ENCODING_PATTERN.matcher((CharSequence)prolog);
                    if (m.find()) {
                        encoding = m.group(1).toUpperCase();
                        encoding = encoding.substring(1, encoding.length() - 1);
                    }
                }
            }
        }
        return encoding;
    }
    
    static boolean isAppXml(final String mime) {
        return mime != null && (mime.equals("application/xml") || mime.equals("application/xml-dtd") || mime.equals("application/xml-external-parsed-entity") || (mime.startsWith("application/") && mime.endsWith("+xml")));
    }
    
    static boolean isTextXml(final String mime) {
        return mime != null && (mime.equals("text/xml") || mime.equals("text/xml-external-parsed-entity") || (mime.startsWith("text/") && mime.endsWith("+xml")));
    }
    
    static {
        BOMS = new ByteOrderMark[] { ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE };
        XML_GUESS_BYTES = new ByteOrderMark[] { new ByteOrderMark("UTF-8", new int[] { 60, 63, 120, 109 }), new ByteOrderMark("UTF-16BE", new int[] { 0, 60, 0, 63 }), new ByteOrderMark("UTF-16LE", new int[] { 60, 0, 63, 0 }), new ByteOrderMark("UTF-32BE", new int[] { 0, 0, 0, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109 }), new ByteOrderMark("UTF-32LE", new int[] { 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109, 0, 0, 0 }), new ByteOrderMark("CP1047", new int[] { 76, 111, 167, 148 }) };
        CHARSET_PATTERN = Pattern.compile("charset=[\"']?([.[^; \"']]*)[\"']?");
        ENCODING_PATTERN = Pattern.compile("<\\?xml.*encoding[\\s]*=[\\s]*((?:\".[^\"]*\")|(?:'.[^']*'))", 8);
    }
}

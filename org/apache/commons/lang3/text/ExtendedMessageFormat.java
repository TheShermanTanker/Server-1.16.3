package org.apache.commons.lang3.text;

import org.apache.commons.lang3.ObjectUtils;
import java.util.Iterator;
import java.text.Format;
import java.util.Collection;
import org.apache.commons.lang3.Validate;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.text.MessageFormat;

public class ExtendedMessageFormat extends MessageFormat {
    private static final long serialVersionUID = -2362048321261811743L;
    private static final int HASH_SEED = 31;
    private static final String DUMMY_PATTERN = "";
    private static final char START_FMT = ',';
    private static final char END_FE = '}';
    private static final char START_FE = '{';
    private static final char QUOTE = '\'';
    private String toPattern;
    private final Map<String, ? extends FormatFactory> registry;
    
    public ExtendedMessageFormat(final String pattern) {
        this(pattern, Locale.getDefault());
    }
    
    public ExtendedMessageFormat(final String pattern, final Locale locale) {
        this(pattern, locale, null);
    }
    
    public ExtendedMessageFormat(final String pattern, final Map<String, ? extends FormatFactory> registry) {
        this(pattern, Locale.getDefault(), registry);
    }
    
    public ExtendedMessageFormat(final String pattern, final Locale locale, final Map<String, ? extends FormatFactory> registry) {
        super("");
        this.setLocale(locale);
        this.registry = registry;
        this.applyPattern(pattern);
    }
    
    public String toPattern() {
        return this.toPattern;
    }
    
    public final void applyPattern(final String pattern) {
        if (this.registry == null) {
            super.applyPattern(pattern);
            this.toPattern = super.toPattern();
            return;
        }
        final ArrayList<Format> foundFormats = (ArrayList<Format>)new ArrayList();
        final ArrayList<String> foundDescriptions = (ArrayList<String>)new ArrayList();
        final StringBuilder stripCustom = new StringBuilder(pattern.length());
        final ParsePosition pos = new ParsePosition(0);
        final char[] c = pattern.toCharArray();
        int fmtCount = 0;
        while (pos.getIndex() < pattern.length()) {
            switch (c[pos.getIndex()]) {
                case '\'': {
                    this.appendQuotedString(pattern, pos, stripCustom);
                    continue;
                }
                case '{': {
                    ++fmtCount;
                    this.seekNonWs(pattern, pos);
                    final int start = pos.getIndex();
                    final int index = this.readArgumentIndex(pattern, this.next(pos));
                    stripCustom.append('{').append(index);
                    this.seekNonWs(pattern, pos);
                    Format format = null;
                    String formatDescription = null;
                    if (c[pos.getIndex()] == ',') {
                        formatDescription = this.parseFormatDescription(pattern, this.next(pos));
                        format = this.getFormat(formatDescription);
                        if (format == null) {
                            stripCustom.append(',').append(formatDescription);
                        }
                    }
                    foundFormats.add(format);
                    foundDescriptions.add(((format == null) ? null : formatDescription));
                    Validate.isTrue(foundFormats.size() == fmtCount);
                    Validate.isTrue(foundDescriptions.size() == fmtCount);
                    if (c[pos.getIndex()] != '}') {
                        throw new IllegalArgumentException(new StringBuilder().append("Unreadable format element at position ").append(start).toString());
                    }
                    break;
                }
            }
            stripCustom.append(c[pos.getIndex()]);
            this.next(pos);
        }
        super.applyPattern(stripCustom.toString());
        this.toPattern = this.insertFormats(super.toPattern(), foundDescriptions);
        if (this.containsElements(foundFormats)) {
            final Format[] origFormats = this.getFormats();
            int i = 0;
            for (final Format f : foundFormats) {
                if (f != null) {
                    origFormats[i] = f;
                }
                ++i;
            }
            super.setFormats(origFormats);
        }
    }
    
    public void setFormat(final int formatElementIndex, final Format newFormat) {
        throw new UnsupportedOperationException();
    }
    
    public void setFormatByArgumentIndex(final int argumentIndex, final Format newFormat) {
        throw new UnsupportedOperationException();
    }
    
    public void setFormats(final Format[] newFormats) {
        throw new UnsupportedOperationException();
    }
    
    public void setFormatsByArgumentIndex(final Format[] newFormats) {
        throw new UnsupportedOperationException();
    }
    
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (ObjectUtils.notEqual(this.getClass(), obj.getClass())) {
            return false;
        }
        final ExtendedMessageFormat rhs = (ExtendedMessageFormat)obj;
        return !ObjectUtils.notEqual(this.toPattern, rhs.toPattern) && !ObjectUtils.notEqual(this.registry, rhs.registry);
    }
    
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ObjectUtils.hashCode(this.registry);
        result = 31 * result + ObjectUtils.hashCode(this.toPattern);
        return result;
    }
    
    private Format getFormat(final String desc) {
        if (this.registry != null) {
            String name = desc;
            String args = null;
            final int i = desc.indexOf(44);
            if (i > 0) {
                name = desc.substring(0, i).trim();
                args = desc.substring(i + 1).trim();
            }
            final FormatFactory factory = (FormatFactory)this.registry.get(name);
            if (factory != null) {
                return factory.getFormat(name, args, this.getLocale());
            }
        }
        return null;
    }
    
    private int readArgumentIndex(final String pattern, final ParsePosition pos) {
        final int start = pos.getIndex();
        this.seekNonWs(pattern, pos);
        final StringBuilder result = new StringBuilder();
        boolean error = false;
        while (!error && pos.getIndex() < pattern.length()) {
            char c = pattern.charAt(pos.getIndex());
            Label_0149: {
                if (Character.isWhitespace(c)) {
                    this.seekNonWs(pattern, pos);
                    c = pattern.charAt(pos.getIndex());
                    if (c != ',' && c != '}') {
                        error = true;
                        break Label_0149;
                    }
                }
                if ((c == ',' || c == '}') && result.length() > 0) {
                    try {
                        return Integer.parseInt(result.toString());
                    }
                    catch (NumberFormatException ex) {}
                }
                error = !Character.isDigit(c);
                result.append(c);
            }
            this.next(pos);
        }
        if (error) {
            throw new IllegalArgumentException(new StringBuilder().append("Invalid format argument index at position ").append(start).append(": ").append(pattern.substring(start, pos.getIndex())).toString());
        }
        throw new IllegalArgumentException(new StringBuilder().append("Unterminated format element at position ").append(start).toString());
    }
    
    private String parseFormatDescription(final String pattern, final ParsePosition pos) {
        final int start = pos.getIndex();
        this.seekNonWs(pattern, pos);
        final int text = pos.getIndex();
        int depth = 1;
        while (pos.getIndex() < pattern.length()) {
            switch (pattern.charAt(pos.getIndex())) {
                case '{': {
                    ++depth;
                    break;
                }
                case '}': {
                    if (--depth == 0) {
                        return pattern.substring(text, pos.getIndex());
                    }
                    break;
                }
                case '\'': {
                    this.getQuotedString(pattern, pos);
                    break;
                }
            }
            this.next(pos);
        }
        throw new IllegalArgumentException(new StringBuilder().append("Unterminated format element at position ").append(start).toString());
    }
    
    private String insertFormats(final String pattern, final ArrayList<String> customPatterns) {
        if (!this.containsElements(customPatterns)) {
            return pattern;
        }
        final StringBuilder sb = new StringBuilder(pattern.length() * 2);
        final ParsePosition pos = new ParsePosition(0);
        int fe = -1;
        int depth = 0;
        while (pos.getIndex() < pattern.length()) {
            final char c = pattern.charAt(pos.getIndex());
            switch (c) {
                case '\'': {
                    this.appendQuotedString(pattern, pos, sb);
                    continue;
                }
                case '{': {
                    ++depth;
                    sb.append('{').append(this.readArgumentIndex(pattern, this.next(pos)));
                    if (depth == 1) {
                        ++fe;
                        final String customPattern = (String)customPatterns.get(fe);
                        if (customPattern == null) {
                            continue;
                        }
                        sb.append(',').append(customPattern);
                        continue;
                    }
                    continue;
                }
                case '}': {
                    --depth;
                    break;
                }
            }
            sb.append(c);
            this.next(pos);
        }
        return sb.toString();
    }
    
    private void seekNonWs(final String pattern, final ParsePosition pos) {
        int len = 0;
        final char[] buffer = pattern.toCharArray();
        do {
            len = StrMatcher.splitMatcher().isMatch(buffer, pos.getIndex());
            pos.setIndex(pos.getIndex() + len);
        } while (len > 0 && pos.getIndex() < pattern.length());
    }
    
    private ParsePosition next(final ParsePosition pos) {
        pos.setIndex(pos.getIndex() + 1);
        return pos;
    }
    
    private StringBuilder appendQuotedString(final String pattern, final ParsePosition pos, final StringBuilder appendTo) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ifne            31
        //     6: aload_1         /* pattern */
        //     7: invokevirtual   java/lang/String.toCharArray:()[C
        //    10: aload_2         /* pos */
        //    11: invokevirtual   java/text/ParsePosition.getIndex:()I
        //    14: caload         
        //    15: bipush          39
        //    17: if_icmpeq       31
        //    20: new             Ljava/lang/AssertionError;
        //    23: dup            
        //    24: ldc_w           "Quoted string must start with quote character"
        //    27: invokespecial   java/lang/AssertionError.<init>:(Ljava/lang/Object;)V
        //    30: athrow         
        //    31: aload_3         /* appendTo */
        //    32: ifnull          42
        //    35: aload_3         /* appendTo */
        //    36: bipush          39
        //    38: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //    41: pop            
        //    42: aload_0         /* this */
        //    43: aload_2         /* pos */
        //    44: invokespecial   org/apache/commons/lang3/text/ExtendedMessageFormat.next:(Ljava/text/ParsePosition;)Ljava/text/ParsePosition;
        //    47: pop            
        //    48: aload_2         /* pos */
        //    49: invokevirtual   java/text/ParsePosition.getIndex:()I
        //    52: istore          start
        //    54: aload_1         /* pattern */
        //    55: invokevirtual   java/lang/String.toCharArray:()[C
        //    58: astore          c
        //    60: iload           start
        //    62: istore          lastHold
        //    64: aload_2         /* pos */
        //    65: invokevirtual   java/text/ParsePosition.getIndex:()I
        //    68: istore          i
        //    70: iload           i
        //    72: aload_1         /* pattern */
        //    73: invokevirtual   java/lang/String.length:()I
        //    76: if_icmpge       146
        //    79: aload           c
        //    81: aload_2         /* pos */
        //    82: invokevirtual   java/text/ParsePosition.getIndex:()I
        //    85: caload         
        //    86: lookupswitch {
        //               39: 104
        //          default: 134
        //        }
        //   104: aload_0         /* this */
        //   105: aload_2         /* pos */
        //   106: invokespecial   org/apache/commons/lang3/text/ExtendedMessageFormat.next:(Ljava/text/ParsePosition;)Ljava/text/ParsePosition;
        //   109: pop            
        //   110: aload_3         /* appendTo */
        //   111: ifnonnull       118
        //   114: aconst_null    
        //   115: goto            133
        //   118: aload_3         /* appendTo */
        //   119: aload           c
        //   121: iload           lastHold
        //   123: aload_2         /* pos */
        //   124: invokevirtual   java/text/ParsePosition.getIndex:()I
        //   127: iload           lastHold
        //   129: isub           
        //   130: invokevirtual   java/lang/StringBuilder.append:([CII)Ljava/lang/StringBuilder;
        //   133: areturn        
        //   134: aload_0         /* this */
        //   135: aload_2         /* pos */
        //   136: invokespecial   org/apache/commons/lang3/text/ExtendedMessageFormat.next:(Ljava/text/ParsePosition;)Ljava/text/ParsePosition;
        //   139: pop            
        //   140: iinc            i, 1
        //   143: goto            70
        //   146: new             Ljava/lang/IllegalArgumentException;
        //   149: dup            
        //   150: new             Ljava/lang/StringBuilder;
        //   153: dup            
        //   154: invokespecial   java/lang/StringBuilder.<init>:()V
        //   157: ldc_w           "Unterminated quoted string at position "
        //   160: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   163: iload           start
        //   165: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   168: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   171: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   174: athrow         
        //    MethodParameters:
        //  Name      Flags  
        //  --------  -----
        //  pattern   
        //  pos       
        //  appendTo  
        //    StackMapTable: 00 08 1F 0A FF 00 1B 00 08 07 00 02 07 00 4F 07 00 58 07 00 4D 01 07 00 5F 01 01 00 00 21 0D 4E 07 00 4D 00 FA 00 0B
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.shouldInferVariableType(TypeAnalysis.java:613)
        //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:265)
        //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:272)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:158)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:93)
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
    
    private void getQuotedString(final String pattern, final ParsePosition pos) {
        this.appendQuotedString(pattern, pos, null);
    }
    
    private boolean containsElements(final Collection<?> coll) {
        if (coll == null || coll.isEmpty()) {
            return false;
        }
        for (final Object name : coll) {
            if (name != null) {
                return true;
            }
        }
        return false;
    }
}

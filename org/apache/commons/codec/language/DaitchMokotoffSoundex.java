package org.apache.commons.codec.language;

import java.util.Arrays;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import org.apache.commons.codec.EncoderException;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.StringEncoder;

public class DaitchMokotoffSoundex implements StringEncoder {
    private static final String COMMENT = "//";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String MULTILINE_COMMENT_END = "*/";
    private static final String MULTILINE_COMMENT_START = "/*";
    private static final String RESOURCE_FILE = "org/apache/commons/codec/language/dmrules.txt";
    private static final int MAX_LENGTH = 6;
    private static final Map<Character, List<Rule>> RULES;
    private static final Map<Character, Character> FOLDINGS;
    private final boolean folding;
    
    private static void parseRules(final Scanner scanner, final String location, final Map<Character, List<Rule>> ruleMapping, final Map<Character, Character> asciiFoldings) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore          currentLine
        //     3: iconst_0       
        //     4: istore          inMultilineComment
        //     6: aload_0         /* scanner */
        //     7: invokevirtual   java/util/Scanner.hasNextLine:()Z
        //    10: ifeq            503
        //    13: iinc            currentLine, 1
        //    16: aload_0         /* scanner */
        //    17: invokevirtual   java/util/Scanner.nextLine:()Ljava/lang/String;
        //    20: astore          rawLine
        //    22: aload           rawLine
        //    24: astore          line
        //    26: iload           inMultilineComment
        //    28: ifeq            47
        //    31: aload           line
        //    33: ldc             "*/"
        //    35: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //    38: ifeq            6
        //    41: iconst_0       
        //    42: istore          inMultilineComment
        //    44: goto            6
        //    47: aload           line
        //    49: ldc             "/*"
        //    51: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    54: ifeq            63
        //    57: iconst_1       
        //    58: istore          inMultilineComment
        //    60: goto            500
        //    63: aload           line
        //    65: ldc             "//"
        //    67: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
        //    70: istore          cmtI
        //    72: iload           cmtI
        //    74: iflt            87
        //    77: aload           line
        //    79: iconst_0       
        //    80: iload           cmtI
        //    82: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //    85: astore          line
        //    87: aload           line
        //    89: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    92: astore          line
        //    94: aload           line
        //    96: invokevirtual   java/lang/String.length:()I
        //    99: ifne            105
        //   102: goto            6
        //   105: aload           line
        //   107: ldc             "="
        //   109: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   112: ifeq            274
        //   115: aload           line
        //   117: ldc             "="
        //   119: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   122: astore          parts
        //   124: aload           parts
        //   126: arraylength    
        //   127: iconst_2       
        //   128: if_icmpeq       179
        //   131: new             Ljava/lang/IllegalArgumentException;
        //   134: dup            
        //   135: new             Ljava/lang/StringBuilder;
        //   138: dup            
        //   139: invokespecial   java/lang/StringBuilder.<init>:()V
        //   142: ldc             "Malformed folding statement split into "
        //   144: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   147: aload           parts
        //   149: arraylength    
        //   150: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   153: ldc             " parts: "
        //   155: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   158: aload           rawLine
        //   160: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   163: ldc             " in "
        //   165: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   168: aload_1         /* location */
        //   169: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   172: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   175: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   178: athrow         
        //   179: aload           parts
        //   181: iconst_0       
        //   182: aaload         
        //   183: astore          leftCharacter
        //   185: aload           parts
        //   187: iconst_1       
        //   188: aaload         
        //   189: astore          rightCharacter
        //   191: aload           leftCharacter
        //   193: invokevirtual   java/lang/String.length:()I
        //   196: iconst_1       
        //   197: if_icmpne       209
        //   200: aload           rightCharacter
        //   202: invokevirtual   java/lang/String.length:()I
        //   205: iconst_1       
        //   206: if_icmpeq       246
        //   209: new             Ljava/lang/IllegalArgumentException;
        //   212: dup            
        //   213: new             Ljava/lang/StringBuilder;
        //   216: dup            
        //   217: invokespecial   java/lang/StringBuilder.<init>:()V
        //   220: ldc             "Malformed folding statement - patterns are not single characters: "
        //   222: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   225: aload           rawLine
        //   227: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   230: ldc             " in "
        //   232: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   235: aload_1         /* location */
        //   236: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   239: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   242: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   245: athrow         
        //   246: aload_3         /* asciiFoldings */
        //   247: aload           leftCharacter
        //   249: iconst_0       
        //   250: invokevirtual   java/lang/String.charAt:(I)C
        //   253: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   256: aload           rightCharacter
        //   258: iconst_0       
        //   259: invokevirtual   java/lang/String.charAt:(I)C
        //   262: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   265: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   270: pop            
        //   271: goto            500
        //   274: aload           line
        //   276: ldc             "\\s+"
        //   278: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   281: astore          parts
        //   283: aload           parts
        //   285: arraylength    
        //   286: iconst_4       
        //   287: if_icmpeq       338
        //   290: new             Ljava/lang/IllegalArgumentException;
        //   293: dup            
        //   294: new             Ljava/lang/StringBuilder;
        //   297: dup            
        //   298: invokespecial   java/lang/StringBuilder.<init>:()V
        //   301: ldc             "Malformed rule statement split into "
        //   303: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   306: aload           parts
        //   308: arraylength    
        //   309: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   312: ldc             " parts: "
        //   314: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   317: aload           rawLine
        //   319: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   322: ldc             " in "
        //   324: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   327: aload_1         /* location */
        //   328: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   331: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   334: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   337: athrow         
        //   338: aload           parts
        //   340: iconst_0       
        //   341: aaload         
        //   342: invokestatic    org/apache/commons/codec/language/DaitchMokotoffSoundex.stripQuotes:(Ljava/lang/String;)Ljava/lang/String;
        //   345: astore          pattern
        //   347: aload           parts
        //   349: iconst_1       
        //   350: aaload         
        //   351: invokestatic    org/apache/commons/codec/language/DaitchMokotoffSoundex.stripQuotes:(Ljava/lang/String;)Ljava/lang/String;
        //   354: astore          replacement1
        //   356: aload           parts
        //   358: iconst_2       
        //   359: aaload         
        //   360: invokestatic    org/apache/commons/codec/language/DaitchMokotoffSoundex.stripQuotes:(Ljava/lang/String;)Ljava/lang/String;
        //   363: astore          replacement2
        //   365: aload           parts
        //   367: iconst_3       
        //   368: aaload         
        //   369: invokestatic    org/apache/commons/codec/language/DaitchMokotoffSoundex.stripQuotes:(Ljava/lang/String;)Ljava/lang/String;
        //   372: astore          replacement3
        //   374: new             Lorg/apache/commons/codec/language/DaitchMokotoffSoundex$Rule;
        //   377: dup            
        //   378: aload           pattern
        //   380: aload           replacement1
        //   382: aload           replacement2
        //   384: aload           replacement3
        //   386: invokespecial   org/apache/commons/codec/language/DaitchMokotoffSoundex$Rule.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   389: astore          r
        //   391: aload           r
        //   393: invokestatic    org/apache/commons/codec/language/DaitchMokotoffSoundex$Rule.access$000:(Lorg/apache/commons/codec/language/DaitchMokotoffSoundex$Rule;)Ljava/lang/String;
        //   396: iconst_0       
        //   397: invokevirtual   java/lang/String.charAt:(I)C
        //   400: istore          patternKey
        //   402: aload_2         /* ruleMapping */
        //   403: iload           patternKey
        //   405: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   408: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   413: checkcast       Ljava/util/List;
        //   416: astore          rules
        //   418: aload           rules
        //   420: ifnonnull       446
        //   423: new             Ljava/util/ArrayList;
        //   426: dup            
        //   427: invokespecial   java/util/ArrayList.<init>:()V
        //   430: astore          rules
        //   432: aload_2         /* ruleMapping */
        //   433: iload           patternKey
        //   435: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   438: aload           rules
        //   440: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   445: pop            
        //   446: aload           rules
        //   448: aload           r
        //   450: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   455: pop            
        //   456: goto            500
        //   459: astore          e
        //   461: new             Ljava/lang/IllegalStateException;
        //   464: dup            
        //   465: new             Ljava/lang/StringBuilder;
        //   468: dup            
        //   469: invokespecial   java/lang/StringBuilder.<init>:()V
        //   472: ldc             "Problem parsing line '"
        //   474: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   477: iload           currentLine
        //   479: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   482: ldc             "' in "
        //   484: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   487: aload_1         /* location */
        //   488: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   491: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   494: aload           e
        //   496: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   499: athrow         
        //   500: goto            6
        //   503: return         
        //    Signature:
        //  (Ljava/util/Scanner;Ljava/lang/String;Ljava/util/Map<Ljava/lang/Character;Ljava/util/List<Lorg/apache/commons/codec/language/DaitchMokotoffSoundex$Rule;>;>;Ljava/util/Map<Ljava/lang/Character;Ljava/lang/Character;>;)V
        //    MethodParameters:
        //  Name           Flags  
        //  -------------  -----
        //  scanner        
        //  location       
        //  ruleMapping    
        //  asciiFoldings  
        //    StackMapTable: 00 0E FD 00 06 01 01 FD 00 28 07 00 43 07 00 43 0F FC 00 17 01 11 FC 00 49 07 00 7E FD 00 1D 07 00 43 07 00 43 24 F8 00 1B FC 00 3F 07 00 7E FF 00 6B 00 11 07 00 39 07 00 43 07 00 13 07 00 13 01 01 07 00 43 07 00 43 01 07 00 7E 07 00 43 07 00 43 07 00 43 07 00 43 07 00 09 01 07 00 A3 00 00 FF 00 0C 00 0A 07 00 39 07 00 43 07 00 13 07 00 13 01 01 07 00 43 07 00 43 01 07 00 7E 00 01 07 00 37 FF 00 28 00 06 07 00 39 07 00 43 07 00 13 07 00 13 01 01 00 00 02
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  338    456    459    500    Ljava/lang/IllegalArgumentException;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 7
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.resolve(CoreMetadataFactory.java:744)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:540)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
        //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:237)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.helpCC(ForkJoinPool.java:1116)
        //     at java.base/java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:1966)
        //     at java.base/java.util.concurrent.ForkJoinTask.tryExternalHelp(ForkJoinTask.java:378)
        //     at java.base/java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:323)
        //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:412)
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
    
    private static String stripQuotes(String str) {
        if (str.startsWith("\"")) {
            str = str.substring(1);
        }
        if (str.endsWith("\"")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
    
    public DaitchMokotoffSoundex() {
        this(true);
    }
    
    public DaitchMokotoffSoundex(final boolean folding) {
        this.folding = folding;
    }
    
    private String cleanup(final String input) {
        final StringBuilder sb = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (!Character.isWhitespace(ch)) {
                ch = Character.toLowerCase(ch);
                if (this.folding && DaitchMokotoffSoundex.FOLDINGS.containsKey(ch)) {
                    ch = (char)DaitchMokotoffSoundex.FOLDINGS.get(ch);
                }
                sb.append(ch);
            }
        }
        return sb.toString();
    }
    
    public Object encode(final Object obj) throws EncoderException {
        if (!(obj instanceof String)) {
            throw new EncoderException("Parameter supplied to DaitchMokotoffSoundex encode is not of type java.lang.String");
        }
        return this.encode((String)obj);
    }
    
    public String encode(final String source) {
        if (source == null) {
            return null;
        }
        return this.soundex(source, false)[0];
    }
    
    public String soundex(final String source) {
        final String[] branches = this.soundex(source, true);
        final StringBuilder sb = new StringBuilder();
        int index = 0;
        for (final String branch : branches) {
            sb.append(branch);
            if (++index < branches.length) {
                sb.append('|');
            }
        }
        return sb.toString();
    }
    
    private String[] soundex(final String source, final boolean branching) {
        if (source == null) {
            return null;
        }
        final String input = this.cleanup(source);
        final Set<Branch> currentBranches = (Set<Branch>)new LinkedHashSet();
        currentBranches.add(new Branch());
        char lastChar = '\0';
        for (int index = 0; index < input.length(); ++index) {
            final char ch = input.charAt(index);
            if (!Character.isWhitespace(ch)) {
                final String inputContext = input.substring(index);
                final List<Rule> rules = (List<Rule>)DaitchMokotoffSoundex.RULES.get(ch);
                if (rules != null) {
                    final List<Branch> nextBranches = (List<Branch>)(branching ? new ArrayList() : Collections.EMPTY_LIST);
                    for (final Rule rule : rules) {
                        if (rule.matches(inputContext)) {
                            if (branching) {
                                nextBranches.clear();
                            }
                            final String[] replacements = rule.getReplacements(inputContext, lastChar == '\0');
                            final boolean branchingRequired = replacements.length > 1 && branching;
                            for (final Branch branch : currentBranches) {
                                for (final String nextReplacement : replacements) {
                                    final Branch nextBranch = branchingRequired ? branch.createBranch() : branch;
                                    final boolean force = (lastChar == 'm' && ch == 'n') || (lastChar == 'n' && ch == 'm');
                                    nextBranch.processNextReplacement(nextReplacement, force);
                                    if (!branching) {
                                        break;
                                    }
                                    nextBranches.add(nextBranch);
                                }
                            }
                            if (branching) {
                                currentBranches.clear();
                                currentBranches.addAll((Collection)nextBranches);
                            }
                            index += rule.getPatternLength() - 1;
                            break;
                        }
                    }
                    lastChar = ch;
                }
            }
        }
        final String[] result = new String[currentBranches.size()];
        int index2 = 0;
        for (final Branch branch2 : currentBranches) {
            branch2.finish();
            result[index2++] = branch2.toString();
        }
        return result;
    }
    
    static {
        RULES = (Map)new HashMap();
        FOLDINGS = (Map)new HashMap();
        final InputStream rulesIS = DaitchMokotoffSoundex.class.getClassLoader().getResourceAsStream("org/apache/commons/codec/language/dmrules.txt");
        if (rulesIS == null) {
            throw new IllegalArgumentException("Unable to load resource: org/apache/commons/codec/language/dmrules.txt");
        }
        final Scanner scanner = new Scanner(rulesIS, "UTF-8");
        parseRules(scanner, "org/apache/commons/codec/language/dmrules.txt", DaitchMokotoffSoundex.RULES, DaitchMokotoffSoundex.FOLDINGS);
        scanner.close();
        for (final Map.Entry<Character, List<Rule>> rule : DaitchMokotoffSoundex.RULES.entrySet()) {
            final List<Rule> ruleList = (List<Rule>)rule.getValue();
            Collections.sort((List)ruleList, (Comparator)new Comparator<Rule>() {
                public int compare(final Rule rule1, final Rule rule2) {
                    return rule2.getPatternLength() - rule1.getPatternLength();
                }
            });
        }
    }
    
    private static final class Branch {
        private final StringBuilder builder;
        private String cachedString;
        private String lastReplacement;
        
        private Branch() {
            this.builder = new StringBuilder();
            this.lastReplacement = null;
            this.cachedString = null;
        }
        
        public Branch createBranch() {
            final Branch branch = new Branch();
            branch.builder.append(this.toString());
            branch.lastReplacement = this.lastReplacement;
            return branch;
        }
        
        public boolean equals(final Object other) {
            return this == other || (other instanceof Branch && this.toString().equals(((Branch)other).toString()));
        }
        
        public void finish() {
            while (this.builder.length() < 6) {
                this.builder.append('0');
                this.cachedString = null;
            }
        }
        
        public int hashCode() {
            return this.toString().hashCode();
        }
        
        public void processNextReplacement(final String replacement, final boolean forceAppend) {
            final boolean append = this.lastReplacement == null || !this.lastReplacement.endsWith(replacement) || forceAppend;
            if (append && this.builder.length() < 6) {
                this.builder.append(replacement);
                if (this.builder.length() > 6) {
                    this.builder.delete(6, this.builder.length());
                }
                this.cachedString = null;
            }
            this.lastReplacement = replacement;
        }
        
        public String toString() {
            if (this.cachedString == null) {
                this.cachedString = this.builder.toString();
            }
            return this.cachedString;
        }
    }
    
    private static final class Rule {
        private final String pattern;
        private final String[] replacementAtStart;
        private final String[] replacementBeforeVowel;
        private final String[] replacementDefault;
        
        protected Rule(final String pattern, final String replacementAtStart, final String replacementBeforeVowel, final String replacementDefault) {
            this.pattern = pattern;
            this.replacementAtStart = replacementAtStart.split("\\|");
            this.replacementBeforeVowel = replacementBeforeVowel.split("\\|");
            this.replacementDefault = replacementDefault.split("\\|");
        }
        
        public int getPatternLength() {
            return this.pattern.length();
        }
        
        public String[] getReplacements(final String context, final boolean atStart) {
            if (atStart) {
                return this.replacementAtStart;
            }
            final int nextIndex = this.getPatternLength();
            final boolean nextCharIsVowel = nextIndex < context.length() && this.isVowel(context.charAt(nextIndex));
            if (nextCharIsVowel) {
                return this.replacementBeforeVowel;
            }
            return this.replacementDefault;
        }
        
        private boolean isVowel(final char ch) {
            return ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u';
        }
        
        public boolean matches(final String context) {
            return context.startsWith(this.pattern);
        }
        
        public String toString() {
            return String.format("%s=(%s,%s,%s)", new Object[] { this.pattern, Arrays.asList((Object[])this.replacementAtStart), Arrays.asList((Object[])this.replacementBeforeVowel), Arrays.asList((Object[])this.replacementDefault) });
        }
    }
}

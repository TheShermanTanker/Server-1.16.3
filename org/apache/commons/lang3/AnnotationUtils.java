package org.apache.commons.lang3;

import java.util.Iterator;
import java.util.Arrays;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AnnotationUtils {
    private static final ToStringStyle TO_STRING_STYLE;
    
    public static boolean equals(final Annotation a1, final Annotation a2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* a2 */
        //     2: if_acmpne       7
        //     5: iconst_1       
        //     6: ireturn        
        //     7: aload_0         /* a1 */
        //     8: ifnull          15
        //    11: aload_1         /* a2 */
        //    12: ifnonnull       17
        //    15: iconst_0       
        //    16: ireturn        
        //    17: aload_0         /* a1 */
        //    18: invokeinterface java/lang/annotation/Annotation.annotationType:()Ljava/lang/Class;
        //    23: astore_2        /* type */
        //    24: aload_1         /* a2 */
        //    25: invokeinterface java/lang/annotation/Annotation.annotationType:()Ljava/lang/Class;
        //    30: astore_3        /* type2 */
        //    31: aload_2         /* type */
        //    32: ldc             "Annotation %s with null annotationType()"
        //    34: iconst_1       
        //    35: anewarray       Ljava/lang/Object;
        //    38: dup            
        //    39: iconst_0       
        //    40: aload_0         /* a1 */
        //    41: aastore        
        //    42: invokestatic    org/apache/commons/lang3/Validate.notNull:(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;
        //    45: pop            
        //    46: aload_3         /* type2 */
        //    47: ldc             "Annotation %s with null annotationType()"
        //    49: iconst_1       
        //    50: anewarray       Ljava/lang/Object;
        //    53: dup            
        //    54: iconst_0       
        //    55: aload_1         /* a2 */
        //    56: aastore        
        //    57: invokestatic    org/apache/commons/lang3/Validate.notNull:(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;
        //    60: pop            
        //    61: aload_2         /* type */
        //    62: aload_3         /* type2 */
        //    63: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //    66: ifne            71
        //    69: iconst_0       
        //    70: ireturn        
        //    71: aload_2         /* type */
        //    72: invokevirtual   java/lang/Class.getDeclaredMethods:()[Ljava/lang/reflect/Method;
        //    75: astore          4
        //    77: aload           4
        //    79: arraylength    
        //    80: istore          5
        //    82: iconst_0       
        //    83: istore          6
        //    85: iload           6
        //    87: iload           5
        //    89: if_icmpge       166
        //    92: aload           4
        //    94: iload           6
        //    96: aaload         
        //    97: astore          m
        //    99: aload           m
        //   101: invokevirtual   java/lang/reflect/Method.getParameterTypes:()[Ljava/lang/Class;
        //   104: arraylength    
        //   105: ifne            160
        //   108: aload           m
        //   110: invokevirtual   java/lang/reflect/Method.getReturnType:()Ljava/lang/Class;
        //   113: invokestatic    org/apache/commons/lang3/AnnotationUtils.isValidAnnotationMemberType:(Ljava/lang/Class;)Z
        //   116: ifeq            160
        //   119: aload           m
        //   121: aload_0         /* a1 */
        //   122: iconst_0       
        //   123: anewarray       Ljava/lang/Object;
        //   126: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   129: astore          v1
        //   131: aload           m
        //   133: aload_1         /* a2 */
        //   134: iconst_0       
        //   135: anewarray       Ljava/lang/Object;
        //   138: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   141: astore          v2
        //   143: aload           m
        //   145: invokevirtual   java/lang/reflect/Method.getReturnType:()Ljava/lang/Class;
        //   148: aload           v1
        //   150: aload           v2
        //   152: invokestatic    org/apache/commons/lang3/AnnotationUtils.memberEquals:(Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/Object;)Z
        //   155: ifne            160
        //   158: iconst_0       
        //   159: ireturn        
        //   160: iinc            6, 1
        //   163: goto            85
        //   166: goto            177
        //   169: astore          ex
        //   171: iconst_0       
        //   172: ireturn        
        //   173: astore          ex
        //   175: iconst_0       
        //   176: ireturn        
        //   177: iconst_1       
        //   178: ireturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  a1    
        //  a2    
        //    StackMapTable: 00 0A 07 07 01 FD 00 35 07 00 2A 07 00 2A FE 00 0D 07 00 30 01 01 FB 00 4A F8 00 05 42 07 00 15 43 07 00 17 03
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                         
        //  -----  -----  -----  -----  ---------------------------------------------
        //  71     159    169    173    Ljava/lang/IllegalAccessException;
        //  160    166    169    173    Ljava/lang/IllegalAccessException;
        //  71     159    173    177    Ljava/lang/reflect/InvocationTargetException;
        //  160    166    173    177    Ljava/lang/reflect/InvocationTargetException;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
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
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:547)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static int hashCode(final Annotation a) {
        int result = 0;
        final Class<? extends Annotation> type = a.annotationType();
        for (final Method m : type.getDeclaredMethods()) {
            try {
                final Object value = m.invoke(a, new Object[0]);
                if (value == null) {
                    throw new IllegalStateException(String.format("Annotation method %s returned null", new Object[] { m }));
                }
                result += hashMember(m.getName(), value);
            }
            catch (RuntimeException ex) {
                throw ex;
            }
            catch (Exception ex2) {
                throw new RuntimeException((Throwable)ex2);
            }
        }
        return result;
    }
    
    public static String toString(final Annotation a) {
        final ToStringBuilder builder = new ToStringBuilder(a, AnnotationUtils.TO_STRING_STYLE);
        for (final Method m : a.annotationType().getDeclaredMethods()) {
            if (m.getParameterTypes().length <= 0) {
                try {
                    builder.append(m.getName(), m.invoke(a, new Object[0]));
                }
                catch (RuntimeException ex) {
                    throw ex;
                }
                catch (Exception ex2) {
                    throw new RuntimeException((Throwable)ex2);
                }
            }
        }
        return builder.build();
    }
    
    public static boolean isValidAnnotationMemberType(Class<?> type) {
        if (type == null) {
            return false;
        }
        if (type.isArray()) {
            type = type.getComponentType();
        }
        return type.isPrimitive() || type.isEnum() || type.isAnnotation() || String.class.equals(type) || Class.class.equals(type);
    }
    
    private static int hashMember(final String name, final Object value) {
        final int part1 = name.hashCode() * 127;
        if (value.getClass().isArray()) {
            return part1 ^ arrayMemberHash(value.getClass().getComponentType(), value);
        }
        if (value instanceof Annotation) {
            return part1 ^ hashCode((Annotation)value);
        }
        return part1 ^ value.hashCode();
    }
    
    private static boolean memberEquals(final Class<?> type, final Object o1, final Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (type.isArray()) {
            return arrayMemberEquals(type.getComponentType(), o1, o2);
        }
        if (type.isAnnotation()) {
            return equals((Annotation)o1, (Annotation)o2);
        }
        return o1.equals(o2);
    }
    
    private static boolean arrayMemberEquals(final Class<?> componentType, final Object o1, final Object o2) {
        if (componentType.isAnnotation()) {
            return annotationArrayMemberEquals((Annotation[])o1, (Annotation[])o2);
        }
        if (componentType.equals(Byte.TYPE)) {
            return Arrays.equals((byte[])o1, (byte[])o2);
        }
        if (componentType.equals(Short.TYPE)) {
            return Arrays.equals((short[])o1, (short[])o2);
        }
        if (componentType.equals(Integer.TYPE)) {
            return Arrays.equals((int[])o1, (int[])o2);
        }
        if (componentType.equals(Character.TYPE)) {
            return Arrays.equals((char[])o1, (char[])o2);
        }
        if (componentType.equals(Long.TYPE)) {
            return Arrays.equals((long[])o1, (long[])o2);
        }
        if (componentType.equals(Float.TYPE)) {
            return Arrays.equals((float[])o1, (float[])o2);
        }
        if (componentType.equals(Double.TYPE)) {
            return Arrays.equals((double[])o1, (double[])o2);
        }
        if (componentType.equals(Boolean.TYPE)) {
            return Arrays.equals((boolean[])o1, (boolean[])o2);
        }
        return Arrays.equals((Object[])o1, (Object[])o2);
    }
    
    private static boolean annotationArrayMemberEquals(final Annotation[] a1, final Annotation[] a2) {
        if (a1.length != a2.length) {
            return false;
        }
        for (int i = 0; i < a1.length; ++i) {
            if (!equals(a1[i], a2[i])) {
                return false;
            }
        }
        return true;
    }
    
    private static int arrayMemberHash(final Class<?> componentType, final Object o) {
        if (componentType.equals(Byte.TYPE)) {
            return Arrays.hashCode((byte[])o);
        }
        if (componentType.equals(Short.TYPE)) {
            return Arrays.hashCode((short[])o);
        }
        if (componentType.equals(Integer.TYPE)) {
            return Arrays.hashCode((int[])o);
        }
        if (componentType.equals(Character.TYPE)) {
            return Arrays.hashCode((char[])o);
        }
        if (componentType.equals(Long.TYPE)) {
            return Arrays.hashCode((long[])o);
        }
        if (componentType.equals(Float.TYPE)) {
            return Arrays.hashCode((float[])o);
        }
        if (componentType.equals(Double.TYPE)) {
            return Arrays.hashCode((double[])o);
        }
        if (componentType.equals(Boolean.TYPE)) {
            return Arrays.hashCode((boolean[])o);
        }
        return Arrays.hashCode((Object[])o);
    }
    
    static {
        TO_STRING_STYLE = new ToStringStyle() {
            private static final long serialVersionUID = 1L;
            
            {
                this.setDefaultFullDetail(true);
                this.setArrayContentDetail(true);
                this.setUseClassName(true);
                this.setUseShortClassName(true);
                this.setUseIdentityHashCode(false);
                this.setContentStart("(");
                this.setContentEnd(")");
                this.setFieldSeparator(", ");
                this.setArrayStart("[");
                this.setArrayEnd("]");
            }
            
            @Override
            protected String getShortClassName(final Class<?> cls) {
                Class<? extends Annotation> annotationType = null;
                for (final Class<?> iface : ClassUtils.getAllInterfaces(cls)) {
                    if (Annotation.class.isAssignableFrom((Class)iface)) {
                        final Class<? extends Annotation> found = annotationType = iface;
                        break;
                    }
                }
                return new StringBuilder((annotationType == null) ? "" : annotationType.getName()).insert(0, '@').toString();
            }
            
            @Override
            protected void appendDetail(final StringBuffer buffer, final String fieldName, Object value) {
                if (value instanceof Annotation) {
                    value = AnnotationUtils.toString((Annotation)value);
                }
                super.appendDetail(buffer, fieldName, value);
            }
        };
    }
}

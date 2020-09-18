package org.apache.commons.lang3.reflect;

import java.lang.reflect.Modifier;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import org.apache.commons.lang3.Validate;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ArrayUtils;

public class ConstructorUtils {
    public static <T> T invokeConstructor(final Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        final Class<?>[] parameterTypes = ClassUtils.toClass(args);
        return ConstructorUtils.<T>invokeConstructor(cls, args, parameterTypes);
    }
    
    public static <T> T invokeConstructor(final Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Constructor<T> ctor = ConstructorUtils.<T>getMatchingAccessibleConstructor(cls, parameterTypes);
        if (ctor == null) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName());
        }
        if (ctor.isVarArgs()) {
            final Class<?>[] methodParameterTypes = ctor.getParameterTypes();
            args = MethodUtils.getVarArgs(args, methodParameterTypes);
        }
        return (T)ctor.newInstance(args);
    }
    
    public static <T> T invokeExactConstructor(final Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        final Class<?>[] parameterTypes = ClassUtils.toClass(args);
        return ConstructorUtils.<T>invokeExactConstructor(cls, args, parameterTypes);
    }
    
    public static <T> T invokeExactConstructor(final Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Constructor<T> ctor = ConstructorUtils.<T>getAccessibleConstructor(cls, parameterTypes);
        if (ctor == null) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName());
        }
        return (T)ctor.newInstance(args);
    }
    
    public static <T> Constructor<T> getAccessibleConstructor(final Class<T> cls, final Class<?>... parameterTypes) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "class cannot be null"
        //     3: iconst_0       
        //     4: anewarray       Ljava/lang/Object;
        //     7: invokestatic    org/apache/commons/lang3/Validate.notNull:(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;
        //    10: pop            
        //    11: aload_0         /* cls */
        //    12: aload_1         /* parameterTypes */
        //    13: invokevirtual   java/lang/Class.getConstructor:([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
        //    16: invokestatic    org/apache/commons/lang3/reflect/ConstructorUtils.getAccessibleConstructor:(Ljava/lang/reflect/Constructor;)Ljava/lang/reflect/Constructor;
        //    19: areturn        
        //    20: astore_2        /* e */
        //    21: aconst_null    
        //    22: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;>(Ljava/lang/Class<TT;>;[Ljava/lang/Class<*>;)Ljava/lang/reflect/Constructor<TT;>;
        //    MethodParameters:
        //  Name            Flags  
        //  --------------  -----
        //  cls             
        //  parameterTypes  
        //    StackMapTable: 00 01 54 07 00 10
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  11     19     20     23     Ljava/lang/NoSuchMethodException;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
        //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
        //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.isCastRequired(AstMethodBodyBuilder.java:1357)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCallCore(AstMethodBodyBuilder.java:1318)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCall(AstMethodBodyBuilder.java:1286)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1197)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:715)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:494)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
    
    public static <T> Constructor<T> getAccessibleConstructor(final Constructor<T> ctor) {
        Validate.<Constructor<T>>notNull(ctor, "constructor cannot be null", new Object[0]);
        return (MemberUtils.isAccessible((Member)ctor) && isAccessible(ctor.getDeclaringClass())) ? ctor : null;
    }
    
    public static <T> Constructor<T> getMatchingAccessibleConstructor(final Class<T> cls, final Class<?>... parameterTypes) {
        Validate.<Class<T>>notNull(cls, "class cannot be null", new Object[0]);
        try {
            final Constructor<T> ctor = (Constructor<T>)cls.getConstructor((Class[])parameterTypes);
            MemberUtils.setAccessibleWorkaround((AccessibleObject)ctor);
            return ctor;
        }
        catch (NoSuchMethodException ex) {
            Constructor<T> result = null;
            final Constructor[] constructors;
            final Constructor<?>[] ctors = (constructors = cls.getConstructors());
            for (Constructor<?> ctor2 : constructors) {
                if (MemberUtils.isMatchingConstructor(ctor2, parameterTypes)) {
                    ctor2 = ConstructorUtils.getAccessibleConstructor(ctor2);
                    if (ctor2 != null) {
                        MemberUtils.setAccessibleWorkaround((AccessibleObject)ctor2);
                        if (result == null || MemberUtils.compareConstructorFit(ctor2, result, parameterTypes) < 0) {
                            final Constructor<T> constructor = result = (Constructor<T>)ctor2;
                        }
                    }
                }
            }
            return result;
        }
    }
    
    private static boolean isAccessible(final Class<?> type) {
        for (Class<?> cls = type; cls != null; cls = cls.getEnclosingClass()) {
            if (!Modifier.isPublic(cls.getModifiers())) {
                return false;
            }
        }
        return true;
    }
}

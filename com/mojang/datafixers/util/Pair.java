java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 10
	at java.base/java.util.Vector.get(Vector.java:749)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
	at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
	at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
	at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1577)
	at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
	at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
	at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
	at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
	at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
	at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1412)
	at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1399)
	at com.strobel.decompiler.languages.java.ast.transforms.AddReferenceQualifiersTransform.qualifyReference(AddReferenceQualifiersTransform.java:109)
	at com.strobel.decompiler.languages.java.ast.transforms.AddReferenceQualifiersTransform.addQualifiersWhereNecessary(AddReferenceQualifiersTransform.java:64)
	at com.strobel.decompiler.languages.java.ast.transforms.AddReferenceQualifiersTransform.run(AddReferenceQualifiersTransform.java:49)
	at com.strobel.decompiler.languages.java.ast.transforms.TransformationPipeline.runTransformationsUntil(TransformationPipeline.java:93)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.runTransformations(AstBuilder.java:119)
	at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:76)
	at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
	at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
	at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
	at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
	at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
	at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
	at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
	at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
	at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
	at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)

java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
	at java.base/java.util.Vector.get(Vector.java:749)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
	at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
	at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
	at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
	at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
	at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
	at com.strobel.assembler.metadata.TypeReference.equals(TypeReference.java:118)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:504)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
	at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
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

java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
	at java.base/java.util.Vector.get(Vector.java:749)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.ArrayType.resolve(ArrayType.java:117)
	at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
	at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
	at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
	at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
	at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
	at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
	at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
	at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
	at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
	at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:547)
	at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
	at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
	at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
	at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
	at com.strobel.assembler.metadata.MethodBinder.findMostSpecificType(MethodBinder.java:451)
	at com.strobel.assembler.metadata.MethodBinder.findMostSpecific(MethodBinder.java:378)
	at com.strobel.assembler.metadata.MethodBinder.findMostSpecificMethod(MethodBinder.java:235)
	at com.strobel.assembler.metadata.MethodBinder.selectMethod(MethodBinder.java:175)
	at com.strobel.decompiler.languages.java.utilities.RedundantCastUtility$IsRedundantVisitor.processCall(RedundantCastUtility.java:742)
	at com.strobel.decompiler.languages.java.utilities.RedundantCastUtility$IsRedundantVisitor.visitInvocationExpression(RedundantCastUtility.java:256)
	at com.strobel.decompiler.languages.java.utilities.RedundantCastUtility$IsRedundantVisitor.visitInvocationExpression(RedundantCastUtility.java:167)
	at com.strobel.decompiler.languages.java.ast.InvocationExpression.acceptVisitor(InvocationExpression.java:78)
	at com.strobel.decompiler.languages.java.utilities.RedundantCastUtility.isCastRedundant(RedundantCastUtility.java:67)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitCastExpression(InsertNecessaryConversionsTransform.java:80)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitCastExpression(InsertNecessaryConversionsTransform.java:37)
	at com.strobel.decompiler.languages.java.ast.CastExpression.acceptVisitor(CastExpression.java:55)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitInvocationExpression(DepthFirstAstVisitor.java:59)
	at com.strobel.decompiler.languages.java.ast.InvocationExpression.acceptVisitor(InvocationExpression.java:78)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitReturnStatement(DepthFirstAstVisitor.java:149)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitReturnStatement(InsertNecessaryConversionsTransform.java:179)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitReturnStatement(InsertNecessaryConversionsTransform.java:37)
	at com.strobel.decompiler.languages.java.ast.ReturnStatement.acceptVisitor(ReturnStatement.java:57)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitBlockStatement(DepthFirstAstVisitor.java:104)
	at com.strobel.decompiler.languages.java.ast.BlockStatement.acceptVisitor(BlockStatement.java:72)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitMethodDeclaration(DepthFirstAstVisitor.java:214)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.visitMethodDeclaration(ContextTrackingVisitor.java:64)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.visitMethodDeclaration(ContextTrackingVisitor.java:28)
	at com.strobel.decompiler.languages.java.ast.MethodDeclaration.acceptVisitor(MethodDeclaration.java:85)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitTypeDeclaration(DepthFirstAstVisitor.java:244)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.visitTypeDeclaration(ContextTrackingVisitor.java:52)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.visitTypeDeclaration(ContextTrackingVisitor.java:28)
	at com.strobel.decompiler.languages.java.ast.TypeDeclaration.acceptVisitor(TypeDeclaration.java:90)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitCompilationUnit(DepthFirstAstVisitor.java:249)
	at com.strobel.decompiler.languages.java.ast.CompilationUnit.acceptVisitor(CompilationUnit.java:81)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.run(ContextTrackingVisitor.java:84)
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

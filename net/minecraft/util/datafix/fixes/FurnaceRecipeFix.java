java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 5
	at java.base/java.util.Vector.get(Vector.java:749)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
	at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
	at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
	at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1577)
	at com.strobel.decompiler.languages.java.utilities.RedundantCastUtility$IsRedundantVisitor.isTypeCastSemantic(RedundantCastUtility.java:1064)
	at com.strobel.decompiler.languages.java.utilities.RedundantCastUtility$CastCollector.addToResults(RedundantCastUtility.java:161)
	at com.strobel.decompiler.languages.java.utilities.RedundantCastUtility$IsRedundantVisitor.processCall(RedundantCastUtility.java:778)
	at com.strobel.decompiler.languages.java.utilities.RedundantCastUtility$IsRedundantVisitor.visitInvocationExpression(RedundantCastUtility.java:256)
	at com.strobel.decompiler.languages.java.utilities.RedundantCastUtility$IsRedundantVisitor.visitInvocationExpression(RedundantCastUtility.java:167)
	at com.strobel.decompiler.languages.java.ast.InvocationExpression.acceptVisitor(InvocationExpression.java:78)
	at com.strobel.decompiler.languages.java.utilities.RedundantCastUtility.getRedundantCastsInside(RedundantCastUtility.java:49)
	at com.strobel.decompiler.languages.java.ast.transforms.RemoveRedundantCastsTransform.visitCastExpression(RemoveRedundantCastsTransform.java:50)
	at com.strobel.decompiler.languages.java.ast.transforms.RemoveRedundantCastsTransform.visitCastExpression(RemoveRedundantCastsTransform.java:30)
	at com.strobel.decompiler.languages.java.ast.CastExpression.acceptVisitor(CastExpression.java:55)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitInvocationExpression(DepthFirstAstVisitor.java:59)
	at com.strobel.decompiler.languages.java.ast.InvocationExpression.acceptVisitor(InvocationExpression.java:78)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitCastExpression(DepthFirstAstVisitor.java:279)
	at com.strobel.decompiler.languages.java.ast.transforms.RemoveRedundantCastsTransform.visitCastExpression(RemoveRedundantCastsTransform.java:48)
	at com.strobel.decompiler.languages.java.ast.transforms.RemoveRedundantCastsTransform.visitCastExpression(RemoveRedundantCastsTransform.java:30)
	at com.strobel.decompiler.languages.java.ast.CastExpression.acceptVisitor(CastExpression.java:55)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitInvocationExpression(DepthFirstAstVisitor.java:59)
	at com.strobel.decompiler.languages.java.ast.InvocationExpression.acceptVisitor(InvocationExpression.java:78)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitAssignmentExpression(DepthFirstAstVisitor.java:329)
	at com.strobel.decompiler.languages.java.ast.AssignmentExpression.acceptVisitor(AssignmentExpression.java:88)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitExpressionStatement(DepthFirstAstVisitor.java:109)
	at com.strobel.decompiler.languages.java.ast.ExpressionStatement.acceptVisitor(ExpressionStatement.java:47)
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
	at com.strobel.decompiler.languages.java.ast.transforms.RemoveRedundantCastsTransform.run(RemoveRedundantCastsTransform.java:43)
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

java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
	at java.base/java.util.Vector.get(Vector.java:749)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
	at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
	at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
	at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
	at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
	at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
	at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
	at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
	at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
	at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:508)
	at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:561)
	at com.strobel.assembler.metadata.MetadataHelper.getConversionType(MetadataHelper.java:317)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitCastExpression(InsertNecessaryConversionsTransform.java:74)
	at com.strobel.decompiler.languages.java.ast.transforms.InsertNecessaryConversionsTransform.visitCastExpression(InsertNecessaryConversionsTransform.java:37)
	at com.strobel.decompiler.languages.java.ast.CastExpression.acceptVisitor(CastExpression.java:55)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitInvocationExpression(DepthFirstAstVisitor.java:59)
	at com.strobel.decompiler.languages.java.ast.InvocationExpression.acceptVisitor(InvocationExpression.java:78)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitExpressionStatement(DepthFirstAstVisitor.java:109)
	at com.strobel.decompiler.languages.java.ast.ExpressionStatement.acceptVisitor(ExpressionStatement.java:47)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitBlockStatement(DepthFirstAstVisitor.java:104)
	at com.strobel.decompiler.languages.java.ast.BlockStatement.acceptVisitor(BlockStatement.java:72)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitCatchClause(DepthFirstAstVisitor.java:174)
	at com.strobel.decompiler.languages.java.ast.CatchClause.acceptVisitor(CatchClause.java:78)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitTryCatchStatement(DepthFirstAstVisitor.java:394)
	at com.strobel.decompiler.languages.java.ast.TryCatchStatement.acceptVisitor(TryCatchStatement.java:73)
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

java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
	at java.base/java.util.Vector.get(Vector.java:749)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
	at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
	at com.strobel.decompiler.languages.java.ast.transforms.IntroduceOuterClassReferencesTransform.tryIntroduceOuterClassReference(IntroduceOuterClassReferencesTransform.java:124)
	at com.strobel.decompiler.languages.java.ast.transforms.IntroduceOuterClassReferencesTransform.visitMemberReferenceExpression(IntroduceOuterClassReferencesTransform.java:106)
	at com.strobel.decompiler.languages.java.ast.transforms.IntroduceOuterClassReferencesTransform.visitMemberReferenceExpression(IntroduceOuterClassReferencesTransform.java:32)
	at com.strobel.decompiler.languages.java.ast.MemberReferenceExpression.acceptVisitor(MemberReferenceExpression.java:120)
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
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitTypeDeclaration(DepthFirstAstVisitor.java:244)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.visitTypeDeclaration(ContextTrackingVisitor.java:52)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.visitTypeDeclaration(ContextTrackingVisitor.java:28)
	at com.strobel.decompiler.languages.java.ast.TypeDeclaration.acceptVisitor(TypeDeclaration.java:90)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitChildren(DepthFirstAstVisitor.java:41)
	at com.strobel.decompiler.languages.java.ast.DepthFirstAstVisitor.visitCompilationUnit(DepthFirstAstVisitor.java:249)
	at com.strobel.decompiler.languages.java.ast.CompilationUnit.acceptVisitor(CompilationUnit.java:81)
	at com.strobel.decompiler.languages.java.ast.ContextTrackingVisitor.run(ContextTrackingVisitor.java:84)
	at com.strobel.decompiler.languages.java.ast.transforms.IntroduceOuterClassReferencesTransform.run(IntroduceOuterClassReferencesTransform.java:52)
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

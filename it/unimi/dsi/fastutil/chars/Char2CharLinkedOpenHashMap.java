java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 4
	at java.base/java.util.Vector.get(Vector.java:749)
	at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
	at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:294)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:173)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:169)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.createField(AstBuilder.java:622)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:544)
	at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
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
	at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.helpCC(ForkJoinPool.java:1116)
	at java.base/java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:1966)
	at java.base/java.util.concurrent.ForkJoinTask.tryExternalHelp(ForkJoinTask.java:378)
	at java.base/java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:323)
	at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:412)
	at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
	at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
	at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
	at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
	at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
	at java.base/java.lang.Thread.run(Thread.java:832)

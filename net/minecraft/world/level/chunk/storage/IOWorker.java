package net.minecraft.world.level.chunk.storage;

import org.apache.logging.log4j.LogManager;
import java.util.concurrent.CompletionStage;
import net.minecraft.util.Unit;
import java.util.Iterator;
import net.minecraft.util.thread.ProcessorHandle;
import javax.annotation.Nullable;
import java.util.concurrent.CompletionException;
import java.io.IOException;
import java.util.function.Function;
import com.mojang.datafixers.util.Either;
import java.util.function.Supplier;
import java.util.concurrent.CompletableFuture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import java.io.File;
import net.minecraft.world.level.ChunkPos;
import java.util.Map;
import net.minecraft.util.thread.StrictQueue;
import net.minecraft.util.thread.ProcessorMailbox;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.Logger;

public class IOWorker implements AutoCloseable {
    private static final Logger LOGGER;
    private final AtomicBoolean shutdownRequested;
    private final ProcessorMailbox<StrictQueue.IntRunnable> mailbox;
    private final RegionFileStorage storage;
    private final Map<ChunkPos, PendingStore> pendingWrites;
    
    protected IOWorker(final File file, final boolean boolean2, final String string) {
        this.shutdownRequested = new AtomicBoolean();
        this.pendingWrites = Maps.newLinkedHashMap();
        this.storage = new RegionFileStorage(file, boolean2);
        this.mailbox = new ProcessorMailbox<StrictQueue.IntRunnable>(new StrictQueue.FixedPriorityQueue(Priority.values().length), Util.ioPool(), "IOWorker-" + string);
    }
    
    public CompletableFuture<Void> store(final ChunkPos bra, final CompoundTag md) {
        return (CompletableFuture<Void>)this.submitTask((java.util.function.Supplier<Either<Object, Exception>>)(() -> {
            final PendingStore a4 = (PendingStore)this.pendingWrites.computeIfAbsent(bra, bra -> new PendingStore(md));
            a4.data = md;
            return Either.<CompletableFuture, Object>left(a4.result);
        })).thenCompose(Function.identity());
    }
    
    @Nullable
    public CompoundTag load(final ChunkPos bra) throws IOException {
        final CompletableFuture<CompoundTag> completableFuture3 = this.<CompoundTag>submitTask((java.util.function.Supplier<Either<CompoundTag, Exception>>)(() -> {
            final PendingStore a3 = (PendingStore)this.pendingWrites.get(bra);
            if (a3 != null) {
                return Either.<CompoundTag, Object>left(a3.data);
            }
            try {
                final CompoundTag md4 = this.storage.read(bra);
                return Either.<CompoundTag, Object>left(md4);
            }
            catch (Exception exception4) {
                IOWorker.LOGGER.warn("Failed to read chunk {}", bra, exception4);
                return Either.<Object, Exception>right(exception4);
            }
        }));
        try {
            return (CompoundTag)completableFuture3.join();
        }
        catch (CompletionException completionException4) {
            if (completionException4.getCause() instanceof IOException) {
                throw (IOException)completionException4.getCause();
            }
            throw completionException4;
        }
    }
    
    public CompletableFuture<Void> synchronize() {
        final CompletableFuture<Void> completableFuture2 = (CompletableFuture<Void>)this.submitTask((java.util.function.Supplier<Either<Object, Exception>>)(() -> Either.<CompletableFuture, Object>left(CompletableFuture.allOf((CompletableFuture[])this.pendingWrites.values().stream().map(a -> a.result).toArray(CompletableFuture[]::new))))).thenCompose(Function.identity());
        return (CompletableFuture<Void>)completableFuture2.thenCompose(void1 -> this.submitTask((java.util.function.Supplier<Either<Object, Exception>>)(() -> {
            try {
                this.storage.flush();
                return Either.left(null);
            }
            catch (Exception exception2) {
                IOWorker.LOGGER.warn("Failed to synchronized chunks", (Throwable)exception2);
                return Either.<Object, Exception>right(exception2);
            }
        })));
    }
    
    private <T> CompletableFuture<T> submitTask(final Supplier<Either<T, Exception>> supplier) {
        return this.mailbox.<T>askEither((java.util.function.Function<? super ProcessorHandle<Either<T, Exception>>, ?>)(aoa -> new StrictQueue.IntRunnable(Priority.HIGH.ordinal(), () -> {
            if (!this.shutdownRequested.get()) {
                aoa.tell(supplier.get());
            }
            this.tellStorePending();
        })));
    }
    
    private void storePendingChunk() {
        final Iterator<Map.Entry<ChunkPos, PendingStore>> iterator2 = (Iterator<Map.Entry<ChunkPos, PendingStore>>)this.pendingWrites.entrySet().iterator();
        if (!iterator2.hasNext()) {
            return;
        }
        final Map.Entry<ChunkPos, PendingStore> entry3 = (Map.Entry<ChunkPos, PendingStore>)iterator2.next();
        iterator2.remove();
        this.runStore((ChunkPos)entry3.getKey(), (PendingStore)entry3.getValue());
        this.tellStorePending();
    }
    
    private void tellStorePending() {
        this.mailbox.tell(new StrictQueue.IntRunnable(Priority.LOW.ordinal(), this::storePendingChunk));
    }
    
    private void runStore(final ChunkPos bra, final PendingStore a) {
        try {
            this.storage.write(bra, a.data);
            a.result.complete(null);
        }
        catch (Exception exception4) {
            IOWorker.LOGGER.error("Failed to store chunk {}", bra, exception4);
            a.result.completeExceptionally((Throwable)exception4);
        }
    }
    
    public void close() throws IOException {
        if (!this.shutdownRequested.compareAndSet(false, true)) {
            return;
        }
        final CompletableFuture<Unit> completableFuture2 = this.mailbox.<Unit>ask((java.util.function.Function<? super ProcessorHandle<Unit>, ?>)(aoa -> new StrictQueue.IntRunnable(Priority.HIGH.ordinal(), () -> aoa.tell(Unit.INSTANCE))));
        try {
            completableFuture2.join();
        }
        catch (CompletionException completionException3) {
            if (completionException3.getCause() instanceof IOException) {
                throw (IOException)completionException3.getCause();
            }
            throw completionException3;
        }
        this.mailbox.close();
        this.pendingWrites.forEach(this::runStore);
        this.pendingWrites.clear();
        try {
            this.storage.close();
        }
        catch (Exception exception3) {
            IOWorker.LOGGER.error("Failed to close storage", (Throwable)exception3);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    enum Priority {
        HIGH, 
        LOW;
    }
    
    static class PendingStore {
        private CompoundTag data;
        private final CompletableFuture<Void> result;
        
        public PendingStore(final CompoundTag md) {
            this.result = (CompletableFuture<Void>)new CompletableFuture();
            this.data = md;
        }
    }
}

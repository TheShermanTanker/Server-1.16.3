package net.minecraft.server.packs.resources;

import net.minecraft.util.Unit;
import java.util.concurrent.CompletableFuture;

public interface ReloadInstance {
    CompletableFuture<Unit> done();
}

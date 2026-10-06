package dev.loadprogress.details.mixin;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SimpleResourceReload;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes the internals of {@link SimpleResourceReload} so the detailed
 * progress line can report task counts and pending reloaders.
 */
@Mixin(SimpleResourceReload.class)
public interface SimpleResourceReloadAccessor {
	@Accessor("reloaderCount")
	int loadprogress$getReloaderCount();

	@Accessor("waitingReloaders")
	Set<ResourceReloader> loadprogress$getWaitingReloaders();

	@Accessor("preparedCount")
	AtomicInteger loadprogress$getPreparedCount();

	@Accessor("toPrepareCount")
	AtomicInteger loadprogress$getToPrepareCount();

	@Accessor("appliedCount")
	AtomicInteger loadprogress$getAppliedCount();

	@Accessor("toApplyCount")
	AtomicInteger loadprogress$getToApplyCount();

	@Accessor("prepareStageFuture")
	CompletableFuture<Unit> loadprogress$getPrepareStageFuture();
}

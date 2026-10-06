package dev.loadprogress.details.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.resource.ResourceReload;
import dev.loadprogress.details.ReloadDetailsRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws detailed reload progress below the vanilla progress bar.
 *
 * <p>Injecting at the tail of {@code renderProgressBar} (instead of wrapping
 * or redirecting vanilla calls) keeps this mod compatible with RRLS, which
 * wraps calls inside the same method to recolor its progress bar.
 */
@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin {
	@Shadow
	@Final
	private ResourceReload reload;

	@Shadow
	private float progress;

	@Inject(method = "renderProgressBar", at = @At("TAIL"))
	private void loadprogress$renderDetails(DrawContext context, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) {
		ReloadDetailsRenderer.render(context, this.reload, this.progress, maxY, opacity);
	}
}

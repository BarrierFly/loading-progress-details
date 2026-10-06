package dev.loadprogress.details;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceReload;
import net.minecraft.resource.SimpleResourceReload;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import dev.loadprogress.details.mixin.SimpleResourceReloadAccessor;

/**
 * Renders two detail lines right below the vanilla reload progress bar:
 * stage/percentage/elapsed time, and task counts plus pending reloaders.
 */
public final class ReloadDetailsRenderer {
	private static final int MAX_PENDING_NAMES = 3;
	private static final int LINE_SPACING = 1;
	private static final int BAR_TEXT_GAP = 5;

	private static ResourceReload trackedReload;
	private static long reloadStartTime = -1L;

	private ReloadDetailsRenderer() {
	}

	public static void render(DrawContext context, ResourceReload reload, float smoothProgress, int barBottom, float opacity) {
		int alpha = MathHelper.clamp((int) (opacity * 255.0F), 0, 255);
		if (alpha <= 0) {
			return;
		}

		// Vanilla's font renderer treats an alpha channel below 4 as opaque,
		// which would make the text pop in fully bright during fades.
		int color = ColorHelper.getArgb(Math.max(alpha, 4), 255, 255, 255);

		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		int centerX = context.getScaledWindowWidth() / 2;

		long now = Util.getMeasuringTimeMs();
		if (trackedReload != reload) {
			trackedReload = reload;
			reloadStartTime = now;
		}
		float elapsedSeconds = (now - reloadStartTime) / 1000.0F;

		Text stageLine = Text.translatable(
				"loading-progress-details.line.stage",
				Text.translatable(stageTranslationKey(reload)),
				Math.round(smoothProgress * 100.0F),
				String.format(Locale.ROOT, "%.1fs", elapsedSeconds)
		);

		int y = barBottom + BAR_TEXT_GAP;
		context.drawCenteredTextWithShadow(textRenderer, stageLine, centerX, y, color);

		Text taskLine = buildTaskLine(reload);
		if (taskLine != null) {
			context.drawCenteredTextWithShadow(textRenderer, taskLine, centerX, y + textRenderer.fontHeight + LINE_SPACING, color);
		}
	}

	private static String stageTranslationKey(ResourceReload reload) {
		if (reload.isComplete()) {
			return "loading-progress-details.stage.done";
		}
		if (reload instanceof SimpleResourceReloadAccessor accessor && accessor.loadprogress$getPrepareStageFuture().isDone()) {
			return "loading-progress-details.stage.applying";
		}
		return "loading-progress-details.stage.preparing";
	}

	private static Text buildTaskLine(ResourceReload reload) {
		if (!(reload instanceof SimpleResourceReloadAccessor accessor)) {
			return null;
		}

		MutableText taskLine = Text.translatable(
				"loading-progress-details.line.tasks",
				accessor.loadprogress$getPreparedCount().get(),
				accessor.loadprogress$getToPrepareCount().get(),
				accessor.loadprogress$getAppliedCount().get(),
				accessor.loadprogress$getToApplyCount().get()
		);

		Text pending = buildPendingLine(accessor);
		if (pending != null) {
			taskLine = taskLine.append(" · ").append(pending);
		}
		return taskLine;
	}

	private static Text buildPendingLine(SimpleResourceReloadAccessor accessor) {
		List<String> names = new ArrayList<>();
		int extra = 0;
		for (ResourceReloader reloader : accessor.loadprogress$getWaitingReloaders()) {
			String name = shortenName(reloader.getName());
			if (names.size() < MAX_PENDING_NAMES) {
				names.add(name);
			} else {
				extra++;
			}
		}
		if (names.isEmpty()) {
			return null;
		}

		String joined = String.join(", ", names);
		if (extra > 0) {
			joined += " +" + extra;
		}
		return Text.translatable("loading-progress-details.line.waiting", joined);
	}

	private static String shortenName(String name) {
		// Fabric API names reloaders like "id (ClassName)"; keep the id part only.
		int paren = name.indexOf(" (");
		return paren > 0 ? name.substring(0, paren) : name;
	}
}

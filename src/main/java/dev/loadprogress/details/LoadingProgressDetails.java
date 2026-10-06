package dev.loadprogress.details;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadingProgressDetails implements ClientModInitializer {
	public static final String MOD_ID = "loading-progress-details";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Loading Progress Details initialized");
	}
}

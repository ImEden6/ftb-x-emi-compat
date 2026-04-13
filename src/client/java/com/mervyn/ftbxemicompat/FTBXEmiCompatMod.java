package com.mervyn.ftbxemicompat;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTBXEmiCompatMod implements ClientModInitializer {
    public static final String MOD_ID = "ftbxemicompat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("FTB x EMI Compatibility (Client) initialized");
    }
}

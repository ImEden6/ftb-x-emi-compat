package com.mervyn.ftbxemicompat.mixin;

import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import com.mervyn.ftbxemicompat.integration.EMIRecipeModHelper;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FTBQuestsClient.class)
public class FTBQuestsClientMixin {
    @Inject(method = "init", at = @At("RETURN"))
    private static void ftbxemicompat$init(CallbackInfo ci) {
        if (FabricLoader.getInstance().isModLoaded("emi")) {
            FTBQuests.setRecipeModHelper(new EMIRecipeModHelper());
        }
    }
}

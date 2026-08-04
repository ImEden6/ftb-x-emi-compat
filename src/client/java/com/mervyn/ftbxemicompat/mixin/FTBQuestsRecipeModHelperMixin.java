package com.mervyn.ftbxemicompat.mixin;

import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.integration.RecipeModHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FTBQuests.class)
public class FTBQuestsRecipeModHelperMixin {

    @Shadow(remap = false)
    private static RecipeModHelper recipeModHelper;

    @Inject(method = "setRecipeModHelper", at = @At("HEAD"), cancellable = true)
    private static void ftbxemicompat$ignoreDuplicateRegistration(RecipeModHelper newHelper, CallbackInfo ci) {
        if (recipeModHelper != null) {
            FTBQuests.LOGGER.warn("[FTB x EMI Compat] Ignoring recipe mod helper '{}' registered after '{}' was already set (likely FTB XMod Compat colliding with EMI support)",
                    newHelper.getHelperName(), recipeModHelper.getHelperName());
            ci.cancel();
        }
    }
}

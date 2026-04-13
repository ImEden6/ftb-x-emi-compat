package com.mervyn.ftbxemicompat.integration;

import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.EmiStack;
import dev.ftb.mods.ftbquests.integration.RecipeModHelper;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import net.minecraft.item.ItemStack;

public class EMIRecipeModHelper implements RecipeModHelper {
    @Override
    public void refreshAll(Components components) {
    }

    @Override
    public void refreshRecipes(QuestObjectBase questObjectBase) {
    }

    @Override
    public void showRecipes(ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            EmiApi.displayRecipes(EmiStack.of(itemStack));
        }
    }

    @Override
    public boolean isRecipeModAvailable() {
        return true;
    }

    @Override
    public String getHelperName() {
        return "EMI";
    }
}

package com.mervyn.ftbxemicompat.integration;

import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import java.util.Optional;

public class EMIIntegration {
    public static void displayRecipes(PositionedIngredient ingredient) {
        getEmiIngredient(ingredient).ifPresent(EmiApi::displayRecipes);
    }

    public static void displayUses(PositionedIngredient ingredient) {
        getEmiIngredient(ingredient).ifPresent(EmiApi::displayUses);
    }

    private static Optional<EmiIngredient> getEmiIngredient(PositionedIngredient ingredient) {
        Object obj = ingredient.ingredient();
        if (obj instanceof ItemStack stack) {
            if (!stack.isEmpty()) {
                return Optional.of(EmiStack.of(stack));
            }
        } else if (obj instanceof Fluid fluid) {
            return Optional.of(EmiStack.of(fluid, 1000L));
        } else if (obj instanceof FluidVariant variant) {
             return Optional.of(EmiStack.of(variant.getFluid(), 1000L));
        }
        return Optional.empty();
    }
}

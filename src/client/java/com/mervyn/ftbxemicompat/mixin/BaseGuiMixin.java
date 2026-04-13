package com.mervyn.ftbxemicompat.mixin;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.client.gui.quests.ValidItemsScreen;
import com.mervyn.ftbxemicompat.integration.EMIIntegration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = BaseScreen.class, remap = false)
public abstract class BaseGuiMixin {

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void ftbxemicompat$keyPressed(Key key, CallbackInfoReturnable<Boolean> cir) {
        // Plain key check: no control, no shift, no alt
        if (!key.modifiers.control() && !key.modifiers.shift() && !key.modifiers.alt() && (key.is(82) || key.is(85))) { // R or U
            Object self = this;
            if (self instanceof QuestScreen || self instanceof ValidItemsScreen) {
                Widget widget = ftbxemicompat$getHoveredWidget((BaseScreen) self);
                if (widget != null) {
                    Optional<PositionedIngredient> ingredient = widget.getIngredientUnderMouse();
                    if (ingredient.isPresent()) {
                        if (key.is(82)) {
                            EMIIntegration.displayRecipes(ingredient.get());
                        } else {
                            EMIIntegration.displayUses(ingredient.get());
                        }
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }

    @Unique
    private Widget ftbxemicompat$getHoveredWidget(Widget root) {
        if (!root.isMouseOver()) {
            return null;
        }
        if (root instanceof Panel panel) {
            // Check widgets in reverse order (top-most first)
            var widgets = panel.getWidgets();
            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget found = ftbxemicompat$getHoveredWidget(widgets.get(i));
                if (found != null) {
                    return found;
                }
            }
        }
        return root;
    }
}

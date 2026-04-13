package com.mervyn.ftbxemicompat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import team.creative.ambientsounds.engine.AmbientEngine;
import team.creative.ambientsounds.engine.AmbientTickHandler;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;
import team.creative.creativecore.common.config.holder.ConfigHolderDynamic;
import team.creative.creativecore.common.config.sync.ConfigSynchronization;
import team.creative.ambientsounds.AmbientSounds;
import team.creative.ambientsounds.dimension.AmbientDimension;
import team.creative.creativecore.reflection.ReflectionHelper;
import team.creative.ambientsounds.region.AmbientRegion;
import team.creative.ambientsounds.sound.AmbientSound;
import team.creative.ambientsounds.sound.AmbientSoundCategory;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.Side;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.DynamicRegistryManager;
import java.lang.reflect.Field;
import java.util.Map;

@Mixin(AmbientTickHandler.class)
public abstract class AmbientTickHandlerMixin {

    @Shadow
    public AmbientEngine engine;
    @Shadow
    private static MinecraftClient mc;

    @Shadow
    protected abstract void createSoundCategoryConfiguration(ConfigHolderDynamic parent, AmbientSoundCategory cat,
            Field categoryField);

    /**
     * @author Mervyn
     * @reason Fix race condition in initConfiguration causing crash (Duplicate key:
     *         ambientsounds)
     */
    @Overwrite(remap = false)
    public void initConfiguration() {
        synchronized (AmbientTickHandler.class) {
            CreativeConfigRegistry.ROOT.removeField("ambientsounds");
            ConfigHolderDynamic holder = CreativeConfigRegistry.ROOT.registerFolder("ambientsounds",
                    ConfigSynchronization.CLIENT);
            holder.registerValue("general", (Object) AmbientSounds.CONFIG);

            if (this.engine == null) {
                return;
            }

            AmbientEngineAccessor engineAccessor = (AmbientEngineAccessor) this.engine;

            // Dimensions
            ConfigHolderDynamic dimensions = holder.registerFolder("dimensions");
            Field dimensionField = ReflectionHelper.findField(AmbientDimension.class, (String) "volumeSetting");
            for (AmbientDimension dimension : engineAccessor.getDimensions().values()) {
                dimensions.registerField(dimension.name, dimensionField, (Object) dimension);
            }

            // Regions
            ConfigHolderDynamic regions = holder.registerFolder("regions");
            Field regionField = ReflectionHelper.findField(AmbientRegion.class, (String) "volumeSetting");
            Field soundField = ReflectionHelper.findField(AmbientSound.class, (String) "volumeSetting");
            for (Map.Entry<String, AmbientRegion> pair : engineAccessor.getAllRegions().entrySet()) {
                ConfigHolderDynamic region = regions.registerFolder(pair.getKey().replace(".", "_"));
                region.registerField("overall", regionField, (Object) pair.getValue());
                if (pair.getValue().loadedSounds != null) {
                    for (AmbientSound sound : pair.getValue().loadedSounds.values()) {
                        region.registerField(sound.name, soundField, (Object) sound);
                    }
                }
            }

            // Categories
            ConfigHolderDynamic categories = holder.registerFolder("categories");
            Field categoryField = ReflectionHelper.findField(AmbientSoundCategory.class, (String) "volumeSetting");
            for (AmbientSoundCategory cat : engineAccessor.getSortedSoundCategories()) {
                this.createSoundCategoryConfiguration(categories, cat, categoryField);
            }

            // Engine static fields
            holder.registerField("fade-volume", ReflectionHelper.findField(AmbientEngine.class, (String) "fadeVolume"),
                    (Object) this.engine);
            holder.registerField("fade-pitch", ReflectionHelper.findField(AmbientEngine.class, (String) "fadePitch"),
                    (Object) this.engine);
            holder.registerField("silent-dimensions",
                    ReflectionHelper.findField(AmbientEngine.class, (String) "silentDimensions"), (Object) this.engine);

            DynamicRegistryManager registryManager = (mc.world != null) ? mc.world.getRegistryManager() : null;
            CreativeCore.CONFIG_HANDLER.load(registryManager, "ambientsounds", Side.CLIENT);
        }
    }
}

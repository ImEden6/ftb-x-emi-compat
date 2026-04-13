package com.mervyn.ftbxemicompat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import team.creative.ambientsounds.engine.AmbientEngine;
import team.creative.ambientsounds.dimension.AmbientDimension;
import team.creative.ambientsounds.region.AmbientRegion;
import team.creative.ambientsounds.sound.AmbientSoundCategory;
import java.util.LinkedHashMap;
import java.util.List;

@Mixin(AmbientEngine.class)
public interface AmbientEngineAccessor {

    @Accessor(remap = false)
    LinkedHashMap<String, AmbientDimension> getDimensions();

    @Accessor(remap = false)
    LinkedHashMap<String, AmbientRegion> getAllRegions();

    @Accessor(remap = false)
    List<AmbientSoundCategory> getSortedSoundCategories();
}

package io.github.vampirestudios.raa_dimension.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.model.ModelLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Redirect(method = "loadModel", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/util/Pair;of(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;"))
    private <F, S> Pair<F, S> blockstatePair(F first, S second) {
        return null;
    }

}

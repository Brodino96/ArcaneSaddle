package net.brodino.arcanesaddle.mixin;

import net.brodino.arcanesaddle.modules.utils.CustomComponents;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public class MountTamed {

    @Inject(method = "bondWithPlayer", at = @At("TAIL"))
    private void bondWithPlayer(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        AbstractHorseEntity mount = (AbstractHorseEntity) (Object) this;
        mount.setComponent(CustomComponents.TAMED_BY, player.getName().toString());
    }
}

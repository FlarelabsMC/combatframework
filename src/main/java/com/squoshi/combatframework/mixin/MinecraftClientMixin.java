package com.squoshi.combatframework.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    public void cancelAttack(CallbackInfoReturnable<Boolean> c) {
        c.setReturnValue(false);
        c.cancel();
    }
}
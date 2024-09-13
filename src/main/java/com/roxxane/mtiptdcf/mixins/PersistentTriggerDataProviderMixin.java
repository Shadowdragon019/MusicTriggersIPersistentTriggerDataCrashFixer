package com.roxxane.mtiptdcf.mixins;

import mods.thecomputerizer.musictriggers.server.data.IPersistentTriggerData;
import mods.thecomputerizer.musictriggers.server.data.PersistentTriggerData;
import mods.thecomputerizer.musictriggers.server.data.PersistentTriggerDataProvider;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentTriggerDataProvider.class)
public class PersistentTriggerDataProviderMixin {
    @Inject(
        method = "getPlayerCapability",
        at = @At("RETURN"),
        cancellable = true,
        remap = false
    )
    private static void getPlayerCapabilityInject(ServerPlayer player, CallbackInfoReturnable<IPersistentTriggerData> cir) {
        if (cir.getReturnValue() == null) {
            cir.setReturnValue(new PersistentTriggerData());
        }
    }
}

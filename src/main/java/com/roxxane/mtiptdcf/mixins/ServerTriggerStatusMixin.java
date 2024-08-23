package com.roxxane.mtiptdcf.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.roxxane.mtiptdcf.MtCrashFixer;
import mods.thecomputerizer.musictriggers.server.channels.ServerTriggerStatus;
import mods.thecomputerizer.musictriggers.server.data.PersistentTriggerDataProvider;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerTriggerStatus.class)
public class ServerTriggerStatusMixin {
    // Covers all but the last
    @ModifyVariable(
        method = "decodeDynamicInfo",
        at = @At("STORE"),
        ordinal = 1,
        remap = false
    )
    private static boolean decodeDynamicInfoInject(boolean bool, @Local ServerPlayer player) {
        return bool && player != null && PersistentTriggerDataProvider.getPlayerCapability(player) != null;
    }

    // Covers all but the first
    @Redirect(
        method = "decodeDynamicInfo",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Objects;nonNull(Ljava/lang/Object;)Z"
        ),
        remap = false
    )
    private static boolean decodeDynamicInfoRedirect(Object obj, @Local ServerPlayer player) {
        MtCrashFixer.logger.info(String.valueOf(obj));
        return player != null && PersistentTriggerDataProvider.getPlayerCapability(player) != null;
    }
}

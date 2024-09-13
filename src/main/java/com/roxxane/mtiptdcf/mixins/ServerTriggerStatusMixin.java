package com.roxxane.mtiptdcf.mixins;

import mods.thecomputerizer.musictriggers.server.channels.ServerTriggerStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.Map;

@Mixin(ServerTriggerStatus.class)
abstract class ServerTriggerStatusMixin {
    @Shadow(remap = false) @Final private static Map<String, ServerTriggerStatus> SERVER_DATA;

    @Shadow(remap = false) protected abstract void runChecks();

    @Redirect(
        method = "runServerChecks",
        at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"),
        remap = false)
    private static boolean runServerChecksInject(Iterator instance) {
        SERVER_DATA.entrySet().removeIf(entry -> {
            if (entry.getValue().isValid()) {
                ((ServerTriggerStatusMixin) (Object) entry.getValue()).runChecks();
                return false;
            } else
                return true;
        });
        return false;
    }
}

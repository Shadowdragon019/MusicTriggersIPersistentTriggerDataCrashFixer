package com.roxxane.mtiptdcf.mixins;

import mods.thecomputerizer.musictriggers.server.channels.ServerTriggerStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Mixin(ServerTriggerStatus.class)
abstract class ServerTriggerStatusMixin {
    @Shadow(remap = false) @Final private static Map<String, ServerTriggerStatus> SERVER_DATA;

    @Shadow(remap = false) protected abstract void runChecks();

    @Redirect(
        method = "runServerChecks", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"),
        remap = false)
    private synchronized static boolean runServerChecksRedirect(Iterator<?> instance) {
        var toRemove = new ArrayList<String>();

        for (var entry : SERVER_DATA.entrySet())
            if (entry.getValue().isValid())
                ((ServerTriggerStatusMixin) (Object) entry.getValue()).runChecks();
            else
                toRemove.add(entry.getKey());

        for (var key : toRemove)
            SERVER_DATA.remove(key);

        return false;
    }
}

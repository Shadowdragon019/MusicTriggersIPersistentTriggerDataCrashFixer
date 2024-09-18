package com.roxxane.mtiptdcf.mixins;

import mods.thecomputerizer.musictriggers.server.channels.ServerTriggerStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings({"SynchronizeOnNonFinalField", "unchecked", "SuspiciousMethodCalls"})
@Mixin(ServerTriggerStatus.class)
abstract class ServerTriggerStatusMixin {
    @Shadow(remap = false) @Final private static Map<String, ServerTriggerStatus> SERVER_DATA;

    @Shadow(remap = false) protected abstract void runChecks();

    @Redirect(
        method = "runServerChecks", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"),
        remap = false)
    private synchronized static boolean runServerChecksRedirect(Iterator<?> instance) {
        synchronized (SERVER_DATA) {
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

    @Redirect(method = "initializePlayerChannels", at = @At(value = "INVOKE",
        target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
        remap = false)
    private static <K, V> V initializePlayerChannelsRedirect(Map<K, V> instance, K k, V v) {
        synchronized (SERVER_DATA) {
            return (V) SERVER_DATA.put((String) k, (ServerTriggerStatus) v);
        }
    }

    @Redirect(method = "decodeDynamicInfo", at = @At(value = "INVOKE",
        target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
    private static <K, V> V decodeDynamicInfoRedirect(Map<K, V> instance, Object o) {
        synchronized (SERVER_DATA) {
            return (V) SERVER_DATA.get(o);
        }
    }

    @Redirect(method = "recordAudioData(Ljava/util/UUID;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
    private static <K, V> V recordAudioDataRedirect(Map<K, V> instance, Object o) {
        synchronized (SERVER_DATA) {
            return (V) SERVER_DATA.get(o);
        }
    }

    @Redirect(method = "setPVP(Lnet/minecraft/server/level/ServerPlayer;Ljava/lang/String;)V",
        at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
    private static <K, V> V setPVPRedirect(Map<K, V> instance, Object o) {
        synchronized (SERVER_DATA) {
            return (V) SERVER_DATA.get(o);
        }
    }
}

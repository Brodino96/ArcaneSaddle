package net.brodino.arcanesaddle.modules.mount;

import net.brodino.arcanesaddle.ArcaneSaddle;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MountManager {

    public static final Integer mountDuration = ArcaneSaddle.CONFIG.mountTimers() * 20;
    //                     Player  Mount
    public static final Map<UUID, Mount> playerMounts = new HashMap<>();
    //                      Mount  Time
    public static final Map<Mount, Integer> mountTimers = new HashMap<>();

    // ------------------------------------------------------------------------------------------ \\

    public static boolean hasSummonedMount(UUID playerUUID) {
        return playerMounts.containsKey(playerUUID);
    }

    public static void dismissMount(PlayerEntity player) {
        ArcaneSaddle.LOGGER.info("Dismissing {}'s mount", player.getDisplayName().getString());
        playerMounts.get(player.getUuid()).dismiss();
    }
}

package net.brodino.arcanesaddle;

import net.brodino.arcanesaddle.utils.Mount;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MountManager {

    //                     Player  Mount
    public static final Map<UUID, Mount> playerMounts = new HashMap<>();
    //                      Mount  Time
    public static final Map<Mount, Integer> mountTimers = new HashMap<>();

    // ------------------------------------------------------------------------------------------ \\

    public static boolean hasSummonedMount(UUID playerUUID) {
        return playerMounts.containsKey(playerUUID);
    }

    public static void dismissMount(PlayerEntity player) {
        playerMounts.get(player.getUuid()).dismiss();
    }
}

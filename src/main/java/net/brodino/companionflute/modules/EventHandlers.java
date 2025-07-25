package net.brodino.companionflute.modules;

import net.brodino.companionflute.CompanionFlute;
import net.brodino.companionflute.modules.mount.MountManager;
import net.brodino.companionflute.modules.utils.CustomComponents;
import net.brodino.companionflute.modules.mount.Mount;
import net.brodino.companionflute.modules.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Contains the logic for all the events
 */
public class EventHandlers {

    public static ActionResult onItemUse(PlayerEntity player, World world, Hand hand) {

        if (world.isClient()) {
            return ActionResult.PASS;
        }

        CompanionFlute.LOGGER.info("Item used event called");

        ItemStack stack = player.getStackInHand(hand);

        // Player didn't use the Arcane Saddle
        if (!stack.getItem().equals(ItemManager.COMPANION_FLUTE)) {
            return ActionResult.PASS;
        }

        String playerName = player.getDisplayName().getString();
        CompanionFlute.LOGGER.info("{} used the saddle", playerName);

        if (Utils.isInCooldown(player, stack)) {
            CompanionFlute.LOGGER.info("{}'s item is in cooldown", playerName);
            Utils.notify(player, "in_cooldown");
            return ActionResult.FAIL;
        }

        if (!DataHelper.hasSavedData(stack)) {
            CompanionFlute.LOGGER.info("{} item didn't have any stored data", playerName);
            Utils.notify(player, "no_mount_saved");
            return ActionResult.SUCCESS;
        }

        if (!Utils.isAnAllowedDimension(world)) {
            CompanionFlute.LOGGER.info("{} isn't in an allowed dimension", playerName);
            Utils.notify(player, "invalid_dimension");
            return ActionResult.FAIL;
        }

        if (MountManager.hasSummonedMount(player.getUuid())) {
            CompanionFlute.LOGGER.info("{} already had a summoned mount", playerName);
            MountManager.dismissMount(player);
            return ActionResult.SUCCESS;
        }

        Mount mount = new Mount(stack, player);
        mount.summon();
        CompanionFlute.LOGGER.info("{}'s successfully summoned a mount", playerName);

        return ActionResult.SUCCESS;
    }

    public static ActionResult itemUsedOnEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {

        if (world.isClient() || entityHitResult == null) {
            return ActionResult.PASS;
        }

        ItemStack stack = player.getStackInHand(hand);

        if (!stack.getItem().equals(ItemManager.COMPANION_FLUTE)) {
            return ActionResult.PASS;
        }

        String playerName = player.getDisplayName().getString();
        CompanionFlute.LOGGER.info("{} used a saddle on an mob", playerName);

        if (Utils.isInCooldown(player, stack)) {
            CompanionFlute.LOGGER.info("{}'s saddle is in cooldown", playerName);
            Utils.notify(player, "in_cooldown");
            return ActionResult.FAIL;
        }

        if (DataHelper.hasSavedData(stack)) {
            CompanionFlute.LOGGER.info("{}'s saddle already has data saved", playerName);
            Utils.notify(player, "item_already_bound");
            return ActionResult.FAIL;
        }

        if (!(entity instanceof HorseEntity mount)) {
            CompanionFlute.LOGGER.info("{}'s selected mob isn't valid", playerName);
            Utils.notify(player, "entity_not_valid");
            return ActionResult.FAIL;
        }

        if (!mount.isTame()) {
            CompanionFlute.LOGGER.info("{}'s selected mob isn't tamed", playerName);
            Utils.notify(player, "mount_untamed");
            return ActionResult.FAIL;
        }

        String tamedBy = mount.get(CustomComponents.TAMED_BY);
        if (tamedBy != null && tamedBy.equals(player.getUuidAsString())) {
            CompanionFlute.LOGGER.info("{}'s selected mob wasn't tamed by him", playerName);
            Utils.notify(player, "mount_is_not_yours");
            return ActionResult.FAIL;
        }

        DataHelper.saveMountData((AbstractHorseEntity) entity, stack);
        entity.discard();
        CompanionFlute.LOGGER.info("{}'s successfully bounded a mount to his saddle", playerName);
        Utils.notify(player, "bound_success");

        return ActionResult.SUCCESS;
    }

    public static ActionResult itemUsedOnABlock(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
        return ActionResult.PASS;
    }

    public static void playerDisconnected(ServerPlayerEntity player) {
        MountManager.dismissMount(player);
    }

    public static void onMountDeath(LivingEntity entity) {
        for (Map.Entry<UUID, Mount> entry : MountManager.playerMounts.entrySet()) {
            Mount mount = entry.getValue();

            if (mount.entity.equals(entity)) {
                mount.entity.clearActiveItem();
                mount.dismiss();
            }
        }
    }

    public static void onServerTick(MinecraftServer server) {
        Map<UUID, Mount> mountsCopy = new HashMap<>(MountManager.playerMounts);

        for (Map.Entry<UUID, Mount> entry : mountsCopy.entrySet()) {
            UUID playerUUID = entry.getKey();
            Mount mount = entry.getValue();

            Entity entity = mount.entity;
            if (entity == null || !entity.isAlive()) {
                MountManager.playerMounts.remove(playerUUID);
                MountManager.mountTimers.remove(mount);
                continue;
            }

            if (entity.hasPassengers()) {
                MountManager.mountTimers.remove(mount);
            } else {
                Integer timer = MountManager.mountTimers.getOrDefault(mount, MountManager.mountDuration);
                timer--;

                if (timer <= 0) {

                    ServerPlayerEntity owner = CompanionFlute.SERVER.getPlayerManager().getPlayer(playerUUID);

                    if (owner != null) {
                        MountManager.dismissMount(owner);
                    }

                } else {
                    MountManager.mountTimers.put(mount, timer);
                }
            }

        }
    }
}

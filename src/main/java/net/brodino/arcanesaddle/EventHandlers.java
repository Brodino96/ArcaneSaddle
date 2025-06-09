package net.brodino.arcanesaddle;

import net.brodino.arcanesaddle.utils.DataHelper;
import net.brodino.arcanesaddle.utils.Mount;
import net.brodino.arcanesaddle.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Contains the logic for all the events
 */
public class EventHandlers {

    public static ActionResult onItemUse(PlayerEntity player, World world, Hand hand) {

        ItemStack stack = player.getStackInHand(hand);

        // Player didn't use the Arcane Saddle
        if (!stack.getItem().equals(ItemManager.ARCANE_SADDLE)) {
            return ActionResult.PASS;
        }

        if (MountManager.hasSummonedMount(player.getUuid())) {
            MountManager.dismissMount(player);
            Utils.notify(player, "mount_dismissed");
            return ActionResult.SUCCESS;
        }

        if (Utils.isInCooldown(player, stack)) {
            Utils.notify(player, "in_cooldown");
            return ActionResult.FAIL;
        }

        if (!Utils.isInAllowedDimension(world)) {
            Utils.notify(player, "invalid_dimension");
            return ActionResult.FAIL;
        }

        if (!DataHelper.hasSavedData(stack)) {
            Utils.notify(player, "no_mount_saved");
            return ActionResult.SUCCESS;
        }

        new Mount(stack, player).summon();

        return ActionResult.FAIL;
    }

    public static ActionResult itemUsedOnEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {

        ItemStack stack = player.getStackInHand(hand);

        if (!stack.getItem().equals(ItemManager.ARCANE_SADDLE)) {
            return ActionResult.PASS;
        }

        if (!(entity instanceof HorseEntity mount)) {
            Utils.notify(player, "entity_not_valid");
            return ActionResult.FAIL;
        }

        if (Utils.isInCooldown(player, stack)) {
            Utils.notify(player, "in_cooldown");
            return ActionResult.FAIL;
        }

        if (!mount.isTame()) {
            Utils.notify(player, "mount_untamed");
            return ActionResult.FAIL;
        }

        System.out.println(mount.getOwner());

        if (DataHelper.hasSavedData(stack)) {
            Utils.notify(player, "item_already_bound");
            return ActionResult.FAIL;
        }

        DataHelper.saveMountData((AbstractHorseEntity) entity, stack);
        entity.discard();
        Utils.notify(player, "success");

        return ActionResult.SUCCESS;
    }

    public static void playerDisconnected(ServerPlayerEntity player) {
    }

    public static void onMountDeath(LivingEntity entity) {
    }

    public static void onServerTick(MinecraftServer server) {
    }
}

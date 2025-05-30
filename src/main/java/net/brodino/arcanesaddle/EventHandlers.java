package net.brodino.arcanesaddle;

import net.brodino.arcanesaddle.utils.DataHelper;
import net.brodino.arcanesaddle.utils.Mount;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
            return ActionResult.SUCCESS;
        }

        // Player had 2 Arcane Saddles in his hand
        if (player.getOffHandStack().getItem().equals(ItemManager.ARCANE_SADDLE) && player.getMainHandStack().getItem().equals(ItemManager.ARCANE_SADDLE)) {
            player.sendMessage(Text.translatable("no_dual_wielding"), true);
            return ActionResult.FAIL;
        }

        if (MountManager.hasSummonedMount(player.getUuid())) {
            MountManager.dismissMount(player);
            return ActionResult.SUCCESS;
        }

        new Mount(stack, player).summon();

        return ActionResult.FAIL;
    }

    public static ActionResult itemUsedOnEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {

        ItemStack stack = player.getStackInHand(hand);

        if (!stack.getItem().equals(ItemManager.ARCANE_SADDLE)) {
            return ActionResult.FAIL;
        }

        ItemCooldownManager cooldownManager = player.getItemCooldownManager();

        if (cooldownManager.isCoolingDown(stack)) {
            player.sendMessage(Text.translatable("item_in_cooldown"), true);
            return ActionResult.FAIL;
        }

        cooldownManager.set(stack, ArcaneSaddle.CONFIG.itemCooldown());

        if (DataHelper.hasSavedData(stack)) {
            player.sendMessage(Text.translatable("item_already_bound"), true);
            return ActionResult.FAIL;
        }


        return ActionResult.FAIL;
    }

    public static void playerDisconnected(ServerPlayerEntity player) {
    }

    public static void onMountDeath(LivingEntity entity) {
    }

    public static void onServerTick(MinecraftServer server) {
    }
}

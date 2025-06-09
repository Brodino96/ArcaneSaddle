package net.brodino.arcanesaddle.utils;

import net.brodino.arcanesaddle.ArcaneSaddle;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class Utils {

    /**
     * Sends a some text in the action bar of the player
     * @param player The player that needs to be notified
     * @param entry The translatable entry
     */
    public static void notify(PlayerEntity player, String entry) {
        player.sendMessage(Text.translatable(entry), true);
    }

    /**
     * Checks if the ItemStack is in cooldown and apply cooldown if it's not
     * @param player The player using the item
     * @param stack The actual item
     * @return boolean if in cooldown or not
     */
    public static boolean isInCooldown(PlayerEntity player, ItemStack stack) {
        ItemCooldownManager itemCooldownManager = player.getItemCooldownManager();

        if (itemCooldownManager.isCoolingDown(stack)) {
            Utils.notify(player, "item_in_cooldown");
            return true;
        }

        itemCooldownManager.set(stack, ArcaneSaddle.CONFIG.itemCooldown()/* * 20 */);
        return false;
    }

    public static boolean isInAllowedDimension(World world) {
        return ArcaneSaddle.CONFIG.allowedDimension().contains(world.getRegistryKey().getValue().toString());
    }
}

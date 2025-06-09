package net.brodino.arcanesaddle.utils;

import net.brodino.arcanesaddle.ArcaneSaddle;
import net.brodino.arcanesaddle.ItemManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class DataHelper {

    public static void saveMountData(AbstractHorseEntity mount, ItemStack stack) {
        NbtCompound nbt = new NbtCompound();
        stack.set(ItemManager.SAVED_MOUNT, mount.writeNbt(nbt).toString());
        stack.set(ItemManager.MOUNT_TYPE, Registries.ENTITY_TYPE.getId(mount.getType()).toString());
    }

    public static NbtCompound loadMountData(ItemStack stack) {
        try {
            NbtElement element = StringNbtReader.readCompound(stack.get(ItemManager.SAVED_MOUNT));

            if (element instanceof NbtCompound compound) {
                return compound;
            }
        } catch (Exception e) {
            ArcaneSaddle.LOGGER.error("Failed to parse NBT: {}", e.getMessage());
        }
        return null;
    }

    public static EntityType<?> loadMountType(ItemStack stack) {
        return Registries.ENTITY_TYPE.get(Identifier.of(stack.get(ItemManager.MOUNT_TYPE)));
    }

    public static boolean hasSavedData(ItemStack stack) {

        String a = stack.get(ItemManager.SAVED_MOUNT);
        String b = stack.get(ItemManager.MOUNT_TYPE);

        return a != null && !a.isEmpty() && b != null && !b.isEmpty();
    }

}

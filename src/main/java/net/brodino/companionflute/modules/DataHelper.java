package net.brodino.companionflute.modules;

import net.brodino.companionflute.CompanionFlute;
import net.brodino.companionflute.modules.utils.CustomComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class DataHelper {

    /**
     * Saved the data inside the item
     * @param mount The "horse" from where to get information
     * @param stack The item where to save information
     */
    public static void saveMountData(AbstractHorseEntity mount, ItemStack stack) {
        NbtCompound nbt = new NbtCompound();
        stack.set(CustomComponents.SAVED_MOUNT, mount.writeNbt(nbt).toString());
        stack.set(CustomComponents.MOUNT_TYPE, Registries.ENTITY_TYPE.getId(mount.getType()).toString());
    }

    /**
     * Gets the information saved inside the item
     * @param stack The item to "unload"
     * @return nbt data
     */
    public static NbtCompound loadMountData(ItemStack stack) {
        try {
            NbtElement element = StringNbtReader.readCompound(stack.get(CustomComponents.SAVED_MOUNT));

            if (element instanceof NbtCompound compound) {
                return compound;
            }
        } catch (Exception e) {
            CompanionFlute.LOGGER.error("Failed to parse NBT: {}", e.getMessage());
        }
        return null;
    }

    public static EntityType<?> loadMountType(ItemStack stack) {
        return Registries.ENTITY_TYPE.get(Identifier.of(stack.get(CustomComponents.MOUNT_TYPE)));
    }

    public static boolean hasSavedData(ItemStack stack) {

        String a = stack.get(CustomComponents.SAVED_MOUNT);
        String b = stack.get(CustomComponents.MOUNT_TYPE);

        return a != null && !a.isEmpty() && b != null && !b.isEmpty();
    }

}

package net.brodino.arcanesaddle;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ItemManager {

    public static final Item ARCANE_SADDLE = register("arcane_saddle", Item::new, new Item.Settings());

    public static final ComponentType<String> SAVED_MOUNT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(ArcaneSaddle.MOD_ID, "saved_mount"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static final ComponentType<String> MOUNT_TYPE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(ArcaneSaddle.MOD_ID, "mount_type"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    // ------------------------------------------------------------------------------------------ \\

    public static void initialize() {
        ArcaneSaddle.LOGGER.info("Initializing items!");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register(groupEntries -> groupEntries.add(ARCANE_SADDLE));
    }

    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ArcaneSaddle.MOD_ID, name));

        Item item = factory.apply(settings.registryKey(itemKey));

        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }
}

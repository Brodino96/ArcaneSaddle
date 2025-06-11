package net.brodino.arcanesaddle.modules;

import net.brodino.arcanesaddle.ArcaneSaddle;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ItemManager {

    public static final Item ARCANE_SADDLE = register("arcane_saddle", Item::new, new Item.Settings().maxCount(1));

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

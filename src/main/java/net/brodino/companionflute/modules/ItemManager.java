package net.brodino.companionflute.modules;

import net.brodino.companionflute.CompanionFlute;
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

    public static final Item COMPANION_FLUTE = register("companion_flute", Item::new, new Item.Settings().maxCount(1));

    // ------------------------------------------------------------------------------------------ \\

    public static void initialize() {
        CompanionFlute.LOGGER.info("Initializing items!");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register(groupEntries -> groupEntries.add(COMPANION_FLUTE));
    }

    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(CompanionFlute.MOD_ID, name));

        Item item = factory.apply(settings.registryKey(itemKey));

        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }
}

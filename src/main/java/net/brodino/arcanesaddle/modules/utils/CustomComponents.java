package net.brodino.arcanesaddle.modules.utils;

import com.mojang.serialization.Codec;
import net.brodino.arcanesaddle.ArcaneSaddle;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomComponents {

    /**
     * All the information about any mount that get saved in that item
     */
    public static final ComponentType<String> SAVED_MOUNT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(ArcaneSaddle.MOD_ID, "saved_mount"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    /**
     * The type of mount to spawn (horse/mule/donkey/etc)
     */
    public static final ComponentType<String> MOUNT_TYPE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(ArcaneSaddle.MOD_ID, "mount_type"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    /**
     * The player that tamed the animal
     */
    public static final ComponentType<String> TAMED_BY = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(ArcaneSaddle.MOD_ID, "tamed_by"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static void initialize() {
        ArcaneSaddle.LOGGER.info("Initializing ComponentTypes!");
    }

}

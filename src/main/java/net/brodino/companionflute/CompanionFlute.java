package net.brodino.companionflute;

import net.brodino.companionflute.config.Config;
import net.brodino.companionflute.modules.EventHandlers;
import net.brodino.companionflute.modules.ItemManager;
import net.brodino.companionflute.modules.utils.CustomComponents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanionFlute implements ModInitializer {

    public static final String MOD_ID = "companion_flute";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Config CONFIG = Config.createAndLoad();
    public static MinecraftServer SERVER;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Arcane Saddle!");
        this.registerEvents();
        CustomComponents.initialize();
        ItemManager.initialize();
    }

    private void registerEvents() {

        // Saved the server on startup
        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
            SERVER = minecraftServer;
        });

        // Player disconnecting
        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {
            EventHandlers.playerDisconnected(handler.player);
        }));

        // Mount dying
        ServerLivingEntityEvents.AFTER_DEATH.register(((entity, damageSource) -> {
            EventHandlers.onMountDeath(entity);
        }));

        // Item being used on an entity
        UseEntityCallback.EVENT.register(EventHandlers::itemUsedOnEntity);

        // Item being used
        UseItemCallback.EVENT.register(EventHandlers::onItemUse);

        // Tick passing
        ServerTickEvents.END_SERVER_TICK.register(EventHandlers::onServerTick);
    }
}

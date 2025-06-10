package net.brodino.arcanesaddle;

import net.brodino.arcanesaddle.config.Config;
import net.brodino.arcanesaddle.modules.EventHandlers;
import net.brodino.arcanesaddle.modules.ItemManager;
import net.brodino.arcanesaddle.modules.utils.CustomComponents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArcaneSaddle implements ModInitializer {

    public static final String MOD_ID = "arcanesaddle";
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

        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
            SERVER = minecraftServer;
        });

        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {
            EventHandlers.playerDisconnected(handler.player);
        }));

        ServerLivingEntityEvents.AFTER_DEATH.register(((entity, damageSource) -> {
            EventHandlers.onMountDeath(entity);
        }));

        UseEntityCallback.EVENT.register(EventHandlers::itemUsedOnEntity);

        UseItemCallback.EVENT.register(EventHandlers::onItemUse);

        ServerTickEvents.END_SERVER_TICK.register(EventHandlers::onServerTick);
    }
}

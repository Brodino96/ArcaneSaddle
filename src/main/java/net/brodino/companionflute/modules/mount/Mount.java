package net.brodino.companionflute.modules.mount;

import net.brodino.companionflute.CompanionFlute;
import net.brodino.companionflute.modules.utils.Utils;
import net.brodino.companionflute.modules.DataHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class Mount {

    public final AbstractHorseEntity entity;
    private final ItemStack stack;
    private final PlayerEntity player;

    public Mount(ItemStack stack, PlayerEntity player) {
        this.stack = stack;
        this.player = player;
        this.entity = this.create();
    }

    /**
     * Loads the data from the item inside an entity
     * @return The entity created
     */
    private AbstractHorseEntity create() {
        EntityType<?> entityType = DataHelper.loadMountType(stack);
        NbtCompound mountData = DataHelper.loadMountData(stack);

        Entity mount = entityType.create(player.getWorld(), SpawnReason.SPAWN_ITEM_USE);

        if (mount == null) {
            Utils.notify(player, "failed_to_load_mount");
            return null;
        }

        mount.readNbt(mountData);

        return (AbstractHorseEntity) mount;
    }

    /**
     * Summons the mount inside the world
     */
    public void summon() {
        this.entity.setPosition(player.getX(), player.getY(), player.getZ());
        player.getWorld().spawnEntity(this.entity);
        Utils.notify(player, "mount_summoned");

        MountManager.playerMounts.put(player.getUuid(), this);
        MountManager.mountTimers.put(this, CompanionFlute.CONFIG.mountTimers() * 20);
    }

    /**
     * Removed the entity from the world and prepares it to be respawned after
     */
    public void dismiss() {
        // Stops the mount from dying if dismissed while falling or dying from fall damage
        this.entity.setHealth(this.entity.getMaxHealth());
        this.entity.fallDistance = 0;
        this.entity.setVelocity(0, 0, 0);

        DataHelper.saveMountData(this.entity, this.stack);
        this.entity.discard();
        Utils.notify(player, "mount_dismissed");

        MountManager.playerMounts.remove(player.getUuid());
        MountManager.playerMounts.remove(player.getUuid());
        MountManager.mountTimers.remove(this);
    }
}

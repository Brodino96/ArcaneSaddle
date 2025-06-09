package net.brodino.arcanesaddle.utils;

import net.brodino.arcanesaddle.ArcaneSaddle;
import net.brodino.arcanesaddle.MountManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class Mount {

    private final AbstractHorseEntity entity;
    private final ItemStack stack;
    private final PlayerEntity player;

    public Mount(ItemStack stack, PlayerEntity player) {
        this.stack = stack;
        this.player = player;
        this.entity = this.create();
    }

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

    public void summon() {
        this.entity.setPosition(player.getX(), player.getY(), player.getZ());
        player.getWorld().spawnEntity(this.entity);
        Utils.notify(player, "mount_summoned");

        MountManager.playerMounts.put(player.getUuid(), this);
        MountManager.mountTimers.put(this, ArcaneSaddle.CONFIG.mountTimers() * 20);
    }

    public void dismiss() {
        DataHelper.saveMountData(this.entity, this.stack);
        this.entity.discard();
        Utils.notify(player, "mount_dismissed");

        MountManager.playerMounts.remove(player.getUuid());
        MountManager.mountTimers.remove(this);
    }
}

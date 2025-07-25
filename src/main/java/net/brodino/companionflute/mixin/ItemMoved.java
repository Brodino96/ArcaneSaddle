package net.brodino.companionflute.mixin;

import net.brodino.companionflute.modules.DataHelper;
import net.brodino.companionflute.modules.ItemManager;
import net.brodino.companionflute.modules.mount.MountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class ItemMoved {

    @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
    private void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {

        ScreenHandler screenHandler = (ScreenHandler)(Object)this;

        if (slotIndex < 0 || slotIndex >= screenHandler.slots.size()) {
            return;
        }

        if (!MountManager.hasSummonedMount(player.getUuid())) {
            return;
        }

        ItemStack itemStack = screenHandler.slots.get(slotIndex).getStack();
        if (itemStack.isEmpty() || !ItemManager.COMPANION_FLUTE.equals(itemStack.getItem())) {
            return;
        }

        if (!DataHelper.hasSavedData(itemStack)) {
            return;
        }

        ci.cancel();
    }
}

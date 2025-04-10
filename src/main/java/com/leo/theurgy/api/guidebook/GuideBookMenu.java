package com.leo.theurgy.api.guidebook;

import com.leo.theurgy.impl.init.TheurgyMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class GuideBookMenu extends AbstractContainerMenu {
    private final Player player;

    public GuideBookMenu(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
        this(pContainerId, inv.player);
    }

    public GuideBookMenu(int containerId, Player player) {
        super(TheurgyMenuTypes.GUIDE_BOOK_MENU.get(), containerId);
        this.player = player;


    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}

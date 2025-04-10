package com.leo.theurgy.impl.item;

import com.leo.theurgy.api.guidebook.GuideBookMenu;
import com.leo.theurgy.impl.TheurgyConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GuideBookItem extends Item implements MenuProvider {
    public GuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.openMenu(this);
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(!context.getPlayer().isCrouching()) return super.useOn(context);
        context.getPlayer().openMenu(this);
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui." + TheurgyConstants.MODID + ".gui.guidebook");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new GuideBookMenu(containerId, player);
    }
}

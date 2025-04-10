package com.leo.theurgy.impl.item;

import com.leo.theurgy.api.aspectus.IAspectusHolderContext;
import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.api.data.aspectus.AspectusHolder;
import com.leo.theurgy.api.aspectus.IAspectusHolder;
import com.leo.theurgy.api.item.AspectusContainerRenderer;
import com.leo.theurgy.impl.Theurgy;
import com.leo.theurgy.impl.init.TheurgyDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.extensions.IItemExtension;

import java.util.Optional;
import java.util.function.Consumer;

public class MionCrystalItem extends Item implements IAspectusHolder, IItemExtension {

    public MionCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        AspectusHolder holder = stack.get(TheurgyDataComponents.ASPECTUS_HOLDER);
        if (holder == null) return;

        Optional<Aspectus> aspectus = holder.aspectus(level.registryAccess());

        if (aspectus.isPresent()) {
            return;
        }

        stack.remove(TheurgyDataComponents.ASPECTUS_HOLDER);
        Theurgy.LOGGER.error("Removed aspectus ${} from stack as the aspectus doesn't exist anymore!", holder.aspectusId());
    }

    @Override
    public int maxAspectus(IAspectusHolderContext context) {
        ItemStack stack = context.stack();
        if(stack == null) return -1;

        return 1;
    }

    @Override
    public Aspectus aspectusType(IAspectusHolderContext context) {
        ItemStack stack = context.stack();
        if(!(context.level() instanceof ServerLevel level)) return null;

        if(stack == null) return null;

        AspectusHolder holder = stack.get(TheurgyDataComponents.ASPECTUS_HOLDER);
        if (holder == null) return null;

        Optional<Aspectus> aspectus = holder.aspectus(level.registryAccess());

        if (aspectus.isPresent()) {
            return aspectus.get();
        }

        stack.remove(TheurgyDataComponents.ASPECTUS_HOLDER);
        Theurgy.LOGGER.error("Removed aspectus ${} from stack as the aspectus doesn't exist anymore!", holder.aspectusId());

        return null;

    }

    @Override
    public int currentAspectus(IAspectusHolderContext context) {
        ItemStack stack = context.stack();

        if(stack == null) return -1;
        if(!stack.has(TheurgyDataComponents.ASPECTUS_HOLDER)) return -1;

        AspectusHolder holder = stack.get(TheurgyDataComponents.ASPECTUS_HOLDER);

        return holder.amount();
    }

    @Override
    public Component getName(ItemStack stack) {
        AspectusHolder holder = stack.get(TheurgyDataComponents.ASPECTUS_HOLDER);
        if (holder == null) return super.getName(stack);

        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if(connection == null) return super.getName(stack);

        RegistryAccess.Frozen registry = connection.registryAccess();
        Optional<Aspectus> aspectus = holder.aspectus(registry);

        if (aspectus.isPresent()) {
            MutableComponent aspectusName = Component.translatable(aspectus.get().translationKey()).withColor(aspectus.get().color());
            Component defaultName = Component.translatable("theurgy.item.mion_crystal.empty").withStyle(ChatFormatting.WHITE);

            return aspectusName.append(defaultName);
        }

        Theurgy.LOGGER.error("Aspectus ${} does not exist anymore!", holder.aspectusId());
        return super.getName(stack);
    }
}

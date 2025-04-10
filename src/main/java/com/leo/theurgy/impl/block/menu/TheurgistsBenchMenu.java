package com.leo.theurgy.impl.block.menu;

import com.leo.theurgy.api.aspectus.IAspectusHolder;
import com.leo.theurgy.api.data.aspectus.AspectusHolder;
import com.leo.theurgy.impl.block.entity.TheurgistsBenchBE;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import com.leo.theurgy.impl.init.TheurgyDataComponents;
import com.leo.theurgy.impl.init.TheurgyMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class TheurgistsBenchMenu extends AbstractContainerMenu {

    public final TheurgistsBenchBE be;
    private final ContainerData data;
    private final IItemHandler itemHandler;
    private ServerPlayer player;

    public TheurgistsBenchMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(TheurgyMenuTypes.THEURGISTS_BENCH_MENU.get(), containerId);
        this.be = ((TheurgistsBenchBE) entity);
        Level level = inv.player.level();

        if(inv.player instanceof ServerPlayer p) this.player = p;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);
        this.data = data;

        itemHandler = level.getCapability(
            Capabilities.ItemHandler.BLOCK,
            be.getBlockPos(),
            be.getBlockState(),
            be,
            null
        );

        createMionCrystalSlots();
        createCraftingSlots();

        be.tryCreateResult(player);

        this.addSlot(new SlotItemHandler(itemHandler, 15, 135, 51) {
            @Override
            public boolean mayPickup(Player playerIn) {
                if(!(playerIn instanceof ServerPlayer player)) return true;

                return TheurgistsBenchMenu.this.be.tryPickupItem(player, true);
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                if(!(player instanceof ServerPlayer sPlayer)) return;

                TheurgistsBenchMenu.this.be.tryPickupItem(sPlayer, false);
                TheurgistsBenchMenu.this.be.tryCreateResult(sPlayer);
                super.setChanged();
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
    }

    private void createMionCrystalSlots() {
        this.addSlot(new SlotItemHandler(itemHandler, 0, 56, 7) {
            @Override
            public void setChanged() {
                TheurgistsBenchMenu.this.be.tryCreateResult(TheurgistsBenchMenu.this.player);
                super.setChanged();
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return mayPlaceCrystal(stack, "theurgy:ordo");
            }
        });

        this.addSlot(new SlotItemHandler(itemHandler, 1, 95, 26) {
            @Override
            public void setChanged() {
                TheurgistsBenchMenu.this.be.tryCreateResult(TheurgistsBenchMenu.this.player);
                super.setChanged();
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return mayPlaceCrystal(stack, "theurgy:aqua");
            }
        });

        this.addSlot(new SlotItemHandler(itemHandler, 2, 95, 75) {
            @Override
            public void setChanged() {
                TheurgistsBenchMenu.this.be.tryCreateResult(TheurgistsBenchMenu.this.player);
                super.setChanged();
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return mayPlaceCrystal(stack, "theurgy:aer");
            }
        });

        this.addSlot(new SlotItemHandler(itemHandler, 3, 56, 95) {
            @Override
            public void setChanged() {
                TheurgistsBenchMenu.this.be.tryCreateResult(TheurgistsBenchMenu.this.player);
                super.setChanged();
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return mayPlaceCrystal(stack, "theurgy:perditio");
            }
        });

        this.addSlot(new SlotItemHandler(itemHandler, 4, 17, 75) {
            @Override
            public void setChanged() {
                TheurgistsBenchMenu.this.be.tryCreateResult(TheurgistsBenchMenu.this.player);
                super.setChanged();
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return mayPlaceCrystal(stack, "theurgy:ignis");
            }
        });

        this.addSlot(new SlotItemHandler(itemHandler, 5, 17, 26) {
            @Override
            public void setChanged() {
                TheurgistsBenchMenu.this.be.tryCreateResult(TheurgistsBenchMenu.this.player);
                super.setChanged();
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return mayPlaceCrystal(stack, "theurgy:terra");
            }
        });
    }

    private void createCraftingSlots() {
        for (int slot : TheurgistsBenchBE.INPUT_SLOTS) {
            this.addSlot(new SlotItemHandler(itemHandler, slot, 38 + (((slot - 6) % 3) * 18), 33 + (((slot - 6) / 3) * 18)) {
                @Override
                public void setChanged() {
                    TheurgistsBenchMenu.this.be.tryCreateResult(TheurgistsBenchMenu.this.player);
                    super.setChanged();
                }
            });
        }
    }

    private boolean mayPlaceCrystal(ItemStack stack, String type) {
        AspectusHolder holder = stack.get(TheurgyDataComponents.ASPECTUS_HOLDER);
        if (holder == null) return false;

        return holder.aspectusId().toString().contains(type);
    }

    public TheurgistsBenchMenu(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(buf.readBlockPos()), new SimpleContainerData(1));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 12 + l * 18, 116 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 12 + i * 18, 174));
        }
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 16;  // must be the number of slots you have!

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.level(), be.getBlockPos()),
            pPlayer, TheurgyBlocks.THEURGISTS_BENCH.get());
    }

    public ContainerData getData() {
        return data;
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }
}

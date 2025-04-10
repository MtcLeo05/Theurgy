package com.leo.theurgy.api.guidebook;

import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.leo.theurgy.api.guidebook.data.GuideBookPage;
import com.leo.theurgy.api.guidebook.data.GuideBookReloadListener;
import com.leo.theurgy.api.util.ScreenUtils;
import com.leo.theurgy.impl.config.ConfigLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GuideBookScreen extends AbstractContainerScreen<GuideBookMenu> {
    private List<GuideBookCategory> categories;

    public GuideBookScreen(GuideBookMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    private int currentCategory = 0;
    private double xMov = 0, yMov = 0;

    private GuideBookEntry currentEntry = null;
    private int currentPage = 0;

    @Override
    protected void init() {
        super.init();

        inventoryLabelY = 10000;
        titleLabelY = 10000;

        categories = GuideBookReloadListener.getInstance().getRegisteredGuideBookCategories().values().stream().toList();

        if (categories == null || categories.isEmpty() || currentCategory >= categories.size()) return;

        GuideBookCategory category = categories.get(currentCategory);

        imageWidth = category.categoryBackgroundSize()[0];
        imageHeight = category.categoryBackgroundSize()[1];

        leftPos = (width - imageWidth) / 2;
        topPos = (height - imageHeight) / 2;

        // Category Button
        for (int i = 0; i < categories.size(); i++) {
            int finalI = i;

            int[] position = calculateCategoryButtonPosition(i, category);

            GuideBookCategory cat = categories.get(i);

            this.addWidget(
                new Button.Builder(Component.empty(), (b) -> {
                    currentCategory = finalI;
                    this.rebuildWidgets();
                })
                    .size(cat.categoryButtonSize()[0], cat.categoryButtonSize()[1])
                    .pos(position[0], position[1])
                    .build()
            );
        }

        if(currentEntry == null) {
            // Entry Buttons
            for (GuideBookEntry entry : category.entries()) {
                if(shouldNotRender(entry.xPos(), entry.yPos(), entry.entryButtonSize()[0], entry.entryButtonSize()[1])) continue;

                List<GuideBookEntry> dependencies = category.dependencies(entry);

                PlayerData playerData = PlayerData.getOrCreateClient();
                boolean isUnlocked = true;

                for (GuideBookEntry dependency : dependencies) {
                    isUnlocked = isUnlocked && playerData.isEntryCompleted(category, dependency);
                }

                if(!isUnlocked) continue;

                this.addWidget(
                    new Button.Builder(Component.empty(), (b) -> {
                        currentEntry = entry;
                        this.rebuildWidgets();
                    })
                        .size(entry.entryButtonSize()[0], entry.entryButtonSize()[1])
                        .pos((int) (this.leftPos + xMov + entry.xPos()), (int) (this.topPos + yMov + entry.yPos()))
                        .build()
                );
            }
        }

        if (currentEntry == null || currentPage >= currentEntry.pages().size()) {
            return;
        }

        GuideBookPage page = currentEntry.pages().get(currentPage);
        page.buttons(this, categories.get(currentCategory), currentEntry).forEach(this::addWidget);

        for (GuiEventListener guiEventListener : page.buttons(this, categories.get(currentCategory), currentEntry)) {
            if(!(guiEventListener instanceof Button b)) return;

            test.add(new int[] {
                b.getX(),
                b.getY(),
                b.getX() + b.getWidth(),
                b.getY() + b.getHeight()
            });
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        if (categories == null || categories.isEmpty() || currentCategory >= categories.size()) return;

        categories.get(currentCategory).renderBackground(guiGraphics, leftPos, topPos, mouseX, mouseY, partialTick);
        if (currentEntry == null || currentEntry.pages().isEmpty()) return;

        handlePage(guiGraphics, partialTick, mouseX, mouseY);
    }

    private void handlePage(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        GuideBookPage page = currentEntry.pages().get(currentPage);

        int[] pageStart = getPageStart(page);

        int pageStartX = pageStart[0];
        int pageStartY = pageStart[1];

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 1000);
        page.render(guiGraphics, pageStartX, pageStartY, mouseX, mouseY, partialTick);
        guiGraphics.pose().popPose();
    }

    private int[] getPageStart(GuideBookPage page) {
        int pageWidth = page.size()[0];
        int pageHeight = page.size()[1];

        return new int[] {
            (width - pageWidth) / 2,
            (height - pageHeight) / 2
        };
    }

    public int @Nullable [] getPageStart() {
        if(currentEntry == null) return null;
        return getPageStart(this.currentEntry.pages().get(currentPage));
    }

    private final List<int[]> test = new ArrayList<>();

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBg(guiGraphics, partialTick, mouseX, mouseX);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        handleCategoryButtons(guiGraphics, mouseX, mouseY, partialTick);
        handleCurrentCategoryEntries(guiGraphics, mouseX, mouseY, partialTick);

        //handleDebug(guiGraphics);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void handleDebug(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 2500);
        for (int[] t : test) {
            guiGraphics.fill(t[0], t[1], t[2], t[3], 0xFFFF0000);
        }
        guiGraphics.pose().popPose();
    }

    private void handleCategoryButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (categories == null || categories.isEmpty()) return;

        for (int i = 0; i < categories.size(); i++) {
            GuideBookCategory category = categories.get(i);
            int[] position = calculateCategoryButtonPosition(i, category);
            category.renderCategoryIcon(guiGraphics, position[0], position[1], mouseX, mouseY, partialTick);
        }
    }

    private int[] calculateCategoryButtonPosition(int i, GuideBookCategory category) {
        return new int[] {
            this.leftPos - category.categoryButtonSize()[0],
            (this.topPos + 5) + (i * category.categoryButtonSize()[1])
        };
    }

    private void handleCurrentCategoryEntries(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (categories == null || categories.isEmpty() || currentCategory >= categories.size()) return;

        GuideBookCategory category = categories.get(currentCategory);
        List<GuideBookEntry> entries = category.entries();

        for (GuideBookEntry entry : entries) {
            List<GuideBookEntry> dependencies = category.dependencies(entry);

            if(shouldNotRender(entry.xPos(), entry.yPos(), entry.entryButtonSize()[0], entry.entryButtonSize()[0])) continue;

            PlayerData playerData = PlayerData.getOrCreateClient();
            boolean isUnlocked = true;

            for (GuideBookEntry dependency : dependencies) {
                isUnlocked = isUnlocked && playerData.isEntryCompleted(category, dependency);
                if(shouldNotRender(dependency.xPos(), dependency.yPos(), dependency.entryButtonSize()[0], dependency.entryButtonSize()[1])) continue;
                entry.renderDependencyLine(guiGraphics, dependency, this.leftPos + xMov + (dependency.entryButtonSize()[0] / 2d), this.topPos + yMov + (dependency.entryButtonSize()[1] / 2d));
            }

            PoseStack pose = guiGraphics.pose();
            pose.pushPose();
            pose.translate(0, 0, 1);

            boolean isComplete = playerData.isEntryCompleted(category, entry);

            entry.renderEntryButton(!isUnlocked, isComplete, guiGraphics, this.leftPos + xMov + entry.xPos(), this.topPos + yMov + entry.yPos(), mouseX, mouseY, partialTick);

            if(currentEntry == null && ScreenUtils.isMouseHovering(this.leftPos + xMov + entry.xPos(), this.topPos + yMov + entry.yPos(), entry.entryButtonSize()[0], entry.entryButtonSize()[1], mouseX, mouseY)) {
                pose.pushPose();
                pose.translate(0, 0, 2500);
                entry.renderTitle(guiGraphics, mouseX, mouseY, mouseX, mouseY, partialTick);
                pose.popPose();
            }

            pose.popPose();
        }
    }

    private boolean shouldNotRender(int xPos, int yPos, int width, int height) {
        return ScreenUtils.isOutsideRenderArea(leftPos + 6, topPos + 6, 250, 250, this.leftPos + xMov + xPos, this.topPos + yMov + yPos, width, height);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button != 1) return super.mouseClicked(mouseX, mouseY, button);

        currentEntry = null;
        currentPage = 0;
        rebuildWidgets();

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button != 2) return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        if (currentEntry != null) return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);

        xMov += dragX;
        yMov += dragY;

        rebuildWidgets();
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    protected void clearWidgets() {
        test.clear();
        super.clearWidgets();
    }

    @Override
    protected void rebuildWidgets() {
        test.clear();
        super.rebuildWidgets();
    }

    public void increasePage() {
        if(currentEntry == null) return;
        this.clearWidgets();
        this.currentPage = Mth.clamp(++currentPage, 0, currentEntry.pages().size() - 1);
        this.rebuildWidgets();
    }

    public void decreasePage() {
        if(currentEntry == null) return;
        this.clearWidgets();
        this.currentPage = Mth.clamp(--currentPage, 0, currentEntry.pages().size() - 1);
        this.rebuildWidgets();
    }
}

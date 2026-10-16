package com.tbadditional.upgrade;

import com.tbadditional.TBAdditional;
import com.tbadditional.client.WorkstationWidget;
import com.tiviacz.travelersbackpack.client.screens.BackpackScreen;
import com.tiviacz.travelersbackpack.client.screens.widgets.WidgetBase;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.List;

public abstract class WorkstationUpgrade extends UpgradeBase {
    protected final String name;

    protected WorkstationUpgrade(UpgradeManager manager, int slot, String name) {
        super(manager, slot, new Point(40, 28));
        this.name = name;
    }

    public abstract AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player);

    public abstract Component title();

    public void open(ServerPlayer player) {
        player.openMenu(new SimpleMenuProvider(this::createMenu, title()));
    }

    @Override
    public boolean hasTab() {
        return false;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getUpgradeSlotsPosition(int x, int y) {
        return List.of();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getUpgradeSlots(BackpackBaseMenu menu, BackpackWrapper wrapper, int x, int y) {
        return List.of();
    }

    @Override
    public WidgetBase createWidget(BackpackScreen screen, int x, int y) {
        return new WorkstationWidget(screen, this,
                new Point(screen.getGuiLeft() + x, screen.getGuiTop() + y),
                new Point(137, 0),
                "screen." + TBAdditional.MODID + "." + name);
    }
}

package com.tbessentials.upgrade;

import com.tbessentials.menu.PortableMenus;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class SmithingUpgrade extends WorkstationUpgrade {
    public SmithingUpgrade(UpgradeManager manager, int slot, ItemStack stack) {
        super(manager, slot, "smithing_table_upgrade");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new PortableMenus.Smithing(id, inv, ContainerLevelAccess.create(player.level(), player.blockPosition()));
    }

    @Override
    public Component title() {
        return Component.translatable("container.upgrade");
    }
}

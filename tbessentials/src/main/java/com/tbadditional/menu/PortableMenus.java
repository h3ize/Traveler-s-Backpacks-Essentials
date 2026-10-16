package com.tbadditional.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.StonecutterMenu;

public final class PortableMenus {
    private PortableMenus() {}

    public static class Anvil extends AnvilMenu {
        public Anvil(int id, Inventory inv, ContainerLevelAccess access) {
            super(id, inv, access);
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }
    }

    public static class Stonecutter extends StonecutterMenu {
        public Stonecutter(int id, Inventory inv, ContainerLevelAccess access) {
            super(id, inv, access);
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }
    }

    public static class Smithing extends SmithingMenu {
        public Smithing(int id, Inventory inv, ContainerLevelAccess access) {
            super(id, inv, access);
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }
    }
}

package com.tbessentials.upgrade;

import com.tbessentials.item.WorkstationUpgradeItem;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.upgrades.ITickableUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class BrewingUpgrade extends WorkstationUpgrade implements ITickableUpgrade {
    private static final int BREW_TIME = 400;
    private static final int SAVE_INTERVAL = 20;

    private final NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
    private int brewTime;
    private int fuel;
    private Item ingredientItem;
    private long lastGameTime = -1;
    private int ticksSinceSave;

    private final Container container = new BrewContainer();
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? brewTime : fuel;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) brewTime = value;
            else fuel = value;
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public BrewingUpgrade(UpgradeManager manager, int slot, ItemStack stack) {
        super(manager, slot, "brewing_stand_upgrade");
        load(stack);
    }


    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new BrewingStandMenu(id, inv, container, data);
    }

    @Override
    public Component title() {
        return Component.translatable("container.brewing");
    }


    @Override
    public int getTickRate() {
        return 1;
    }

    @Override
    public void tick(Player player, Level level, BlockPos pos, int ticks) {
        if (level == null || level.isClientSide) return;
        long now = level.getGameTime();
        if (now == lastGameTime) return;
        int elapsed = lastGameTime < 0 ? 1 : (int) Math.min(Math.max(now - lastGameTime, 1L), 40L);
        lastGameTime = now;

        BlockPos soundPos = pos != null ? pos : (player != null ? player.blockPosition() : null);
        boolean structural = false;
        for (int i = 0; i < elapsed; i++) {
            structural |= step(level, soundPos);
        }
        ticksSinceSave += elapsed;
        if (structural || (brewTime > 0 && ticksSinceSave >= SAVE_INTERVAL)) {
            save();
        }
    }

        private boolean step(Level level, BlockPos soundPos) {
        boolean changed = false;
        ItemStack fuelStack = items.get(4);
        if (fuel <= 0 && fuelStack.is(Items.BLAZE_POWDER)) {
            fuel = 20;
            fuelStack.shrink(1);
            changed = true;
        }

        boolean canBrew = isBrewable(level);
        ItemStack ingredient = items.get(3);

        if (brewTime > 0) {
            if (ingredientItem == null) ingredientItem = ingredient.getItem();
            brewTime--;
            boolean done = brewTime == 0;
            if (done && canBrew) {
                doBrew(level);
                if (soundPos != null) level.levelEvent(1035, soundPos, 0);
                changed = true;
            } else if (!canBrew || !ingredient.is(ingredientItem)) {
                brewTime = 0;
                changed = true;
            }
        } else if (canBrew && fuel > 0) {
            fuel--;
            brewTime = BREW_TIME;
            ingredientItem = ingredient.getItem();
            changed = true;
        }
        return changed;
    }

    private boolean isBrewable(Level level) {
        ItemStack ingredient = items.get(3);
        if (ingredient.isEmpty()) return false;
        PotionBrewing brewing = level.potionBrewing();
        if (!brewing.isIngredient(ingredient)) return false;
        for (int i = 0; i < 3; i++) {
            ItemStack potion = items.get(i);
            if (!potion.isEmpty() && brewing.hasMix(potion, ingredient)) return true;
        }
        return false;
    }

    private void doBrew(Level level) {
        PotionBrewing brewing = level.potionBrewing();
        ItemStack ingredient = items.get(3);
        for (int i = 0; i < 3; i++) {
            items.set(i, brewing.mix(ingredient, items.get(i)));
        }
        ingredient.shrink(1);
    }


    private HolderLookup.Provider registries() {
        return getUpgradeManager().getWrapper().getRegistriesAccess();
    }

    private void load(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.isEmpty()) return;
        HolderLookup.Provider access = registries();
        if (access != null && tag.contains("Items")) {
            ContainerHelper.loadAllItems(tag, items, access);
        }
        brewTime = tag.getInt("BrewTime");
        fuel = tag.getInt("Fuel");
    }

    private void save() {
        HolderLookup.Provider access = registries();
        if (access == null) return;
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, items, access);
        tag.putInt("BrewTime", brewTime);
        tag.putInt("Fuel", fuel);
        updateDataHolderUnchecked(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        ticksSinceSave = 0;
    }

    private boolean stillInBackpack() {
        ItemStack held = getUpgradeManager().getUpgradesHandler().getStackInSlot(getDataHolderSlot());
        return held.getItem() instanceof WorkstationUpgradeItem;
    }


    private class BrewContainer implements Container {
        @Override
        public int getContainerSize() {
            return items.size();
        }

        @Override
        public boolean isEmpty() {
            for (ItemStack s : items) if (!s.isEmpty()) return false;
            return true;
        }

        @Override
        public ItemStack getItem(int slot) {
            return slot >= 0 && slot < items.size() ? items.get(slot) : ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            ItemStack result = ContainerHelper.removeItem(items, slot, amount);
            if (!result.isEmpty()) setChanged();
            return result;
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            return ContainerHelper.takeItem(items, slot);
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            if (slot >= 0 && slot < items.size()) {
                items.set(slot, stack);
                if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
                setChanged();
            }
        }

        @Override
        public void setChanged() {
            save();
        }

        @Override
        public boolean stillValid(Player player) {
            return stillInBackpack();
        }

        @Override
        public void clearContent() {
            for (int i = 0; i < items.size(); i++) items.set(i, ItemStack.EMPTY);
            setChanged();
        }
    }
}

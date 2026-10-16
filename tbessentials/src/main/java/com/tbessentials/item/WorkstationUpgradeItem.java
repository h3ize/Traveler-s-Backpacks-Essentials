package com.tbessentials.item;

import com.tbessentials.upgrade.WorkstationUpgrade;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.items.upgrades.UpgradeItem;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Optional;

public class WorkstationUpgradeItem extends UpgradeItem {
    @FunctionalInterface
    public interface Factory {
        WorkstationUpgrade create(UpgradeManager manager, int slot, ItemStack stack);
    }

    private final String name;
    private final Class<? extends WorkstationUpgrade> upgradeClass;
    private final Factory factory;
    private final boolean ticking;
    private final boolean requiresEquipped;

    public WorkstationUpgradeItem(Properties properties, String name, Class<? extends WorkstationUpgrade> upgradeClass,
                                  Factory factory, boolean ticking, boolean requiresEquipped) {
        super(properties, name);
        this.name = name;
        this.upgradeClass = upgradeClass;
        this.factory = factory;
        this.ticking = ticking;
        this.requiresEquipped = requiresEquipped;
    }

    @Override
    public boolean isTickingUpgrade() {
        return ticking;
    }

    @Override
    public boolean requiresEquippedBackpack() {
        return requiresEquipped;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class getUpgradeClass() {
        return upgradeClass;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public TriFunction getUpgrade() {
        TriFunction<UpgradeManager, Integer, ItemStack, Optional<WorkstationUpgrade>> fn =
                (manager, slot, stack) -> Optional.of(factory.create(manager, slot, stack));
        return fn;
    }
}

package com.tbadditional.item;

import com.tbadditional.TBAdditional;
import com.tbadditional.upgrade.WorkstationUpgrade;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.items.upgrades.UpgradeItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
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
        super(properties, null);
        this.name = name;
        this.upgradeClass = upgradeClass;
        this.factory = factory;
        this.ticking = ticking;
        this.requiresEquipped = requiresEquipped;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("item." + TBAdditional.MODID + "." + name + ".tooltip").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("item.travelersbackpack.upgrade_apply_tooltip"));
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

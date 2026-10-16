package com.tbessentials;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TBEssentials.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tbessentials"))
            .icon(() -> new ItemStack(ModItems.ANVIL_UPGRADE.get()))
            .displayItems((params, out) -> {
                out.accept(ModItems.ANVIL_UPGRADE.get());
                out.accept(ModItems.STONECUTTER_UPGRADE.get());
                out.accept(ModItems.SMITHING_TABLE_UPGRADE.get());
                out.accept(ModItems.BREWING_STAND_UPGRADE.get());
            })
            .build());
}

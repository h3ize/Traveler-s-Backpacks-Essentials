package com.tbessentials.network;

import com.tbessentials.TBEssentials;
import com.tbessentials.item.WorkstationUpgradeItem;
import com.tbessentials.upgrade.WorkstationUpgrade;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.menu.AbstractBackpackMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public record OpenWorkstationPayload(int slot) implements CustomPacketPayload {
    public static final Type<OpenWorkstationPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TBEssentials.MODID, "open_workstation"));

    public static final StreamCodec<ByteBuf, OpenWorkstationPayload> STREAM_CODEC =
            ByteBufCodecs.VAR_INT.map(OpenWorkstationPayload::new, OpenWorkstationPayload::slot);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenWorkstationPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                open(player, payload.slot());
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void open(ServerPlayer player, int slot) {
        if (!(player.containerMenu instanceof AbstractBackpackMenu menu)) return;
        BackpackWrapper wrapper = menu.getWrapper();
        if (wrapper == null) return;

        ItemStackHandler upgrades = wrapper.getUpgrades();
        if (slot < 0 || slot >= upgrades.getSlots()) return;

        ItemStack stack = upgrades.getStackInSlot(slot);
        if (!(stack.getItem() instanceof WorkstationUpgradeItem item)) return;

        Class upgradeClass = item.getUpgradeClass();
        Optional found = wrapper.getUpgradeManager().getUpgrade(upgradeClass);
        Object upgrade = found.orElse(null);
        if (upgrade instanceof WorkstationUpgrade workstation) {
            workstation.open(player);
        }
    }
}

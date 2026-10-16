package com.tbadditional.client;

import com.tbadditional.network.OpenWorkstationPayload;
import com.tiviacz.travelersbackpack.client.screens.BackpackScreen;
import com.tiviacz.travelersbackpack.client.screens.widgets.UpgradeWidgetBase;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;

public class WorkstationWidget extends UpgradeWidgetBase {
    public WorkstationWidget(BackpackScreen screen, UpgradeBase upgrade, Point pos, Point tabUv, String tooltipKey) {
        super(screen, upgrade, pos, tabUv, tooltipKey);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && !Screen.hasShiftDown() && isMouseOverIcon(mouseX, mouseY)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            PacketDistributor.sendToServer(new OpenWorkstationPayload(this.dataHolderSlot));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}

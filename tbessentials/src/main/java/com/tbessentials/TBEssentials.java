package com.tbessentials;

import com.tbessentials.network.OpenWorkstationPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(TBEssentials.MODID)
public class TBEssentials {
    public static final String MODID = "tbessentials";

    public TBEssentials(IEventBus modBus) {
        ModItems.ITEMS.register(modBus);
        ModCreativeTabs.TABS.register(modBus);
        modBus.addListener(TBEssentials::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(OpenWorkstationPayload.TYPE, OpenWorkstationPayload.STREAM_CODEC, OpenWorkstationPayload::handle);
    }
}

package com.tbadditional;

import com.tbadditional.network.OpenWorkstationPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(TBAdditional.MODID)
public class TBAdditional {
    public static final String MODID = "tbadditional";

    public TBAdditional(IEventBus modBus) {
        ModItems.ITEMS.register(modBus);
        ModCreativeTabs.TABS.register(modBus);
        modBus.addListener(TBAdditional::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(OpenWorkstationPayload.TYPE, OpenWorkstationPayload.STREAM_CODEC, OpenWorkstationPayload::handle);
    }
}

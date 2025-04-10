package com.leo.theurgy.impl.network;

import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.network.payloads.ServerResearchComplete;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = TheurgyConstants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TheurgyNetwork {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playBidirectional(
            PlayerData.TYPE,
            PlayerData.STREAM_CODEC,
            PlayerData::handleData
        );

        registrar.playToServer(
            ServerResearchComplete.TYPE,
            ServerResearchComplete.STREAM_CODEC,
            ServerResearchComplete::complete
        );
    }
}

package com.leo.theurgy.impl.init;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.command.GuideBookCommand;
import com.leo.theurgy.impl.command.ResearchCommand;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = TheurgyConstants.MODID)
public class TheurgyCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().
            register(
                Commands
                    .literal(TheurgyConstants.MODID)
                    .then(ResearchCommand.register())
                    .then(GuideBookCommand.register())
            );
    }
}

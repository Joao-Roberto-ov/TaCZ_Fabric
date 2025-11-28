package com.tacz.guns.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class OverwriteCommand {
    private static final String OVERWRITE_NAME = "overwrite";
    private static final String ENABLE = "enable";

    // MUDANÇA 1: Criamos uma variável estática aqui para substituir a Config que deletamos
    public static boolean isOverwriteEnabled = true;

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> reload = Commands.literal(OVERWRITE_NAME);
        RequiredArgumentBuilder<CommandSourceStack, Boolean> enable = Commands.argument(ENABLE, BoolArgumentType.bool());
        reload.then(enable.executes(OverwriteCommand::setOverwrite));
        return reload;
    }

    private static int setOverwrite(CommandContext<CommandSourceStack> context) {
        boolean enable = BoolArgumentType.getBool(context, ENABLE);

        // MUDANÇA 2: Atualizamos a variável local
        isOverwriteEnabled = enable;

        if (context.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            // MUDANÇA 3: Descomentamos a lógica, usando a nova variável
            if (isOverwriteEnabled) {
                serverPlayer.sendSystemMessage(Component.translatable("commands.tacz.reload.overwrite_on"));
            }
            else {
                serverPlayer.sendSystemMessage(Component.translatable("commands.tacz.reload.overwrite_off"));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}

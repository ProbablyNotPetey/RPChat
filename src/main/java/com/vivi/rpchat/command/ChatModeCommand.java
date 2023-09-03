package com.vivi.rpchat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.vivi.rpchat.ChatModeState;
import com.vivi.rpchat.RPChat;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

public class ChatModeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("chatmode").requires(source -> !RPChat.CONFIG.isChatModeCommandOpOnly() || source.hasPermissionLevel(2))
                .then(CommandManager.literal("global")
                        .executes(ctx -> execute(ctx, ChatModeState.ChatMode.GLOBAL))
                        .then(CommandManager.argument("targets", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2))
                                .executes(ctx -> execute(ctx, ChatModeState.ChatMode.GLOBAL, EntityArgumentType.getPlayers(ctx, "targets")))
                        )
                )
                .then(CommandManager.literal("local")
                        .executes(ctx -> execute(ctx, ChatModeState.ChatMode.LOCAL))
                        .then(CommandManager.argument("targets", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2))
                                .executes(ctx -> execute(ctx, ChatModeState.ChatMode.LOCAL, EntityArgumentType.getPlayers(ctx, "targets")))
                        )
                )
                .then(CommandManager.literal("whisper")
                        .executes(ctx -> execute(ctx, ChatModeState.ChatMode.WHISPER))
                        .then(CommandManager.argument("targets", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2))
                                .executes(ctx -> execute(ctx, ChatModeState.ChatMode.WHISPER, EntityArgumentType.getPlayers(ctx, "targets")))
                        )
                )
                .then(CommandManager.literal("shout")
                        .executes(ctx -> execute(ctx, ChatModeState.ChatMode.SHOUT))
                        .then(CommandManager.argument("targets", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2))
                                .executes(ctx -> execute(ctx, ChatModeState.ChatMode.SHOUT, EntityArgumentType.getPlayers(ctx, "targets")))
                        )
                )
                .then(CommandManager.literal("ooc")
                        .executes(ctx -> execute(ctx, ChatModeState.ChatMode.OOC))
                        .then(CommandManager.argument("targets", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2))
                                .executes(ctx -> execute(ctx, ChatModeState.ChatMode.OOC, EntityArgumentType.getPlayers(ctx, "targets")))
                        )
                )
        );

    }

    private static int execute(CommandContext<ServerCommandSource> ctx, ChatModeState.ChatMode mode) {
        return execute(ctx, mode, null);
    }
    private static int execute(CommandContext<ServerCommandSource> ctx, ChatModeState.ChatMode mode, Collection<ServerPlayerEntity> players) {

        ServerCommandSource source = ctx.getSource();

        Collection<ServerPlayerEntity> targets;
        if(players == null) {
            if(source.getPlayer() == null) return 0;
            targets = new ArrayList<>();
            targets.add(source.getPlayer());
        }
        else targets = players;

        ChatModeState chatModeState = ChatModeState.getChatModeState(source.getServer());

        targets.forEach(player -> {
            chatModeState.setChatMode(player, mode);

        });
        if(targets.size() == 1) {
            String strMode = switch (mode) {
                case GLOBAL -> "global";
                case LOCAL -> "local";
                case WHISPER -> "whisper";
                case SHOUT -> "shout";
                case OOC -> "ooc";
            };
            source.sendFeedback(Text.literal("You are now chatting in " + strMode + " channel"), false);
        }
        else {
            source.sendFeedback(Text.literal("Set chat mode for " + targets.size() + " players"), true);
        }


        return 1;
    }
}

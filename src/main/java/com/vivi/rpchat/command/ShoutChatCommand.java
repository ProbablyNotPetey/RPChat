package com.vivi.rpchat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.vivi.rpchat.ChatUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;


public class ShoutChatCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        final LiteralCommandNode<ServerCommandSource> localChatNode = dispatcher.register(CommandManager.literal("shout")
                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            if(context.getSource().getPlayer() == null) return 0;
                            ChatUtil.sendShoutMessage(context.getSource().getPlayer(), Text.literal(StringArgumentType.getString(context, "message")));
                            return 1;
                        })
                )

        );
    }
}

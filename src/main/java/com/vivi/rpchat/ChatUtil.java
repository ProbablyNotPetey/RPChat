package com.vivi.rpchat;

import com.vivi.rpchat.config.RPChatConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ChatUtil {

    public static void sendLocalMessage(ServerPlayerEntity sender, Text message) {
        ServerWorld world = sender.getWorld();

        world.getPlayers().forEach(serverPlayer -> {

            if(sender.getUuid().equals(serverPlayer.getUuid())) {
                //person is sender
                serverPlayer.sendMessage(Text.literal("[Local] <").append(getPlayerName(sender)).append("> ").append(message));
            }
            else {
                double dist = getDistance(sender.getBlockPos(), serverPlayer.getBlockPos());
                if(dist <= RPChat.CONFIG.localChatRange()) {
                    //person is not sender, but in range
                    serverPlayer.sendMessage(Text.literal(prefix("Local", dist) + " <").append(getPlayerName(sender)).append("> ").append(message));
                }
            }

        });
    }

    public static void sendShoutMessage(ServerPlayerEntity sender, Text message) {
        ServerWorld world = sender.getWorld();

        world.getPlayers().forEach(serverPlayer -> {

            if(sender.getUuid().equals(serverPlayer.getUuid())) {
                //person is sender
                serverPlayer.sendMessage(formatText(Text.literal("[SHOUT] <").append(getPlayerName(sender)).append("> ").append(message), Style.EMPTY.withBold(true), RPChat.CONFIG.enableShoutFormatting()));
            }
            else {
                double dist = getDistance(sender.getBlockPos(), serverPlayer.getBlockPos());
                if(dist <= RPChat.CONFIG.shoutRange()) {
                    //person is not sender, but in range
                    serverPlayer.sendMessage(formatText(Text.literal(prefix("SHOUT", dist) + " <").append(getPlayerName(sender)).append("> ").append(message), Style.EMPTY.withBold(true), RPChat.CONFIG.enableShoutFormatting()));
                }
            }

        });
    }

    public static void sendWhisperMessage(ServerPlayerEntity sender, Text message) {
        ServerWorld world = sender.getWorld();

        world.getPlayers().forEach(serverPlayer -> {

            if(sender.getUuid().equals(serverPlayer.getUuid())) {
                //person is sender
                serverPlayer.sendMessage(formatText(Text.literal("[Whisper] <").append(getPlayerName(sender)).append("> ").append(message), Style.EMPTY.withItalic(true).withColor(Formatting.GRAY), RPChat.CONFIG.enableWhisperFormatting()));
            }
            else {
                double dist = getDistance(sender.getBlockPos(), serverPlayer.getBlockPos());
                if(dist <= RPChat.CONFIG.whisperRange()) {
                    //person is not sender, but in range
                    serverPlayer.sendMessage(formatText(Text.literal(prefix("Whisper", dist) + " <").append(getPlayerName(sender)).append("> ").append(message), Style.EMPTY.withItalic(true).withColor(Formatting.GRAY), RPChat.CONFIG.enableWhisperFormatting()));
                }
            }

        });
    }

    public static void sendOOCMessage(ServerPlayerEntity sender, Text message) {
        sender.getWorld().getPlayers().forEach(player -> {
            player.sendMessage(formatText(Text.literal("[OOC] <").append(getPlayerName(sender)).append("> ").append(message), Style.EMPTY.withColor(Formatting.GOLD), RPChat.CONFIG.enableOOCFormatting()));
        });
    }

    public static void sendGlobalMessage(ServerPlayerEntity sender, Text message) {
        sender.getWorld().getPlayers().forEach(player -> {
            player.sendMessage(Text.literal("<").append(getPlayerName(sender)).append("> ").append(message));
        });
    }

    public static Text getPlayerName(PlayerEntity player) {
        return player.hasCustomName() ? player.getCustomName() : player.getDisplayName();
    }

    public static double getDistance(BlockPos pos1, BlockPos pos2) {
        BlockPos d = pos1.subtract(pos2);
        int x = d.getX();
        int y = d.getY();
        int z = d.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }
    private static String formatDist(double d) {
        String out = "" + (Math.round(d * 10.0) / 10.0);
        return out.substring(0, out.indexOf('.') + 2);
    }

    private static String prefix(String text, double dist) {
        return RPChat.CONFIG.enableRangeNumbers() ? String.format("[%1$s %2$sm]", text, formatDist(dist)) : String.format("[%s]", text);
    }

    private static MutableText formatText(MutableText text, Style style, boolean shouldStyle) {
        return shouldStyle ? text.setStyle(style) : text;
    }
}

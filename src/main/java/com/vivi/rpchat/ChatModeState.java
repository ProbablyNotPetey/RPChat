package com.vivi.rpchat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatModeState extends PersistentState {


    public Map<UUID, ChatMode> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {

        NbtCompound allPlayersTag = new NbtCompound();
        players.forEach((uuid, chatMode) -> {
            NbtCompound playerTag = new NbtCompound();
            playerTag.putInt("mode", chatMode.ordinal());
            allPlayersTag.put(String.valueOf(uuid), playerTag);
        });
        tag.put("players", allPlayersTag);
        return tag;
    }

    private static ChatModeState createFromNbt(NbtCompound tag) {
        ChatModeState chatModeState = new ChatModeState();
        NbtCompound allPlayersTag = tag.getCompound("players");
        allPlayersTag.getKeys().forEach(key -> {
            ChatMode chatMode = ChatMode.fromInt(allPlayersTag.getCompound(key).getInt("mode"));
            UUID uuid = UUID.fromString(key);
            chatModeState.players.put(uuid, chatMode);
        });
        return chatModeState;
    }


    public enum ChatMode {
        GLOBAL,
        LOCAL,
        WHISPER,
        SHOUT,
        OOC
        ;

        public static ChatMode fromInt(int i) {
            return switch (i) {
                case 0 -> GLOBAL;
                case 1 -> LOCAL;
                case 2 -> WHISPER;
                case 3 -> SHOUT;
                case 4 -> OOC;
                default -> throw new IllegalArgumentException("Unknown chat mode id: " + i);
            };
        }
    }

    public static ChatModeState getChatModeState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        ChatModeState serverState = persistentStateManager.getOrCreate(
                ChatModeState::createFromNbt,
                ChatModeState::new,
                RPChat.MOD_ID);

        return serverState;
    }

    public ChatMode getChatMode(PlayerEntity player) {
        return this.players.computeIfAbsent(player.getUuid(), uuid -> ChatMode.GLOBAL);
    }

    public void setChatMode(PlayerEntity player, ChatMode mode) {
        this.players.put(player.getUuid(), mode);
        this.markDirty();
    }
}

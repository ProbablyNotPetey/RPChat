package com.vivi.rpchat;

import com.vivi.rpchat.command.*;
import com.vivi.rpchat.config.RPChatConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPChat implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "rpchat";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final RPChatConfig CONFIG = RPChatConfig.create(MOD_ID);


	@Override
	public void onInitialize() {

		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(((message, sender, params) -> {
			if(sender.getServer() == null) return true;
			//cancel the original chat message, handle manually.
			ChatModeState chatModeState = ChatModeState.getChatModeState(sender.getServer());
			ChatModeState.ChatMode mode = chatModeState.getChatMode(sender);
			switch (mode) {
				case LOCAL:
					ChatUtil.sendLocalMessage(sender, message.getContent());
					break;
				case SHOUT:
					ChatUtil.sendShoutMessage(sender, message.getContent());
					break;
				case WHISPER:
					ChatUtil.sendWhisperMessage(sender, message.getContent());
					break;
				case OOC:
					ChatUtil.sendOOCMessage(sender, message.getContent());
					break;
				default:
					return true;
			}
			return false;
		}));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			if(CONFIG.enableChatModeCommand()) ChatModeCommand.register(dispatcher);
			if(CONFIG.enableGlobalChatCommand()) GlobalChatCommand.register(dispatcher);
			if(CONFIG.enableLocalChatCommand()) LocalChatCommand.register(dispatcher);
			if(CONFIG.enableShoutCommand()) ShoutChatCommand.register(dispatcher);
			if(CONFIG.enableWhisperCommand()) WhisperChatCommand.register(dispatcher);
			if(CONFIG.enableOOCCommand()) OOCChatCommand.register(dispatcher);

		});
	}
}

package com.vivi.rpchat.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import com.vivi.rpchat.RPChat;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class RPChatConfig {

    public final String name;
    private boolean isLoading = false;
    private RPChatConfigObject configObject;
    private final Jankson jankson = Jankson.builder().build();


    public boolean enableRangeNumbers() { return configObject.enableRangeNumbers; }
    public boolean enableChatModeCommand() { return configObject.enableChatModeCommand; }
    public boolean isChatModeCommandOpOnly() { return configObject.isChatModeCommandOpOnly; }
    public boolean enableGlobalChatCommand() { return configObject.enableGlobalChatCommand; }
    public boolean enableLocalChatCommand() { return configObject.enableLocalChatCommand; }
    public double localChatRange() { return configObject.localChatRange; }
    public boolean enableShoutCommand() { return configObject.enableShoutCommand; }
    public double shoutRange() { return configObject.shoutRange; }
    public boolean enableShoutFormatting() { return configObject.enableShoutFormatting; }
    public boolean enableWhisperCommand() { return configObject.enableWhisperCommand; }
    public double whisperRange() { return configObject.whisperRange; }
    public boolean enableWhisperFormatting() { return configObject.enableWhisperFormatting; }
    public boolean enableOOCCommand() { return configObject.enableOOCCommand; }
    public boolean enableOOCFormatting() { return configObject.enableOOCFormatting; }



    protected RPChatConfig(String name) {
        this.name = name;
        configObject = new RPChatConfigObject();

    }


    public Path getFileLocation() {
        return FabricLoader.getInstance().getConfigDir().resolve(name + ".json5");
    }



    public static RPChatConfig create(String name) {

        RPChatConfig config = new RPChatConfig(name);
        config.readFromJson();

        return config;
    }


    public void writeToJson() {
        if(isLoading) return;

        try {
            getFileLocation().getParent().toFile().mkdirs();
            Files.writeString(this.getFileLocation(), this.jankson.toJson(this.configObject).toJson(JsonGrammar.JANKSON), StandardCharsets.UTF_8);
        } catch (IOException e) {
            RPChat.LOGGER.warn("Could not save config " + name + ".json5", e);
        }

    }

    public void readFromJson() {
        if(!Files.exists(getFileLocation())) {
            writeToJson();
            return;
        }

        try {
            isLoading = true;

            JsonObject json = jankson.load(Files.readString(this.getFileLocation(), StandardCharsets.UTF_8));
            configObject = jankson.fromJson(json, RPChatConfigObject.class);

        } catch (IOException | SyntaxError e) {
            RPChat.LOGGER.warn("Could not load config " + name + ".json5", e);
        } finally {
            isLoading = false;
        }
    }
}

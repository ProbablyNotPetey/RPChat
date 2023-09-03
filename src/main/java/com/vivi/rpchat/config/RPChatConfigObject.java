package com.vivi.rpchat.config;

import blue.endless.jankson.Comment;

public class RPChatConfigObject {

    @Comment("Enables/disables range numbers appearing when chatting in range-bount chat modes.")
    public boolean enableRangeNumbers = true;
    @Comment("Enables /chatmode")
    public boolean enableChatModeCommand = true;
    @Comment("Enables/disables whether or not /chatmode can only be used by operators.")
    public boolean isChatModeCommandOpOnly = false;
    @Comment("Enables/disables /globalchat")
    public boolean enableGlobalChatCommand = true;

    @Comment("Enables/disables /localchat")
    public boolean enableLocalChatCommand = true;
    @Comment("Range of local chat. Defaults to 30.0")
    public double localChatRange = 30.0;

    @Comment("Enables/disables /shout")
    public boolean enableShoutCommand = true;
    @Comment("Range of shout chat. Defaults to 80.0")
    public double shoutRange = 80.0;
    @Comment("Enables/disables text formatting on shout chat (bold)")
    public boolean enableShoutFormatting = true;

    @Comment("Enables/disables /whisper")
    public boolean enableWhisperCommand = true;
    @Comment("Range of whisper chat. Defaults to 5.0")
    public double whisperRange = 5.0;
    @Comment("Enables/disables text formatting on whisper chat (gray italics)")
    public boolean enableWhisperFormatting = true;

    @Comment("Enables/disables /ooc")
    public boolean enableOOCCommand = true;
    @Comment("Enables/disables text formatting on Out of Character chat (gold)")
    public boolean enableOOCFormatting = true;

}



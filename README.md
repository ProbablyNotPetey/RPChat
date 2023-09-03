# RPChat

RPChat is a fairly simple mod that adds in a handful of commands useful for roleplay servers:

- /localchat : Send a message in local chat. Only players within 30 blocks of you can see this message.
- /globalchat : Sends a normal chat message.
- /shout : Sends a message in shout chat. Players within 80 blocks of you can see this message. Message appears bolded.
- /whisper : Sends a message in whisper chat. Only players within 5 blocks of you can see this message. Message appears gray and italicized.
- /ooc : Sends a message in out of character chat. Message appears gold.
- /chatmode : Allows you to change what chat you type in by default. Options are global, local, shout, whisper, and ooc.


## Configuration

Commands can be enabled / disabled in the config. Text formatting for each mode can additionally be enabled / disabled, and all ranges are configurable. Additionally, /chatmode can be toggled to be only for ops.

## Installation

Requires [Fabric Api](https://modrinth.com/mod/fabric-api). Only needs to be installed on the server.

## Compatibility

This breaks chat reporting! Mods like [No Chat Reports](https://modrinth.com/mod/no-chat-reports) should still function, but if no such mods exist most messages will not be reportable.

May or may not break discord chat mods, AdvancedChat should work fine (both untested)

### FAQ

- Forge? Maybe later, don't ask.
- Backport? No.
- Update? Maybe.

package ca.samueltaylor.simple_commands.helpers;

import ca.samueltaylor.simple_commands.SimpleCommands;
import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

public class Permission {

    protected static JsonObject commands = SimpleCommands.config.get("commands").getAsJsonObject();

    public static boolean can(ServerCommandSource serverCommandSource, LiteralArgumentBuilder<ServerCommandSource> command) {
        JsonObject commandConfig = commands.get(command.getLiteral()).getAsJsonObject();

        if(commandConfig.get("enabled").getAsBoolean()) {
            if(commandConfig.get("opOnly").getAsBoolean()) {
                return serverCommandSource.hasPermissionLevel(4);
            } else {
                return true;
            }
        }

        return false;
    }
}

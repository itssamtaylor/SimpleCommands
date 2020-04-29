package ca.samueltaylor.taylor_commands.helper;

import ca.samueltaylor.taylor_commands.TaylorCommands;
import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

public class Permission {

	public static boolean hasperm(ServerCommandSource source,LiteralArgumentBuilder<ServerCommandSource> literal) {
		JsonObject commandConfig = TaylorCommands.config.get("commands").getAsJsonObject().get(literal.getLiteral()).getAsJsonObject();

		if(commandConfig.get("enabled").getAsBoolean()) {
			if(commandConfig.get("opOnly").getAsBoolean()) {
				return source.hasPermissionLevel(4);
			} else {
				return true;
			}
		}

		return false;
	}

}

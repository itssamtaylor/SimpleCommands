package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.SimpleCommands;
import ca.samueltaylor.simple_commands.abstract_commands.OptionalStringArgument;
import ca.samueltaylor.simple_commands.helpers.Chat;
import ca.samueltaylor.simple_commands.points.HomePointManager;
import ca.samueltaylor.simple_commands.points.WarpPointManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Locale;

public class ListAll extends OptionalStringArgument {
    protected String defaultList;

    public ListAll() {
        command = "listall";
        argumentName = "warps|homes";
        defaultList = SimpleCommands.config
                .get("commands").getAsJsonObject()
                .get("listall").getAsJsonObject()
                .get("defaultList").getAsString();
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        return this.run(commandContext, defaultList);
    }

    @Override
    public int executeWithArgument(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        return this.run(commandContext, StringArgumentType.getString(commandContext, argumentName));
    }

    protected int run(CommandContext<ServerCommandSource> commandContext, String listType) throws CommandSyntaxException {
        PlayerEntity player = commandContext.getSource().getPlayer();
        String startingLetter = listType.toLowerCase(Locale.ROOT).substring(0, 1);
        Chat chat = new Chat(player);

        if(startingLetter.equals("w")) {
            chat.send("Available warps: " + WarpPointManager.instance().listPoints());
        } else if(startingLetter.equals("h")) {
            chat.send("Your homes: " + HomePointManager.instance().listHomes(player));
        } else {
            chat.send("Invalid list option, warps or homes accepted.");
        }

        this.logCommand(commandContext);
        return Command.SINGLE_SUCCESS;
    }
}

package ca.samueltaylor.taylor_commands.commands;

import ca.samueltaylor.taylor_commands.TaylorCommands;
import ca.samueltaylor.taylor_commands.helper.ChatMessage;
import ca.samueltaylor.taylor_commands.helper.HomePoint;
import ca.samueltaylor.taylor_commands.helper.Permission;
import ca.samueltaylor.taylor_commands.helper.Teleport;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;


public class CommandHome {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("home");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(CommandHome::execut)

                .then(CommandManager.argument("HomeName", StringArgumentType.word()).
                        executes(CommandHome::execute));
        dispatcher.register(literal);

    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String args = StringArgumentType.getString(context, "HomeName").toString();
        ServerPlayerEntity player = context.getSource().getPlayer();
        HomePoint home = HomePoint.getHome(player, args);
        ChatMessage chat = new ChatMessage(player);

        if (home != null) {
            Teleport.warp(player, home.location, false);
            chat.send("Warped to home " + home.homename);
            TaylorCommands.logCommand(player, "home");
        } else {
            chat.send("Home does not exist!");
            if (!HomePoint.gethomePoints(player).equals("")) {
                chat.send("Your homes: " + HomePoint.gethomePoints(player));
            } else {
                chat.send("Home not set!");
            }
        }
        return 1;

    }

    public static int execut(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        HomePoint home = HomePoint.getHome(player, "home");
        ChatMessage chat = new ChatMessage(player);

        if (home != null) {
            Teleport.warp(player, home.location, false);
            chat.send("Warped home!");
            TaylorCommands.logCommand(player, "home");
        } else {
            if (!HomePoint.gethomePoints(player).equals("")) {
                chat.send("Your homes: " + HomePoint.gethomePoints(player));
            } else {
                chat.send("Home not set!");
            }
        }
        return 1;
    }

} 




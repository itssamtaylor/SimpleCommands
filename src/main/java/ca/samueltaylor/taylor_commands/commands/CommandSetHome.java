package ca.samueltaylor.taylor_commands.commands;


import ca.samueltaylor.taylor_commands.TaylorCommands;
import ca.samueltaylor.taylor_commands.helper.ChatMessage;
import ca.samueltaylor.taylor_commands.helper.HomePoint;
import ca.samueltaylor.taylor_commands.helper.Permission;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class CommandSetHome {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("sethome");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(CommandSetHome::execut)
                .then(CommandManager.argument("HomeName", StringArgumentType.word())
                        .executes(CommandSetHome::execute));
        dispatcher.register(literal);
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String args = StringArgumentType.getString(context, "HomeName").toString();
        ServerPlayerEntity player = context.getSource().getPlayer();
        int homes = HomePoint.getHomecounts(player);
        ChatMessage chat = new ChatMessage(player);

        if (homes <= TaylorCommands.config.getInt("maxHomes", 5)) {
            HomePoint home = HomePoint.getHome(player, args);
            if (home == null) {
                HomePoint.setHome(player, args);
                chat.send("Home " + HomePoint.getHome(player, args).homename + " set!");
            } else {
                chat.send("Could not set home, it already exists!");
                chat.send("Your homes: " + HomePoint.gethomePoints(player));
            }
        } else {
            chat.send("You have the maximum number of homes!");
            chat.send("Your homes: " + HomePoint.gethomePoints(player));
        }
        return 1;
    }

    public static int execut(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        int homes = HomePoint.getHomecounts(player);
        ChatMessage chat = new ChatMessage(player);

        if (homes <= TaylorCommands.config.getInt("maxHomes", 5)) {
            HomePoint home = HomePoint.getHome(player, "home");
            if (home == null) {
                HomePoint.setHome(player, "home");
                chat.send("Home " + HomePoint.getHome(player, "home").homename + " set!");
            } else {
                chat.send("Could not set home, it already exists!");
                chat.send("Your homes: " + HomePoint.gethomePoints(player));

            }
        } else {
            chat.send("You have the maximum number of homes!");
            chat.send("Your homes: " + HomePoint.gethomePoints(player));
        }
        return 1;
    }
}



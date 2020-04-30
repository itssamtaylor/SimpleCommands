package ca.samueltaylor.taylor_commands.commands;

import ca.samueltaylor.taylor_commands.TaylorCommands;
import ca.samueltaylor.taylor_commands.helper.*;
import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class CommandListAll {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("listall");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(CommandListAll::executeNoArg)
                .then(CommandManager.argument("ListName", StringArgumentType.word())
                .executes(CommandListAll::execute));
        dispatcher.register(literal);
    }

    public static int executeNoArg(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        JsonObject commandConfig = TaylorCommands.config.get("commands").getAsJsonObject().get("listall").getAsJsonObject();
        TaylorCommands.logCommand(player, "listall");
        return listFromArg(commandConfig.get("defaultList").getAsString().toLowerCase(), player);
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String arg = StringArgumentType.getString(context, "ListName").toString();
        ServerPlayerEntity player = context.getSource().getPlayer();
        JsonObject commandConfig = TaylorCommands.config.get("commands").getAsJsonObject().get("listall").getAsJsonObject();
        TaylorCommands.logCommand(player, "listall");
        return listFromArg(arg, player);
    }

    public static int listFromArg(String arg, ServerPlayerEntity player) {
        if(isHomeList(arg)) {
            return listHomes(player);
        }

        if (isWarpList(arg)) {
            return listWarps(player);
        }

        new ChatMessage(player).send(arg + " is not a valid list.");
        return 0;
    }

    public static int listHomes(ServerPlayerEntity player) {
        new ChatMessage(player).send("Your homes: " + HomePoint.gethomePoints(player));
        return Command.SINGLE_SUCCESS;
    }

    public static int listWarps(ServerPlayerEntity player) {
        new ChatMessage(player).send("Available warp points: " + WarpPoint.getWarpPoints());
        return Command.SINGLE_SUCCESS;
    }

    public static boolean isHomeList(String arg) {
        arg = arg.toLowerCase();
        return Objects.equals(arg, "home")
                || Objects.equals(arg, "homes")
                || Objects.equals(arg, "h");
    }

    public static boolean isWarpList(String arg) {
        arg = arg.toLowerCase();
        return Objects.equals(arg, "warp")
                || Objects.equals(arg, "warps")
                || Objects.equals(arg, "w");
    }
}

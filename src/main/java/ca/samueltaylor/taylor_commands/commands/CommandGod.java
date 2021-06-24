package ca.samueltaylor.taylor_commands.commands;

import ca.samueltaylor.taylor_commands.TaylorCommands;
import ca.samueltaylor.taylor_commands.helper.ChatMessage;
import ca.samueltaylor.taylor_commands.helper.Permission;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.GameMode;


public class CommandGod {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("god");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(CommandGod::execute)
                .then(CommandManager.argument("target", EntityArgumentType.players()).executes(CommandGod::execut));

        dispatcher.register(literal);
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
        ChatMessage chat = new ChatMessage(playerEntity);

        if (playerEntity.interactionManager.getGameMode() == GameMode.SURVIVAL || playerEntity.interactionManager.getGameMode() == GameMode.ADVENTURE) {

            if (!playerEntity.getAbilities().invulnerable) {
                playerEntity.getAbilities().invulnerable = true;
                playerEntity.sendAbilitiesUpdate();
                chat.send("God mode enabled!");
                TaylorCommands.logCommand(playerEntity, "god", "enabled god mode");
            } else {
                playerEntity.getAbilities().invulnerable = false;
                playerEntity.sendAbilitiesUpdate();
                chat.send("God mode disabled!");
                TaylorCommands.logCommand(playerEntity, "god", "disabled god mode");
            }
        } else {
            chat.send("God: An error occurred!");
        }

        return 1;
    }

    private static int execut(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
        ServerPlayerEntity requestedPlayer = Command.getPlayer(context.getSource(), EntityArgumentType.getPlayers(context, "target"));
        ChatMessage chatRP = new ChatMessage(requestedPlayer);
        ChatMessage chatSP = new ChatMessage(playerEntity);

        if (playerEntity.interactionManager.getGameMode() == GameMode.SURVIVAL || playerEntity.interactionManager.getGameMode() == GameMode.ADVENTURE) {
            if (!requestedPlayer.getAbilities().invulnerable) {
                requestedPlayer.getAbilities().invulnerable = true;
                requestedPlayer.sendAbilitiesUpdate();
                chatRP.send("God mode enabled!");
                chatSP.send("God mode enabled for " + requestedPlayer.getName().getString());
                TaylorCommands.logCommand(playerEntity, "god", "enabled god mode for " + requestedPlayer.getName().getString());
            } else {
                requestedPlayer.getAbilities().invulnerable = false;
                requestedPlayer.sendAbilitiesUpdate();
                chatRP.send("God mode disabled!");
                chatSP.send("God mode disabled for " + requestedPlayer.getName().getString());
                TaylorCommands.logCommand(playerEntity, "god", "disabled god mode for " + requestedPlayer.getName().getString());
            }
        } else {
            chatSP.send("God: An error occurred!");
        }

        return 1;
    }
}
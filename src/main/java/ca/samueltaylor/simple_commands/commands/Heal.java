package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.OptionalPlayerArgument;
import ca.samueltaylor.simple_commands.helpers.Chat;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class Heal extends OptionalPlayerArgument {
    public Heal() {
        command = "heal";
        argumentName = "target";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        return this.run(commandContext, commandContext.getSource().getPlayer());
    }

    @Override
    public int executeWithArgument(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        return this.run(commandContext, EntityArgumentType.getPlayer(commandContext, argumentName));
    }

    public int run(CommandContext<ServerCommandSource> commandContext, PlayerEntity target) throws CommandSyntaxException {
        PlayerEntity player = commandContext.getSource().getPlayer();

        target.setHealth(target.getMaxHealth());
        target.getHungerManager().setFoodLevel(20);
        Chat.send(target, "You're fully healed.");

        if(!target.equals(player)) {
            Chat.send(player, target.getName().getString() + " is fully healed!");
        }

        this.logCommand(commandContext);
        return Command.SINGLE_SUCCESS;
    }
}

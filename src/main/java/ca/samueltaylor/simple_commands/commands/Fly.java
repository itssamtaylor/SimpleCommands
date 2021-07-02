package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.OptionalPlayerArgument;
import ca.samueltaylor.simple_commands.helpers.Chat;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class Fly extends OptionalPlayerArgument {
    public Fly() {
        command = "fly";
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
        boolean toggle = !target.getAbilities().allowFlying;

        target.getAbilities().allowFlying = toggle;
        target.getAbilities().flying = false;
        target.sendAbilitiesUpdate();

        Chat.send(target, "You can " + (toggle ? "now" : "no longer") + " fly.");

        if(!target.equals(player)) {
            Chat.send(player, (toggle ? "Enabled" : "Disabled") + " fly for " + target.getName().getString());
        }

        this.logCommand(commandContext);
        return Command.SINGLE_SUCCESS;
    }
}

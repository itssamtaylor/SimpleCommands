package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.BaseCommand;
import ca.samueltaylor.simple_commands.helpers.Chat;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class Fly extends BaseCommand {
    public Fly() {
        command = "fly";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity player = commandContext.getSource().getPlayer();
        Chat chat = new Chat(player);
        boolean toggle = !player.getAbilities().allowFlying;

        player.getAbilities().allowFlying = toggle;
        player.getAbilities().flying = false;
        player.sendAbilitiesUpdate();

        chat.send("You can " + (toggle ? "now" : "no longer") + " fly.");

        this.logCommand(commandContext);
        return Command.SINGLE_SUCCESS;
    }
}

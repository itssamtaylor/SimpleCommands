package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.BaseCommand;
import ca.samueltaylor.simple_commands.helpers.Chat;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class God extends BaseCommand {
    static {
        command = "god";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity player = commandContext.getSource().getPlayer();
        Chat chat = new Chat(player);
        boolean toggle = !player.getAbilities().invulnerable;

        player.getAbilities().invulnerable = toggle;
        player.sendAbilitiesUpdate();
        chat.send("God mode " + (toggle ? "enabled!" : "disabled!"));

        return Command.SINGLE_SUCCESS;
    }
}

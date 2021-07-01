package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.BaseCommand;
import ca.samueltaylor.simple_commands.helpers.Chat;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;

public class Repair extends BaseCommand {
    static {
        command = "repair";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity player = commandContext.getSource().getPlayer();
        ItemStack item = player.getMainHandStack();
        Chat chat = new Chat(player);

        if (item.isDamaged()) {
            item.setDamage(0);
            chat.send("Item repaired!");
        } else {
            chat.send("Item is not damaged!");
        }
        return Command.SINGLE_SUCCESS;
    }
}

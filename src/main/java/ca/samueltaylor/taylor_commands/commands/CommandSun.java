package ca.samueltaylor.taylor_commands.commands;

import ca.samueltaylor.taylor_commands.TaylorCommands;
import ca.samueltaylor.taylor_commands.helper.ChatMessage;
import ca.samueltaylor.taylor_commands.helper.Permission;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.level.LevelProperties;


public class CommandSun {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("sun");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(CommandSun::execute);
        dispatcher.register(literal);

    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ChatMessage chat = new ChatMessage(player);

//        player.getServerWorld().getLevelProperties().setClearWeatherTime(10000);
//        player.getServerWorld().getLevelProperties().setRainTime(0);
        player.getServerWorld().getLevelProperties().setRaining(false);
//        player.getServerWorld().getLevelProperties().setThundering(false);
        chat.send("It's sunny!");
        TaylorCommands.logCommand(player, "sun");
        return 1;

    }
}

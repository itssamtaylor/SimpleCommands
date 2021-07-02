package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.BaseCommand;
import ca.samueltaylor.simple_commands.helpers.Chat;
import ca.samueltaylor.simple_commands.helpers.TeleportRequests;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class TeleportAsk extends BaseCommand {
    public TeleportAsk() {
        command = "tpa";
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        super.register(dispatcher);

        RequiredArgumentBuilder<ServerCommandSource, EntitySelector> argumentBuilder = CommandManager.argument("Player", EntityArgumentType.player());
        argumentBuilder.executes(this::execute);

        this.getBaseNode().addChild(argumentBuilder.build());
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity player = commandContext.getSource().getPlayer();
        PlayerEntity target = EntityArgumentType.getPlayer(commandContext, "Player");

        TeleportRequests.add(player, target);

        Chat.send(player, "Sent teleport request to " + target.getName().getString());
        Chat.send(target, "Teleport request from " + player.getName().getString());
        Chat.send(target, "/tpaccept to accept, /tpdeny to deny");

        return Command.SINGLE_SUCCESS;
    }

    @Override
    public LiteralCommandNode<ServerCommandSource> getBaseNode() {
        if(this.baseNode == null) {
            this.baseNode = this.getBaseBuilder().requires(this::requirements).build();
        }
        return this.baseNode;
    }

}

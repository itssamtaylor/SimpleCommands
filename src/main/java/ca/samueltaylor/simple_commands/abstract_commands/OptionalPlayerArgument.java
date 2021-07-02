package ca.samueltaylor.simple_commands.abstract_commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

abstract public class OptionalPlayerArgument extends BaseCommand {
    protected String argumentName;
    protected EntityArgumentType argumentType = EntityArgumentType.player();

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        super.register(dispatcher);

        RequiredArgumentBuilder<ServerCommandSource, EntitySelector> argumentBuilder = CommandManager.argument(argumentName, argumentType);
        argumentBuilder.executes(this::executeWithArgument);

        this.getBaseNode().addChild(argumentBuilder.build());
    }

    abstract public int executeWithArgument(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException;
}

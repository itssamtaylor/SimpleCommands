package ca.samueltaylor.simple_commands.abstract_commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;

abstract public class RequiredStringArgument extends BaseCommand {
    protected static String argumentName;
    protected static StringArgumentType argumentType = StringArgumentType.word();

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        super.register(dispatcher);

        RequiredArgumentBuilder<ServerCommandSource, String> argumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
        argumentBuilder.executes(this::execute);

        this.getBaseNode().addChild(argumentBuilder.build());
    }

    @Override
    public LiteralCommandNode<ServerCommandSource> getBaseNode() {
        if(this.baseNode == null) {
            this.baseNode = this.getBaseBuilder().requires(this::requirements).build();
        }
        return this.baseNode;
    }
}

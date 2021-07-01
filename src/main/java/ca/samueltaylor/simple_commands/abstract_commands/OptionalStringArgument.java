package ca.samueltaylor.simple_commands.abstract_commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

abstract public class OptionalStringArgument extends BaseCommand {

        protected static String argumentName;
        protected static StringArgumentType argumentType = StringArgumentType.word();

        @Override
        public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
            super.register(dispatcher);

            RequiredArgumentBuilder<ServerCommandSource, String> argumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
            argumentBuilder.executes(this::executeWithArgument);

            this.getBaseNode().addChild(argumentBuilder.build());
        }

        abstract public int executeWithArgument(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException;
}

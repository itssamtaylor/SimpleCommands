package ca.samueltaylor.simple_commands.abstract_commands;

import ca.samueltaylor.simple_commands.helpers.Permission;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

abstract public class BaseCommand {
    public static String command;

    protected LiteralCommandNode<ServerCommandSource> baseNode;

    protected LiteralArgumentBuilder<ServerCommandSource> baseBuilder;

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.getRoot().addChild(this.getBaseNode());
    }

    public LiteralCommandNode<ServerCommandSource> getBaseNode() {
        if(this.baseNode == null) {
            this.baseNode = this.getBaseBuilder().requires(this::requirements).executes(this::execute).build();
        }
        return this.baseNode;
    }

    protected boolean requirements(ServerCommandSource source) {
        return Permission.can(source, this.getBaseBuilder());
    }

    protected LiteralArgumentBuilder<ServerCommandSource> getBaseBuilder() {
        if(this.baseBuilder == null) {
            this.baseBuilder = CommandManager.literal(command);
        }
        return this.baseBuilder;
    }

    abstract public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException;
}

package ca.samueltaylor.simple_commands.helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;

public class Chat {
    protected final PlayerEntity player;
    protected boolean actionBar = false;

    public Chat(PlayerEntity player) {
        this.player = player;
    }

    public Chat withActionBar() {
        this.actionBar = true;
        return this;
    }

    public Chat withoutActionBar() {
        this.actionBar = false;
        return this;
    }

    public void send(String message) {
        this.player.sendMessage(new TranslatableText(message), this.actionBar);
    }

    public static void send(PlayerEntity player, String message) {
        new Chat(player).send(message);
    }
}

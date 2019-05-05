/*
 * Developed by Billy Hu on 5/4/19 7:01 PM.
 * Last modified 5/4/19 7:01 PM.
 * Copyright (c) 2019. All rights reserved.
 */

package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

public abstract class AmusementCommand extends Command {
    public AmusementCommand() {
        this.guildOnly = true;
        this.category = new Category("Amusement");
    }

    @Override
    protected void execute(CommandEvent event) {
        TextChannel tchannel = event.getTextChannel();
        if (tchannel == null) {
            try {
                event.getMessage().delete().queue();
            } catch (PermissionException ignore) {
            }
            event.replyInDm("You must enter commands in a text channel!");
            Guild guild = event.getGuild();

            if (guild != null) {
                doCommand(event);
            }
        }
    }

    public abstract void doCommand(CommandEvent event);
}

/*
 * Developed by Billy Hu on 5/1/19 8:15 AM.
 * Last modified 5/1/19 8:42 PM.
 * Copyright (c) 2019. All rights reserved.
 */

package commands;

import audio.AudioManager;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

public abstract class MusicCommand extends Command {
    protected final AudioManager audioManager;

    public MusicCommand(AudioManager audioManager) {
        this.guildOnly = true;
        this.category = new Category("Music");
        this.audioManager = audioManager;
    }

    @Override
    protected void execute(CommandEvent event) {
        TextChannel tchannel = event.getTextChannel();
        if(tchannel == null) {
            try
            {
                event.getMessage().delete().queue();
            } catch(PermissionException ignore){}
            event.replyInDm("You must enter commands in a text channel!");
            return;
        }

        Guild guild = event.getGuild();

        if (guild != null) {
            doCommand(event);
        }
    }

    public abstract void doCommand(CommandEvent event);
}

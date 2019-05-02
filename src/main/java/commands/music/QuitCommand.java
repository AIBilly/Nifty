/*
 * Developed by Billy Hu on 5/1/19 9:30 AM.
 * Last modified 5/1/19 8:49 PM.
 * Copyright (c) 2019. All rights reserved.
 */

package commands.music;

import audio.AudioManager;
import audio.GuildMusicManager;
import com.jagrosh.jdautilities.command.CommandEvent;
import commands.MusicCommand;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class QuitCommand extends MusicCommand {
    public QuitCommand(AudioManager audioManager) {
        super(audioManager);
        this.name = "quit";
        this.help = "let the bot quit from playing";
    }

    @Override
    public void doCommand(CommandEvent event) {
        VoiceChannel connectedChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();

        if(connectedChannel == null) {
            // Get slightly fed up at the user.
            event.replyError("I am not connected to a voice channel!");
            return;
        }

        GuildMusicManager musicManager = audioManager.getGuildAudioPlayer(event.getGuild());
        // Disconnect from the channel.
        event.getGuild().getAudioManager().closeAudioConnection();
        // Notify the user.
        event.reply("Disconnected from the voice channel!");
        musicManager.scheduler.clearQueue();
        musicManager.scheduler.nextTrack();
    }
}

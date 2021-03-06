/*
 * Developed by Billy Hu on 5/1/19 10:35 AM.
 * Last modified 5/2/19 4:32 PM.
 * Copyright (c) 2019. All rights reserved.
 */

package commands.music;

import audio.AudioManager;
import audio.GuildMusicManager;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.MusicCommand;

public class NowPlayingCommand extends MusicCommand {
    public NowPlayingCommand(AudioManager audioManager) {
        super(audioManager);
        this.name = "nowplaying";
        this.help = "show the track is now playing";
    }

    @Override
    public void doCommand(CommandEvent event) {
        GuildMusicManager musicManager = audioManager.getGuildAudioPlayer(event.getGuild());
        AudioPlayer player = musicManager.player;

        AudioTrack currentTrack = player.getPlayingTrack();
        if (currentTrack != null) {
            String title = currentTrack.getInfo().title;
            String position = audioManager.getTimestamp(currentTrack.getPosition());
            String duration = audioManager.getTimestamp(currentTrack.getDuration());

            String nowplaying = String.format("**Playing:** %s\n**Time:** [%s / %s]",
                    title, position, duration);

            event.reply(nowplaying);
        }
        else
            event.replyError("The player is not currently playing anything!");
    }
}

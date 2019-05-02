/*
 * Developed by Billy Hu on 5/1/19 5:35 PM.
 * Last modified 5/2/19 4:33 PM.
 * Copyright (c) 2019. All rights reserved.
 */

package commands.music;

import audio.AudioManager;
import audio.GuildMusicManager;
import audio.TrackScheduler;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.MusicCommand;

public class RestartCommand extends MusicCommand {
    public RestartCommand(AudioManager audioManager) {
        super(audioManager);
        this.name = "restart";
        this.help = "if playing, replay current track, if not playing, replay the previous track";
    }

    @Override
    public void doCommand(CommandEvent event) {
        GuildMusicManager musicManager = audioManager.getGuildAudioPlayer(event.getGuild());
        AudioPlayer player = musicManager.player;
        TrackScheduler scheduler = musicManager.scheduler;

        AudioTrack track = player.getPlayingTrack();
        if (track == null)
            track = scheduler.getLastTrack();

        if (track != null) {
            event.replySuccess("Restarting track: " + track.getInfo().title);
            player.playTrack(track.makeClone());
        }
        else {
            event.replyError("No track has been previously started, so the player cannot replay a track!");
        }
    }
}

/*
 * Developed by Billy Hu on 19-5-4 下午7:21.
 * Last modified 19-5-4 下午7:21.
 * Copyright (c) 2019. All rights reserved.
 */

package commands.music;

import audio.AudioManager;
import audio.GuildMusicManager;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.MusicCommand;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.List;

public class PlayAllCommand extends MusicCommand {
    public PlayAllCommand(AudioManager audioManager) {
        super(audioManager);
        this.name = "playall";
        this.arguments = "<URL>";
        this.help = "plays all tracks from the playlist provided";
    }

    @Override
    public void doCommand(CommandEvent event) {
        if(event.getArgs().isEmpty())
        {
            event.replyError("Please include a query.");
            return;
        }

        GuildVoiceState userState = event.getMember().getVoiceState();
        VoiceChannel voiceChannel = userState.getChannel();

        if(!userState.inVoiceChannel() || userState.isDeafened()) { //check if the author is currently connected to a voice channel
            // Don't forget to .queue()!
            if(voiceChannel == null)
                event.replyError("You are not connected to a voice channel!");
            else
                event.replyError("You must be listening in a voice channel!");
            return;
        }

        try {
            event.getGuild().getAudioManager().openAudioConnection(userState.getChannel());
        }
        catch(PermissionException ex) {
            event.reply(event.getClient().getError()+" I am unable to connect to **"+userState.getChannel().getName()+"**!");
            return;
        }

        GuildMusicManager musicManager = audioManager.getGuildAudioPlayer(event.getGuild());

        event.reply(" Searching... `["+event.getArgs()+"]`",
                message -> audioManager.getPlayerManager().loadItemOrdered(event.getGuild(), event.getArgs(), new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack track) {
                        event.replySuccess("Adding to queue " + track.getInfo().title);

                        musicManager.scheduler.queue(track);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        List<AudioTrack> tracks = playlist.getTracks();

                        if (tracks == null) {
                            event.replyError("No track found from the playlist!");
                            return;
                        }

                        if (tracks.size() == 0) {
                            event.replyError("No track found from the playlist!");
                            return;
                        }

                        StringBuilder sb = new StringBuilder();
                        for (AudioTrack track: tracks) {
                            sb.append("Adding to queue ").append(track.getInfo().title).append(" of playlist ").append(playlist.getName()).append("\n");
                            musicManager.scheduler.queue(track);
                        }

                        sb.append("`[").append(tracks.size()).append("]`").append(" tracks added.");

                        event.replySuccess(sb.toString());
                    }

                    @Override
                    public void noMatches() {
                        event.replyError("Nothing found by " + event.getArgs());
                    }

                    @Override
                    public void loadFailed(FriendlyException exception) {
                        event.replyError("Could not play: " + exception.getMessage());
                    }
                }));
    }
}

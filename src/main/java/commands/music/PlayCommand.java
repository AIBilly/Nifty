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

public class PlayCommand extends MusicCommand {
    public PlayCommand(AudioManager audioManager) {
        super(audioManager);
        this.name = "play";
        this.arguments = "<title|URL|subcommand>";
        this.help = "plays the provided song";
    }

    @Override
    public void doCommand(CommandEvent event) {
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

        try
        {
            event.getGuild().getAudioManager().openAudioConnection(userState.getChannel());
        }
        catch(PermissionException ex)
        {
            event.reply(event.getClient().getError()+" I am unable to connect to **"+userState.getChannel().getName()+"**!");
            return;
        }

        GuildMusicManager musicManager = audioManager.getGuildAudioPlayer(event.getGuild());


        final String trackUrl = event.getArgs();

        audioManager.getPlayerManager().loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                event.replySuccess("Adding to queue " + track.getInfo().title);

                musicManager.scheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                event.replySuccess("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");

                musicManager.scheduler.queue(firstTrack);
            }

            @Override
            public void noMatches() {
                event.replyError("Nothing found by " + trackUrl);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.replyError("Could not play: " + exception.getMessage());
            }

        });
        System.out.println(musicManager.scheduler.getQueue().size());
    }
}

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class CommandListener extends ListenerAdapter{
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public CommandListener(){
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();

        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        playerManager.registerSourceManager(new LocalAudioSourceManager());

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        if (content.startsWith("$ping")) {
            channel.sendMessage("Pong " + event.getJDA().getPing() + "ms").queue();
        } else if (content.startsWith("$Jerry")) {
            channel.sendMessage("Pong " + event.getJDA().getPing() + "ms").queue();
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();

        String[] command = event.getMessage().getContentRaw().split(" ", 2);
        Guild guild = event.getGuild();

        if (guild != null) {
            GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
            AudioPlayer player = musicManager.player;
            TrackScheduler scheduler = musicManager.scheduler;

            if ("$play".equals(command[0]) && command.length == 2) {
                VoiceChannel voiceChannel  = event.getMember().getVoiceState().getChannel();

                if(voiceChannel == null) { //check if the author is currently connected to a voice channel
                    // Don't forget to .queue()!
                    channel.sendMessage("You are not connected to a voice channel!").queue();
                    return;
                }

                loadAndPlay(channel, voiceChannel, command[1]); // play music from source
            } else if ("$skip".equals(command[0])) {
                skipTrack(channel);
            } else if ("$quit".equals(command[0])) {
                VoiceChannel connectedChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
                // Checks if the bot is connected to a voice channel.
                if(connectedChannel == null) {
                    // Get slightly fed up at the user.
                    channel.sendMessage("I am not connected to a voice channel!").queue();
                    return;
                }

                quit(event.getGuild(), channel);
            } else if ("$nowplaying".equals(command[0]) || "$np".equals(command[0])) {
                AudioTrack currentTrack = player.getPlayingTrack();
                if (currentTrack != null)
                {
                    String title = currentTrack.getInfo().title;
                    String position = getTimestamp(currentTrack.getPosition());
                    String duration = getTimestamp(currentTrack.getDuration());

                    String nowplaying = String.format("**Playing:** %s\n**Time:** [%s / %s]",
                            title, position, duration);

                    channel.sendMessage(nowplaying).queue();
                }
                else
                    channel.sendMessage("The player is not currently playing anything!").queue();
            } else if ("$pause".equals(command[0])) {
                if (player.getPlayingTrack() == null)
                {
                    event.getChannel().sendMessage("Cannot pause or resume player because no track is loaded for playing.").queue();
                    return;
                }

                player.setPaused(!player.isPaused());
                if (player.isPaused())
                    channel.sendMessage("The player has been paused.").queue();
                else
                    channel.sendMessage("The player has resumed playing.").queue();
            } else if ("$shuffle".equals(command[0])) {
                if (scheduler.getQueue().isEmpty())
                {
                    event.getChannel().sendMessage("The queue is currently empty!").queue();
                    return;
                }

                scheduler.shuffle();
                event.getChannel().sendMessage("The queue has been shuffled!").queue();
            } else if ("$restart".equals(command[0])) {
                AudioTrack track = player.getPlayingTrack();
                if (track == null)
                    track = scheduler.lastTrack;

                if (track != null)
                {
                    event.getChannel().sendMessage("Restarting track: " + track.getInfo().title).queue();
                    player.playTrack(track.makeClone());
                }
                else
                {
                    event.getChannel().sendMessage("No track has been previously started, so the player cannot replay a track!").queue();
                }
            } else if ("$repeat".equals(command[0])) {
                scheduler.setRepeating(!scheduler.isRepeating());
                event.getChannel().sendMessage("Player was set to: **" + (scheduler.isRepeating() ? "repeat" : "not repeat") + "**").queue();
            }
        }
    }

    private void loadAndPlay(final TextChannel channel, final VoiceChannel voiceChannel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

                play(voiceChannel, channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();

                play(voiceChannel, channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    private void play(VoiceChannel voiceChannel, Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        guild.getAudioManager().openAudioConnection(voiceChannel);

        musicManager.scheduler.queue(track);
    }

    private void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Skipped to next track.").queue();
    }

    private void quit(Guild guild, TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        // Disconnect from the channel.
        guild.getAudioManager().closeAudioConnection();
        // Notify the user.
        channel.sendMessage("Disconnected from the voice channel!").queue();
        musicManager.scheduler.clearQueue();
        musicManager.scheduler.nextTrack();
    }

    private String getTimestamp(long milliseconds)
    {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        if (hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else
            return String.format("%02d:%02d", minutes, seconds);
    }
}

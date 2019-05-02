package commands;

import audio.AudioManager;
import audio.GuildMusicManager;
import audio.TrackScheduler;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
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
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.HashMap;
import java.util.Map;

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

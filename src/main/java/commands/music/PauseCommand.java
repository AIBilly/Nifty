package commands.music;

import audio.AudioManager;
import audio.GuildMusicManager;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import commands.MusicCommand;

public class PauseCommand extends MusicCommand {
    public PauseCommand(AudioManager audioManager) {
        super(audioManager);
        this.name = "pause";
        this.help = "pause playing / continue playing";
    }

    @Override
    public void doCommand(CommandEvent event) {
        GuildMusicManager musicManager = audioManager.getGuildAudioPlayer(event.getGuild());
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            event.replyError("Cannot pause or resume player because no track is loaded for playing.");
            return;
        }

        player.setPaused(!player.isPaused());
        if (player.isPaused())
            event.reply("The player has been paused.");
        else
            event.reply("The player has resumed playing.");
    }
}

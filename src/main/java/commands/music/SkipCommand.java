package commands.music;

import audio.AudioManager;
import audio.GuildMusicManager;
import com.jagrosh.jdautilities.command.CommandEvent;
import commands.MusicCommand;

public class SkipCommand extends MusicCommand {
    public SkipCommand(AudioManager audioManager) {
        super(audioManager);
        this.name = "skip";
        this.help = "skip to the next track in queue";
    }

    @Override
    public void doCommand(CommandEvent event) {
        GuildMusicManager musicManager = audioManager.getGuildAudioPlayer(event.getGuild());
        musicManager.scheduler.nextTrack();

        event.replySuccess("Skipped to next track.");
        System.out.println(musicManager.scheduler.getQueue().size());
    }
}

package commands.music;

import audio.AudioManager;
import audio.GuildMusicManager;
import audio.TrackScheduler;
import com.jagrosh.jdautilities.command.CommandEvent;
import commands.MusicCommand;

public class RepeatCommand extends MusicCommand {
    public RepeatCommand(AudioManager audioManager) {
        super(audioManager);
        this.name = "repeat";
        this.help = "set player to repeating";
    }

    @Override
    public void doCommand(CommandEvent event) {
        GuildMusicManager musicManager = audioManager.getGuildAudioPlayer(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;

        scheduler.setRepeating(!scheduler.isRepeating());
        event.reply("Player was set to: **" + (scheduler.isRepeating() ? "repeat" : "not repeat") + "**");
    }
}

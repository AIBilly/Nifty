package commands.music;

import audio.AudioManager;
import audio.GuildMusicManager;
import audio.TrackScheduler;
import com.jagrosh.jdautilities.command.CommandEvent;
import commands.MusicCommand;

public class ShuffleCommand extends MusicCommand {
    public ShuffleCommand(AudioManager audioManager) {
        super(audioManager);
        this.name = "shuffle";
        this.help = "shuffle the queue";
    }

    @Override
    public void doCommand(CommandEvent event) {
        GuildMusicManager musicManager = audioManager.getGuildAudioPlayer(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;

        if (scheduler.getQueue().isEmpty()) {
            event.getChannel().sendMessage("The queue is currently empty!").queue();
            return;
        }

        scheduler.shuffle();
        event.getChannel().sendMessage("The queue has been shuffled!").queue();
    }
}

/*
 * Developed by Billy Hu on 5/1/19 9:53 PM.
 * Last modified 5/2/19 4:32 PM.
 * Copyright (c) 2019. All rights reserved.
 */

package commands.music;

import audio.AudioManager;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.MusicCommand;

import java.util.Queue;

public class ListCommand extends MusicCommand {
    public ListCommand(AudioManager audioManager) {
        super(audioManager);
        this.name = "list";
        this.help = "show the current queue";
    }

    @Override
    public void doCommand(CommandEvent event) {
        Queue<AudioTrack> queue = audioManager.getGuildAudioPlayer(event.getGuild()).scheduler.getQueue();
        synchronized (queue) {
            if (queue.isEmpty()) {
                event.getChannel().sendMessage("The queue is currently empty!").queue();
            }
            else {
                int trackCount = 0;
                long queueLength = 0;
                StringBuilder sb = new StringBuilder();
                sb.append("Current Queue: Entries: ").append(queue.size()).append("\n");
                for (AudioTrack track : queue) {
                    queueLength += track.getDuration();
                    if (trackCount < 10) {
                        sb.append("`[").append(audioManager.getTimestamp(track.getDuration())).append("]` ");
                        sb.append(track.getInfo().title).append("\n");
                        trackCount++;
                    }
                }
                sb.append("\n").append("Total Queue Time Length: ").append(audioManager.getTimestamp(queueLength));

                event.reply(sb.toString());
            }
        }
    }
}

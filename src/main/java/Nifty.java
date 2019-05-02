/*
 * Developed by Billy Hu on 4/25/19 4:25 PM PM.
 * Last modified 5/1/19 10:00 PM.
 * Copyright (c) 2019. All rights reserved.
 */

import audio.AudioManager;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import commands.amusement.CatCommand;
import commands.music.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Nifty {
    public static void main(String[] args) throws IOException, LoginException, IllegalArgumentException, RateLimitedException {
        Config config = new Config();
        AudioManager audioManager = new AudioManager();

        // define an eventwaiter, dont forget to add this to the JDABuilder!
        EventWaiter waiter = new EventWaiter();

        // define a command client
        CommandClientBuilder client = new CommandClientBuilder();

        // The default is "Type !!help" (or whatver prefix you set)
        client.useDefaultGame();

        // sets the owner of the bot
        client.setOwnerId(config.getOwnerID());

        // sets emojis used throughout the bot on successes, warnings, and failures
        client.setEmojis(config.getSuccessEmoji(), config.getWarningEmoji(),config.getErrorEmoji());

        // sets the bot prefix
        client.setPrefix(config.getPrefix());

        client.addCommands(new CatCommand(),
                            new PlayCommand(audioManager),
                            new QuitCommand(audioManager),
                            new SkipCommand(audioManager),
                            new NowPlayingCommand(audioManager),
                            new PauseCommand(audioManager),
                            new ShuffleCommand(audioManager),
                            new RepeatCommand(audioManager),
                            new RestartCommand(audioManager),
                            new ListCommand(audioManager));

        JDA jda = new JDABuilder(AccountType.BOT)
                // set the token
                .setToken(config.getToken())

                // set the game for when the bot is loading
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setGame(Game.playing("loading..."))

                // add the listeners
                .addEventListener(waiter)
                .addEventListener(client.build())
                // start it up!
                .build();
    }
}

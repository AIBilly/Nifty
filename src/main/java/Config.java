/*
 * Developed by Billy Hu on 5/1/19 0:14 AM.
 * Last modified 5/1/19 7:07 AM.
 * Copyright (c) 2019. All rights reserved.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class Config {
    private String token;
    private String ownerID;
    private String prefix;
    private String successEmoji;
    private String warningEmoji;
    private String errorEmoji;

    public Config() {
        try {
            InputStream is = this.getClass().getResourceAsStream("config.txt");
            if (is == null)
                System.out.println("No file found!");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String s = br.readLine();
            s = s.replaceAll("\r|\n", "");

            this.token =  s;

            /*String path = this.getClass().getResource("config.txt").getPath();
            path = java.net.URLDecoder.decode(path, "UTF-8");
            File file = new File(path);
            Scanner input = new Scanner(file);

            this.token =  input.next();

            input.close();*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.ownerID = "349253106699075594";
        this.prefix = "$";
        this.successEmoji = "\uD83D\uDE03";
        this.warningEmoji = "\uD83D\uDE2E";
        this.errorEmoji = "\uD83D\uDE26";
    }

    public String getToken() {
        return token;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuccessEmoji() {
        return successEmoji;
    }

    public String getWarningEmoji() {
        return warningEmoji;
    }

    public String getErrorEmoji() {
        return errorEmoji;
    }
}

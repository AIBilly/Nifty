public class Config {
    private String token;
    private String ownerID;
    private String prefix;
    private String successEmoji;
    private String warningEmoji;
    private String errorEmoji;

    public Config() {
        this.token = "NTcwNzIyODcyODMwNDU5OTY0.XMFYAA.PCJKgBN79a8iiahGTE36tgCmlqA";
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

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.AccountType;

public class Nifty {
    public static void main(String[] args) throws Exception {
        try {
            JDA api = new JDABuilder(AccountType.BOT).setToken("NTcwNzIyODcyODMwNDU5OTY0.XMFYAA.PCJKgBN79a8iiahGTE36tgCmlqA").build();
            api.addEventListener(new MyEventListener());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

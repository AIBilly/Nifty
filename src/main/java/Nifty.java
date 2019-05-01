import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.AccountType;

public class Nifty {
    public static void main(String[] args) throws Exception {
        Config config = new Config();
        try {
            JDA api = new JDABuilder(AccountType.BOT).setToken(config.getToken()).build();
            api.addEventListener(new CommandListener());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

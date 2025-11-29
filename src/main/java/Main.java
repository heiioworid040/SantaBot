import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;


public class Main {
    public static void main(String[] args) {

        FileManager fileManager = new FileManager();

        String botToken = fileManager.getBotToken();

        if (botToken == null || botToken.isEmpty()) return;

        JDABuilder.createDefault(botToken)
                .enableIntents(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT
                )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new CommandHandler(fileManager))
                .build();


        fileManager.loadChannels();
    }
}
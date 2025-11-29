import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class Main {
    public static void main(String[] args) {

        FileManager fileManager = new FileManager();

        String botToken = fileManager.getBotToken();

        if (botToken == null || botToken.isEmpty()) return;

        JDABuilder.createLight(botToken,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new CommandHandler(fileManager))
                .build();

        fileManager.loadChannels();
    }
}
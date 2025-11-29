import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String CONFIG_FILE_PATH = "santabot_config.json";
    private static final String CHANNEL_FILE_PATH = "santabot_channel.json";
    private JSONObject channelsData;
    public String getBotToken() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE_PATH)));
            JSONObject json = new JSONObject(content);
            return json.getString("botToken");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void loadChannels() {
        try {
            File file = new File(CHANNEL_FILE_PATH);
            if (!file.exists()) {
                try {
                    file.createNewFile();

                    FileWriter writer = new FileWriter(file);
                    writer.write("{}");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String text = Files.readString(file.toPath());
            channelsData = new JSONObject(text.isEmpty() ? "{}" : text);
        } catch (Exception e) {
            e.printStackTrace();
            channelsData = new JSONObject();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CHANNEL_FILE_PATH)) {
            writer.write(channelsData.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBotChannel(String serverId, String channelId) {
        JSONObject guildObj = channelsData.optJSONObject("guild");
        if (guildObj == null) guildObj = new JSONObject();

        JSONObject guild = guildObj.optJSONObject(serverId);
        if (guild == null) guild = new JSONObject();

        JSONArray channel = guild.optJSONArray("channel");
        if (channel == null) channel = new JSONArray();

        if (!channel.toList().contains(channelId)) {
            channel.put(channelId);
        }

        guild.put("channel", channel);
        guildObj.put(serverId, guild);
        channelsData.put("guild", guildObj);

        save();
    }

    public List<String> getBotChannels(String guildId) {
        JSONObject serverObj = channelsData.optJSONObject("guild");
        if (serverObj == null) return new ArrayList<>();

        JSONObject guild = serverObj.optJSONObject(guildId);
        if (guild == null) return new ArrayList<>();

        JSONArray channel = guild.optJSONArray("channel");
        if (channel == null) return new ArrayList<>();

        List<String> list = new ArrayList<>();
        for (int i = 0; i < channel.length(); i++) {
            list.add(channel.getString(i));
        }
        return list;
    }

    public boolean isBotChannel(String guildId, String channelId) {
        return getBotChannels(guildId).contains(channelId);
    }
}
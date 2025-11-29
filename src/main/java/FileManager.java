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
    private static final String CHANNEL_FILE_PATH = "santabot_channels.json";
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
        JSONObject serverObj = channelsData.optJSONObject("servers");
        if (serverObj == null) serverObj = new JSONObject();

        JSONObject server = serverObj.optJSONObject(serverId);
        if (server == null) server = new JSONObject();

        JSONArray channels = server.optJSONArray("botChannels");
        if (channels == null) channels = new JSONArray();

        if (!channels.toList().contains(channelId)) {
            channels.put(channelId);
        }

        server.put("botChannels", channels);
        serverObj.put(serverId, server);
        channelsData.put("servers", serverObj);

        save();
    }

    public List<String> getBotChannels(String serverId) {
        JSONObject serverObj = channelsData.optJSONObject("servers");
        if (serverObj == null) return new ArrayList<>();

        JSONObject server = serverObj.optJSONObject(serverId);
        if (server == null) return new ArrayList<>();

        JSONArray channels = server.optJSONArray("botChannels");
        if (channels == null) return new ArrayList<>();

        List<String> list = new ArrayList<>();
        for (int i = 0; i < channels.length(); i++) {
            list.add(channels.getString(i));
        }
        return list;
    }

    public boolean isBotChannel(String serverId, String channelId) {
        return getBotChannels(serverId).contains(channelId);
    }
}
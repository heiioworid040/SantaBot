import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class CommandHandler extends ListenerAdapter {

    private final FileManager fileManager;

    public CommandHandler(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        Guild guild = event.getGuild();
        MessageChannel channel = event.getChannel();
        User user = event.getAuthor();

        System.out.println(event.getMessage());

        if(user.isBot()) return;

        if(msg.getContentRaw().equals("hi")) {
            msg.reply("ho ho ho").queue();
        }
        else if(msg.getContentRaw().equals("산타봇 채널 설정")) {
            fileManager.addBotChannel(guild.getId(), channel.getId());

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("🎵  캐롤 부르는 산타봇");
            embed.setDescription("현재 재생: 없음");

            Message controlMessage =
                    ((MessageChannelUnion) channel).sendMessageEmbeds(embed.build()).complete();

            controlMessage.addReaction(Emoji.fromUnicode("⏹️")).queue();
            controlMessage.addReaction(Emoji.fromUnicode("🔀")).queue();
            controlMessage.addReaction(Emoji.fromUnicode("⏭️")).queue();
            controlMessage.addReaction(Emoji.fromUnicode("⏯️")).queue();

            if (event.getChannel() instanceof TextChannel textChannel) {
                textChannel.getManager().setTopic("🎄 **산타봇 전용 채널 **" +
                        " \n 산타봇이 캐롤 가방을 들고 왔어요 🎁" +
                        " \n 듣고 싶은 캐롤이 있다면 살짝 말해보세요 🎶" +
                        " \n" +
                        " \n 🧑‍🎄 **산타 사용법** 🧑‍🎄‍" +
                        " \n ✨⏹️ – 캐롤 초기화" +
                        " \n ✨🔀 – 랜덤 캐롤!" +
                        " \n ✨⏭️ – 다음 캐롤로 산타 출발!" +
                        " \n ✨⏯️ – 재생 / 잠깐 멈춰요").queue();
            }

            msg.reply("채널 설정 완료!").queue();
        }
    }

}
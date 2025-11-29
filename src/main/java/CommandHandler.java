import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
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
        Member member = event.getMember();

        System.out.println(msg);

        if(user.isBot()) return;

        if(msg.getContentRaw().equals("!산타야")) {
            msg.reply("🧑‍🎄 반말하지 마세요.").queue();
        }
        else if(msg.getContentRaw().equals("!산타봇채널설정")) {
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

            msg.reply("🎄 " + channel.getAsMention() + "를 산타봇 전용 채널로 설정했어요.").queue();
        }
        else
        {
            if (fileManager.isBotChannel(guild.getId(), channel.getId()))
            {
                GuildVoiceState voiceState = member.getVoiceState();

                if (voiceState == null || !voiceState.inAudioChannel()) {
                    channel.sendMessage("🧑‍🎄 음성 채널에 들어가주세요.").queue();

                    return;
                }

                AudioChannel audioChannel = voiceState.getChannel();
                guild.getAudioManager().openAudioConnection(audioChannel);
            }
        }
    }
}
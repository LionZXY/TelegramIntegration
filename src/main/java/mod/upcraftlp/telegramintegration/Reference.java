package mod.upcraftlp.telegramintegration;

import net.minecraftforge.common.config.Config;

/**
 * (c)2017 UpcraftLP
 */
public class Reference {

    public static final String MCVERSIONS = "[1.11,)";
    public static final String VERSION = "@VERSION@";

    public static final String MODNAME = "Telegram Integration";
    public static final String MODID = "telegramintegration";
    public static final String DEPENDENCIES = "required-after:forge@[13.19.1.2189,)"; //recommended 1.11 forge version as of 2017/07/19
    public static final String UPDATE_URL = "https://minecraft.curseforge.com/projects/telegram-integration";
    public static final String UPDATE_JSON = "https://gist.githubusercontent.com/UpcraftLP/2e49655df6ff855a45cc48297fe165b6/raw/f02c9cc051e99c1919b74c6bc57664ed26da9f92/telegram_integration_update.json";

    @Config(modid = MODID)
    public static class TelegramConfig {

        @Config.Comment("announce player join/leave messages")
        public static boolean announceJoinLeave = true;

        @Config.Comment("announce server start/stop")
        public static boolean serverStartStop = true;

        @Config.Comment("true to only announce PvP deaths")
        public static boolean pvpOnly = false;

        @Config.Comment("list of telegram chats that messages will be relayed to")
        public static String[] chatIDs = new String[0];

        @Config.Comment("the bot api token")
        public static String apiToken = "";

        @Config.Comment("if true, resend message from telegram to minecraft")
        public static boolean chatFromTelegram = false;

        @Config.Comment("timeout in seconds for long pooling update")
        public static int longPoolingTimeout = 10;

        @Config.Comment("message template for minecraft message.  is nickname")
        public static String messageTemplate = "TelegramIntegration: [%nickname%] %message%";

        @Config.Comment("timeout in seconds for long pooling update")
        public static boolean receiveMessageOnlyFromChatIDs = true;

        @Config.Comment("verbose logging for telegram")
        public static boolean verbosLogging = false;

        @Config.Comment("true to send the server chat messages to Telegram (one-way ONLY)")
        public static boolean chatRelay = false;
    }
}

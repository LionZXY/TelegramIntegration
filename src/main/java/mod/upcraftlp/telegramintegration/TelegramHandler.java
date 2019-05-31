package mod.upcraftlp.telegramintegration;

import mod.upcraftlp.telegramintegration.utils.HttpUtils;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * (c)2017 UpcraftLP
 */
public class TelegramHandler implements Runnable {

    private static final ExecutorService SERVICE = Executors.newCachedThreadPool();

    private final String message;
    private final String destination;

    private TelegramHandler(String destination, String message) {
        this.message = message;
        this.destination = destination;
    }

    @Override
    public void run() {
        String baseUrl = "https://api.telegram.org/bot" + Reference.TelegramConfig.apiToken + "/sendMessage";

        List<AbstractMap.SimpleEntry<String, Object>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry<>("parse_mode", "Markdown"));
        params.add(new AbstractMap.SimpleEntry<>("chat_id", destination));
        params.add(new AbstractMap.SimpleEntry<>("text", message));

        try {
            String response = HttpUtils.doPostRequest(baseUrl, params);
            if (Reference.TelegramConfig.verbosLogging) {
                Main.getLogger().info("Telegram answer >> " + response);
            }
        } catch (Exception e) {
            Main.getLogger().error(e);
        }
    }

    public static void postToAll(String message) {
        for (String chat : Reference.TelegramConfig.chatIDs) {
            post(chat, message);
        }
    }

    public static void post(String destination, String message) {
        if (Main.hasConnection()) SERVICE.execute(new TelegramHandler(destination, message));
    }

    public static boolean add(String arg) {
        List<String> IDs = new LinkedList<>(Arrays.asList(Reference.TelegramConfig.chatIDs));
        if (IDs.contains(arg)) return false;
        IDs.add(arg);
        Reference.TelegramConfig.chatIDs = IDs.toArray(new String[0]);
        sync();
        return true;
    }

    public static boolean remove(int index) {
        List<String> IDs = new LinkedList<>(Arrays.asList(Reference.TelegramConfig.chatIDs));
        if (IDs.size() > index) {
            IDs.remove(index);
            Reference.TelegramConfig.chatIDs = IDs.toArray(new String[0]);
            sync();
            return true;
        }
        return false;
    }

    public static void clearList() {
        Reference.TelegramConfig.chatIDs = new String[0];
        sync();
    }

    public static void sync() {
        ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
    }
}

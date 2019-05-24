package mod.upcraftlp.telegramintegration.telegramapi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mod.upcraftlp.telegramintegration.Main;
import mod.upcraftlp.telegramintegration.Reference;
import mod.upcraftlp.telegramintegration.TelegramHandler;
import mod.upcraftlp.telegramintegration.utils.HttpUtils;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TelegramLoop extends Thread {
    private final String BASE_URL = "https://api.telegram.org/bot" + Reference.TelegramConfig.apiToken;
    private final String UPDATE_URL = BASE_URL + "/getUpdates?allowed_updates=[\"message\"]&offset=%s&timeout=" + Reference.TelegramConfig.longPoolingTimeout;
    private final Map<String, IMessageReceiver> receiverMap = new ConcurrentHashMap<String, IMessageReceiver>();
    private final Type updateType = new TypeToken<TelegramAnswerObject<List<UpdateObject>>>() {
    }.getType();
    private final Gson gson = new Gson();
    @Nullable
    private IMessageReceiver globalMessageReceiver;

    public TelegramLoop() {
        TelegramHandler.sync();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                String updateJson = HttpUtils.httpGet(String.format(UPDATE_URL, TelegramOffsetDataHelper.getOffset() + 1));
                logInfoInternal("Get from telegram update " + updateJson);
                TelegramAnswerObject<List<UpdateObject>> updates = gson.fromJson(updateJson, updateType);

                for (UpdateObject update : updates.getResult()) {
                    TelegramOffsetDataHelper.setIfLargerOffset(update.getUpdateId());
                    processUpdate(update);
                }
            } catch (Exception e) {
                Main.getLogger().log(Level.ERROR, e);
                e.printStackTrace();
            }
        }
    }

    private void processUpdate(@Nullable UpdateObject updateObject) {
        if (updateObject == null) {
            return;
        }

        if (updateObject.getMessage() == null) {
            logInfoInternal("I received message without text");
            return;
        }

        String messageText = updateObject.getMessage().getText();

        if (messageText == null || messageText.length() == 0) {
            logInfoInternal("I received message without text");
            return;
        }

        if (globalMessageReceiver != null) {
            globalMessageReceiver.onTelegramMessage(updateObject.getMessage().getFrom(), messageText);
        }

        IMessageReceiver receiver = receiverMap.get(String.valueOf(updateObject.getMessage().getChat().getId()));

        if (receiver != null) {
            receiver.onTelegramMessage(updateObject.getMessage().getFrom(), messageText);
        }
    }

    private void logInfoInternal(String log) {
        if (!Reference.TelegramConfig.logTelegramAnswer) {
            return;
        }
        Main.getLogger().log(Level.INFO, log);
    }

    public void setListener(IMessageReceiver messageReceiver, @Nullable String destination) {
        if (destination == null) {
            globalMessageReceiver = messageReceiver;
            return;
        }
        receiverMap.put(destination, messageReceiver);
    }

}

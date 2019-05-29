package mod.upcraftlp.telegramintegration.telegramapi;

import javax.annotation.Nonnull;

public abstract class IMessageReceiver {
    public void onTelegramMessage(UserObject userObject, @Nonnull String message) {
    }

    public void onTelegramObjectMessage(@Nonnull MessageObject messageObject) {
        String messageText = messageObject.getText();

        if (messageText == null || messageText.length() == 0) {
            TelegramLoop.logInfoInternal("I received message without text");
            return;
        }
        onTelegramMessage(messageObject.getFrom(), messageText);
    }
}

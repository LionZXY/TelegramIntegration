package mod.upcraftlp.telegramintegration.telegramapi;

import javax.annotation.Nonnull;

public interface IMessageReceiver {
    void onTelegramMessage(UserObject userObject, @Nonnull String message);
}

package mod.upcraftlp.telegramintegration;

import mod.upcraftlp.telegramintegration.telegramapi.IMessageReceiver;
import mod.upcraftlp.telegramintegration.telegramapi.UserObject;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;

public class TelegramMessageFormatter extends IMessageReceiver {
    @Override
    public void onTelegramMessage(UserObject userObject, @Nonnull String message) {
        if (TelegramCommandHandler.isCommand(message)) {
            return;
        }
        String textMessage = Reference.TelegramConfig.messageTemplate.replaceAll("%nickname%", userObject.getUsername()).replaceAll("%message%", message);

        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString(textMessage));
    }
}

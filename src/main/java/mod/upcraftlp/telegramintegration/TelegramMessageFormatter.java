package mod.upcraftlp.telegramintegration;

import mod.upcraftlp.telegramintegration.telegramapi.IMessageReceiver;
import mod.upcraftlp.telegramintegration.telegramapi.UserObject;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;

public class TelegramMessageFormatter implements IMessageReceiver {
    @Override
    public void onTelegramMessage(UserObject userObject, @Nonnull String message) {
        String textMessage = String.format("TelegramIntegration: [%s] %s", userObject.getUsername(), message);
        FMLCommonHandler.instance().getMinecraftServerInstance().sendMessage(new TextComponentString(message));
    }
}

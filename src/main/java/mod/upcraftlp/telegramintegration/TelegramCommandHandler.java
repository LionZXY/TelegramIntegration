package mod.upcraftlp.telegramintegration;

import mod.upcraftlp.telegramintegration.telegramapi.IMessageReceiver;
import mod.upcraftlp.telegramintegration.telegramapi.MessageObject;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelegramCommandHandler extends IMessageReceiver {

    public static boolean isCommand(String msg) {
        return msg.startsWith("/");
    }

    @Override
    public void onTelegramObjectMessage(@Nonnull MessageObject messageObject) {
        String messageText = messageObject.getText();

        if (messageText == null || messageText.length() == 0) {
            return;
        }

        if (!messageText.startsWith("/players")) {
            return;
        }

        String chatId = messageObject.getChat().getId().toString();
        List<String> players = getPlayerList();
        if (players.isEmpty()) {
            TelegramHandler.post(chatId, "Никого онлайн. Может, пора это исправить? :)");
            return;
        }

        StringBuilder sb = new StringBuilder("*Игроки онлайн*\n\n");
        for (int i = 0; i < players.size(); i++) {
            sb.append(i + 1).append(". ").append(players.get(i).replace("_", "\\_")).append('\n');
        }

        sb.append("\nВсего игроков: *").append(players.size()).append('*');
        TelegramHandler.post(chatId, sb.toString());
    }

    private List<String> getPlayerList() {
        final MinecraftServer is = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (is == null) {
            return Collections.emptyList();
        }
        final List<String> playerList = new ArrayList<String>();
        for (EntityPlayerMP player : is.getPlayerList().getPlayers()) {
            playerList.add(player.getGameProfile().getName());
        }

        return playerList;
    }
}

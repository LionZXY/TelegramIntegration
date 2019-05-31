package mod.upcraftlp.telegramintegration;

import com.google.common.collect.Lists;
import mod.upcraftlp.telegramintegration.telegramapi.TelegramLoop;
import mod.upcraftlp.telegramintegration.utils.HttpUtils;
import mod.upcraftlp.telegramintegration.utils.LanguageUtils;
import mod.upcraftlp.telegramintegration.utils.TextUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * (c)2017 UpcraftLP
 */
@Mod(
        name = Reference.MODNAME,
        version = Reference.VERSION,
        acceptedMinecraftVersions = Reference.MCVERSIONS,
        modid = Reference.MODID,
        dependencies = Reference.DEPENDENCIES,
        updateJSON = Reference.UPDATE_JSON,
        serverSideOnly = true,
        acceptableRemoteVersions = "*")
public class Main {

    @Mod.Instance
    public static Main instance;

    private static boolean hasConnection = false;
    private static final Logger log = LogManager.getFormatterLogger(Reference.MODNAME);
    private TelegramLoop telegramLoop = null;

    public static Logger getLogger() {
        return log;
    }

    public static boolean hasConnection() {
        return hasConnection;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (StringUtils.isNullOrEmpty(Reference.TelegramConfig.apiToken)) {
            log.error("no API token set, disabling mod!");
        } else {
            hasConnection = HttpUtils.isAvailable("https://api.telegram.org/");
        }
        if (hasConnection()) log.info("Successfully established connection to the telegram services!");
        else log.warn("Unable to connect to the telegram services.");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LanguageUtils.switchServerToLocal();
    }

    private static final List<String> QUOTES = Lists.newArrayList(
            "Let's take over the world!",
            "UNDEFINED",
            "Error 404: Telegram not found",
            "bot does not want to serve you.",
            "Hello Minecraft 1.7.10!",
            "This is not going to work.......",
            Double.toString(Math.random()),
            "welcome to the dark side"
            //TODO moar funny quotes
    );

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        log.info(QUOTES.get((int) (Math.random() * QUOTES.size())));
        if (Reference.TelegramConfig.serverStartStop) TelegramHandler.postToAll("\\[ Сервер запущен ]");
        if (ForgeVersion.getResult(FMLCommonHandler.instance().findContainerFor(instance)).status == ForgeVersion.Status.OUTDATED) {
            TelegramHandler.postToAll("There's a new update for the mod! Download it \\[here](" + Reference.UPDATE_URL + ")!");
        }
        event.registerServerCommand(new CommandTelegram());

        if (!Reference.TelegramConfig.chatFromTelegram || !hasConnection()) {
            return;
        }
        if (telegramLoop == null) {
            telegramLoop = new TelegramLoop();
        }
        telegramLoop.start();
        if (!Reference.TelegramConfig.receiveMessageOnlyFromChatIDs) {
            telegramLoop.setListener(new TelegramMessageFormatter(), null);
            return;
        }
        for (String id : Reference.TelegramConfig.chatIDs) {
            telegramLoop.setListener(new TelegramMessageFormatter(), id);
        }
        telegramLoop.setListener(new TelegramCommandHandler(), null);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        if (Reference.TelegramConfig.serverStartStop) TelegramHandler.postToAll("\\[ Сервер остановлен ]");
        if (telegramLoop != null) {
            telegramLoop.interrupt();
        }
    }

    @Mod.EventBusSubscriber
    public static class EventHandler {

        @SubscribeEvent
        public static void onPlayerDeath(LivingDeathEvent event) {
            if (event.getEntityLiving() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
                if (Reference.TelegramConfig.pvpOnly && !(event.getSource().getTrueSource() instanceof EntityPlayer))
                    return;
                ITextComponent textComponent = player.getCombatTracker().getDeathMessage();
                String message = TextUtils.boldInText(textComponent.getUnformattedText(), player.getGameProfile().getName());
                TelegramHandler.postToAll("\\[ " + message + " ]");
            }
        }

        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (!Reference.TelegramConfig.announceJoinLeave) return;
            String message = "\\[ Игрок *" + event.player.getDisplayNameString() + "* зашел в игру ]";
            TelegramHandler.postToAll(message);
        }

        @SubscribeEvent
        public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
            if (!Reference.TelegramConfig.announceJoinLeave) return;
            String message = "\\[ Игрок *" + event.player.getDisplayNameString() + "* вышел из игры ]";
            TelegramHandler.postToAll(message);
        }

        @SubscribeEvent
        public static void onChatMessage(ServerChatEvent event) {
            if (Reference.TelegramConfig.chatRelay)
                TelegramHandler.postToAll("*" + event.getUsername() + ":* " + event.getMessage());
        }
    }

}


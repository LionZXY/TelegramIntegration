package mod.upcraftlp.telegramintegration.utils;

import net.minecraft.util.text.translation.LanguageMap;

import java.io.InputStream;

public class LanguageUtils {

    public static void switchServerToLocal() {
        InputStream is = LanguageUtils.class.getResourceAsStream("/assets/telegramintegration/lang/ru_ru.lang");
        LanguageMap.inject(is);
    }
}

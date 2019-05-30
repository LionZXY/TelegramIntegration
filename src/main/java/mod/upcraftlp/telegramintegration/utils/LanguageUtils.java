package mod.upcraftlp.telegramintegration.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LanguageUtils {

    public static void switchServerToLocal() {
        for (ModContainer container : Loader.instance().getActiveModList()) {
            InputStream is = LanguageUtils.class.getResourceAsStream("/assets/" + container.getModId() + "/lang/ru_ru.lang");
            LanguageMap.inject(is);
        }

        if (Loader.isModLoaded("ic2")) {
            InputStream is = LanguageUtils.class.getResourceAsStream("/assets/ic2/lang_ic2/ru_ru.properties");
            LanguageMap.inject(is);
        }
    }

    public static ITextComponent transformTextComponent(ITextComponent textComponent) {
        if (!(textComponent instanceof TextComponentTranslation)) {
            return textComponent;
        }

        TextComponentTranslation tCT = (TextComponentTranslation) textComponent;
        List<Object> args = new ArrayList<>();
        for (Object arg : tCT) {
            if (arg instanceof TextComponentTranslation) {
                args.add(transformTextComponent(textComponent));
            } else {
                args.add(arg);
            }
        }
        return new TextComponentTranslation(tCT.getKey(), args);
    }
}

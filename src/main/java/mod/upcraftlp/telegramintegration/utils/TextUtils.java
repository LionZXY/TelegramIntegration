package mod.upcraftlp.telegramintegration.utils;

public class TextUtils {
    public static String boldInText(String text, String textToBold) {
        return text.replaceFirst(textToBold, "*" + textToBold + "*");
    }
}

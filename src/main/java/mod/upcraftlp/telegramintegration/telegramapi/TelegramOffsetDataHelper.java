package mod.upcraftlp.telegramintegration.telegramapi;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import org.apache.http.util.TextUtils;

import java.io.*;

public class TelegramOffsetDataHelper {
    private static Integer offset = -1;
    private static File offsetFile = new File("telegramintegrationsoffset.dat");

    public static Integer getOffset() throws IOException {
        if (offset < 0) {
            if (!offsetFile.exists()) {
                offsetFile.createNewFile();
            }

            String offsetText = readFile(offsetFile).replaceAll("[\\n\\t ]", "").trim();
            if (TextUtils.isEmpty(offsetText)) {
                offset = 0;
            } else {
                offset = Integer.valueOf(offsetText);
            }
        }
        return offset;
    }

    public static void setIfLargerOffset(Integer localOffset) throws IOException {
        if (localOffset <= offset) {
            return;
        }

        offset = localOffset;
        if (!offsetFile.exists()) {
            offsetFile.getParentFile().mkdirs();
            offsetFile.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(offsetFile);
        fileWriter.write(String.valueOf(offset));
        fileWriter.close();
    }

    private static String readFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        return sb.toString();
    }

}

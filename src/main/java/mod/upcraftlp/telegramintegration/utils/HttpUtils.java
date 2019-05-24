package mod.upcraftlp.telegramintegration.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    /**
     * Executes a simple HTTP-GET request
     * @param url URL to request
     * @return The result of request
     * @throws Exception I/O Exception or HTTP errors
     */
    public static String httpGet(String url) throws Exception {
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = br.readLine()) != null) {
            response = response.append(line).append('\r');
        }
        connection.disconnect();
        return response.toString();
    }
}

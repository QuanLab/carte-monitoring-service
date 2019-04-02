package vn.five9.data.util;

import vn.five9.data.config.Config;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class RestUtil {

    /**
     * request get method to Carte server
     * @param urlToRead
     * @return
     * @throws Exception
     */
    public static String get(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        String encoded = Base64.getEncoder().encodeToString((Config.CarteUsername + ":" + Config.CartePassword)
                .getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + encoded);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();
        return result.toString();
    }


    /**
     * get image from Carte Server and encode to base64 format
     * @param url
     *      url of image
     * @return
     *      string base64 of image
     */
    public static String getByteArrayFromImageURL(String url) {
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            //authentication to get stream byte of image
            String encoded = Base64.getEncoder().encodeToString((Config.CarteUsername + ":" + Config.CartePassword)
                    .getBytes(StandardCharsets.UTF_8));
            ucon.setRequestProperty("Authorization", "Basic " + encoded);

            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

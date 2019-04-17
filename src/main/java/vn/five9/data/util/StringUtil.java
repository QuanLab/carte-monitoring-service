package vn.five9.data.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class StringUtil {

    private static final Logger logger  = LogManager.getLogger();

    public static String escapeSQL(String text) {
        try  {
            return URLEncoder.encode(text, "UTF-8");
        }catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return text;
    }


    public static String deEscapeSQL(String text) {
        try  {
            return URLDecoder.decode(text, "UTF-8");
        }catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return text;
    }
}

package vn.five9.data.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StringUtil {

    private static final Logger logger  = LogManager.getLogger();

    public static String escapeSQL(String text) {
        return Base64.encodeBase64String(text.getBytes());
    }


    public static String deEscapeSQL(String base64text) {
        if(base64text == null) {
            return "";
        }
        return new String(Base64.decodeBase64(base64text));
    }
}

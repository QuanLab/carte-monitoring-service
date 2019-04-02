package vn.five9.data.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Properties;


public class Config {

	private static final Logger logger  = LogManager.getLogger();

	public static String CarteUsername;
	public static String CartePassword;
	public static String CarteHost;
	public static String CarteKettleStatus;
	public static String CarteStartJob;
	public static String CarteStopJob;
	public static String CarteJobStatus;
	public static String CarteJobImage;

    public static String repositoryDrive;
    public static String repositoryUrl;
	public static String repositoryUsername;
	public static String repositoryPassword;

    static {
        loadProperties();
    }

    /**
     * load config from property file
     *
     */
    private static void loadProperties() {
        String propertiesFilePath = "carte.properties";
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(propertiesFilePath));
            String key;
            Class c = Config.class;
            Field f;

            for (Object okey : prop.keySet()) {
                key = okey.toString();
                f = c.getDeclaredField(key);
                String typeName = f.getType().getName();
                if (typeName.equals(int.class.getName()) || typeName.equals(Integer.class.getName())) {
                    f.set(Config.class, Integer.valueOf(prop.getProperty(key)));
                } else if (typeName.equals(String.class.getName())) {
                    f.set(Config.class, prop.getProperty(key));
                } else if (typeName.equals(long.class.getName()) || typeName.equals(Long.class.getName())) {
                    f.set(Config.class, Long.valueOf(prop.getProperty(key)));
                } else {
                    f.set(Config.class, prop.getProperty(key));
                }
            }
        } catch (Exception ex) {
			logger.error("Exception in load properties file ", ex);
        }
    }
}
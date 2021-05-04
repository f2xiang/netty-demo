package cn.itcast.server.service;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-03 20:57
 */
public class ServiceFactory {
    private static final Map<String, String> map = new HashMap<>();

    static {
        try (InputStream in = ServiceFactory.class.getResourceAsStream("/application.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            Set<String> names = properties.stringPropertyNames();
            for (String name: names) {
                if (name.endsWith("Service")) {
                    map.put(name, properties.getProperty(name));
                }
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Object getService(String interfaceName) {
        try {
            return Class.forName(map.get(interfaceName)).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

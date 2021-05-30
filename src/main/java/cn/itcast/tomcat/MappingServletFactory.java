package cn.itcast.tomcat;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @description: web.xml的简易替代品 用于url映射
 * @author: fengx
 * @create: 2021-05-03 20:57
 */
public class MappingServletFactory {
    private static final Map<String, String> map = new HashMap<>();

    static {
        try (InputStream in = MappingServletFactory.class.getResourceAsStream("/servlet-mapping.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            Set<String> names = properties.stringPropertyNames();
            for (String name: names) {
                map.put(name, properties.getProperty(name));
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Object getService(String interfaceName) {
        try {
            return Class.forName(map.get(interfaceName)).newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}

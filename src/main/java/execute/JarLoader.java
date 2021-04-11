package execute;

import java.io.File;
import java.io.InvalidObjectException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarLoader {

    public static Object getPort (String jarPath, String className) throws InvalidObjectException {
        try {
            var url = new URL[] {new File(jarPath).toURI().toURL()};
            var classLoader = new URLClassLoader(url, Executor.class.getClassLoader());
            var clazz = Class.forName(className, true, classLoader);
            var instance = clazz.getMethod("getInstance").invoke(null);
            return clazz.getDeclaredField("port").get(instance);
        } catch (Exception e){
            throw new InvalidObjectException("getting port from jar failed!");
        }
    }
}

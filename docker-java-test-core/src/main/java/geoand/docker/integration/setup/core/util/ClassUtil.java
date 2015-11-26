package geoand.docker.integration.setup.core.util;

/**
 * Created by gandrianakis on 26/11/2015.
 */
public abstract class ClassUtil {

    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            Class.forName(className, false, classLoader);
            return true;
        }
        catch (Throwable ex) {
            return false;
        }
    }


}

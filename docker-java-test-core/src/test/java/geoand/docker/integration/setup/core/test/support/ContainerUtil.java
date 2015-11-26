package geoand.docker.integration.setup.core.test.support;

import com.github.dockerjava.api.model.Container;

/**
 * Created by gandrianakis on 1/12/2015.
 */
public class ContainerUtil {

    public static String getFirstContainerName(Container container) {
        if((null == container) || (null == container.getNames()) || (0 ==container.getNames().length)) {
            return null;
        }

        return container.getNames()[0].replace("/", "");
    }
}

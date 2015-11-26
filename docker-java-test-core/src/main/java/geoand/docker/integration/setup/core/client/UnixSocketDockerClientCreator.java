package geoand.docker.integration.setup.core.client;

/**
 * Created by gandrianakis on 23/11/2015.
 */
public class UnixSocketDockerClientCreator extends AbstractDockerClientCreator {

    @Override
    String dockerHostUri() {
        return "unix:///var/run/docker.sock";
    }
}

package geoand.docker.integration.setup.core.client;

/**
 * Created by gandrianakis on 23/11/2015.
 */
public class LocalPortDockerClientCreator extends AbstractDockerClientCreator {

    private String dockerHostUri;

    public LocalPortDockerClientCreator(int port) {
        dockerHostUri = String.format("http://localhost:%d", port);
    }

    @Override
    String dockerHostUri() {
        return dockerHostUri;
    }
}

package geoand.docker.integration.setup.core.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

/**
 * Created by gandrianakis on 23/11/2015.
 */
public abstract class AbstractDockerClientCreator implements DockerClientCreator {

    private static final String DOCKER_API_VERSION = "1.19";

    abstract String dockerHostUri();

    @Override
    public DockerClient create() {
        final DockerClientConfig config = DockerClientConfig
                .createDefaultConfigBuilder()
                .withVersion(DOCKER_API_VERSION)
                .withUri(dockerHostUri())
                .build();

        return DockerClientBuilder.getInstance(config).build();
    }
}

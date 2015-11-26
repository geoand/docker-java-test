package geoand.docker.integration.setup.core.client;

import com.github.dockerjava.api.DockerClient;

/**
 * Created by gandrianakis on 23/11/2015.
 */
public interface DockerClientCreator {

    DockerClient create();
}

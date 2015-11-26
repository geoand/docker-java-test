package geoand.docker.integration.setup.core.status.handler;

import com.github.dockerjava.api.DockerClient;
import geoand.docker.integration.setup.core.model.ContainerLaunchInfo;
import geoand.docker.integration.setup.core.status.model.ContainerStatus;

/**
 * Created by gandrianakis on 25/11/2015.
 */
public interface StatusHandler<T extends ContainerStatus> {

    void handle(DockerClient dockerClient, ContainerLaunchInfo containerLaunchInfo);
}

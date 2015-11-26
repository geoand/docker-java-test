package geoand.docker.integration.setup.core.launcher.strategy;

import com.github.dockerjava.api.DockerClient;
import geoand.docker.integration.setup.core.model.ContainerLaunchInfo;

/**
 * Created by gandrianakis on 7/12/2015.
 */
public interface LaunchStrategy {

    void launch(DockerClient dockerClient, ContainerLaunchInfo containerLaunchInfo);
}

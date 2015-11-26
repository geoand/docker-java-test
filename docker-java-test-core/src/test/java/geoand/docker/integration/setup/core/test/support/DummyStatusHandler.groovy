package geoand.docker.integration.setup.core.test.support

import com.github.dockerjava.api.DockerClient
import geoand.docker.integration.setup.core.model.ContainerLaunchInfo
import geoand.docker.integration.setup.core.status.handler.StatusHandler

/**
 * Created by gandrianakis on 26/11/2015.
 */
public class DummyStatusHandler implements StatusHandler {

    @Override
    void handle(DockerClient dockerClient, ContainerLaunchInfo containerLaunchInfo) {

    }
}

package geoand.docker.integration.setup.core.status.handler;

import com.github.dockerjava.api.DockerClient;
import geoand.docker.integration.setup.core.model.ContainerLaunchInfo;
import geoand.docker.integration.setup.core.status.model.StartedContainerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gandrianakis on 25/11/2015.
 */
public class StartedStatusHandler implements StatusHandler<StartedContainerStatus> {

    final static Logger log = LoggerFactory.getLogger(StartedStatusHandler.class);

    @Override
    public void handle(DockerClient dockerClient, ContainerLaunchInfo containerLaunchInfo) {
        log.info("No action needs to be taken for container " + containerLaunchInfo.getContainerName() + " since it's already started");
    }
}

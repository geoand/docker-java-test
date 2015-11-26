package geoand.docker.integration.setup.core.status.handler;

import com.github.dockerjava.api.DockerClient;
import geoand.docker.integration.setup.core.model.ContainerLaunchInfo;
import geoand.docker.integration.setup.core.status.model.NotRunningContainerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gandrianakis on 25/11/2015.
 */
public class NotRunningStatusHandler implements StatusHandler<NotRunningContainerStatus> {

    final static Logger log = LoggerFactory.getLogger(StartedStatusHandler.class);

    @Override
    public void handle(DockerClient dockerClient, ContainerLaunchInfo containerLaunchInfo) {
        final String containerName = containerLaunchInfo.getContainerName();
        log.debug("Container " + containerName + " needs to be started since it exists but is currently not running");

        dockerClient.startContainerCmd(containerLaunchInfo.getContainerName()).exec();

        log.info("Container " + containerName + " was started");
    }
}

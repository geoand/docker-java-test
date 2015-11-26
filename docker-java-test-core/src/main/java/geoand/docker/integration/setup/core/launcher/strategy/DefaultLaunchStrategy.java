package geoand.docker.integration.setup.core.launcher.strategy;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import geoand.docker.integration.setup.core.launcher.Launcher;
import geoand.docker.integration.setup.core.model.ContainerLaunchInfo;
import geoand.docker.integration.setup.core.status.handler.StatusHandler;
import geoand.docker.integration.setup.core.status.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gandrianakis on 7/12/2015.
 */
public class DefaultLaunchStrategy implements LaunchStrategy {

    final static Logger log = LoggerFactory.getLogger(Launcher.class);

    private static final Set<MatchingContainerStatus<? extends MatchingContainerStatus>> HANDLED_STATUSES = new HashSet<>();
    static {
        HANDLED_STATUSES.add(new StartedContainerStatus());
        HANDLED_STATUSES.add(new NotRunningContainerStatus());
    }

    @Override
    public void launch(DockerClient dockerClient, ContainerLaunchInfo containerLaunchInfo) {
        final List<Container> allContainers = dockerClient.listContainersCmd().withShowAll(true).exec();
        log.debug("Containers currently present: " + allContainers);

        for (Container container : allContainers) {
            if(Arrays.asList(container.getNames()).contains(containerLaunchInfo.getCanonicalContainerName())) {
                final ContainerStatus containerStatus = determineContainerStatus(container);
                if(null == containerStatus) {
                    log.info("Container status " + container.getStatus() + " is not known and cannot be handled");
                    return;
                }
                handle(containerStatus, dockerClient, containerLaunchInfo);
                return;
            }
        }

        handle(new MissingContainerStatus(), dockerClient, containerLaunchInfo);
    }

    private void handle(ContainerStatus<? extends ContainerStatus> containerStatus, DockerClient dockerClient, ContainerLaunchInfo containerLaunchInfo) {
        final StatusHandler<? extends ContainerStatus> statusHandler = containerStatus.getHandlerSupplier().get();
        statusHandler.handle(dockerClient, containerLaunchInfo);
    }

    private ContainerStatus determineContainerStatus(Container container) {
        for (MatchingContainerStatus<? extends MatchingContainerStatus> matchingStatus : HANDLED_STATUSES) {
            if(matchingStatus.matches(container.getStatus())) {
                return matchingStatus;
            }
        }

        return null;
    }
}

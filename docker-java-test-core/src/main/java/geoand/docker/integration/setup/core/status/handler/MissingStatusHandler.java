package geoand.docker.integration.setup.core.status.handler;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.google.common.base.Strings;
import geoand.docker.integration.setup.core.model.ContainerLaunchInfo;
import geoand.docker.integration.setup.core.model.ImageBuildInfo;
import geoand.docker.integration.setup.core.status.model.MissingContainerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by gandrianakis on 25/11/2015.
 */
public class MissingStatusHandler implements StatusHandler<MissingContainerStatus> {

    final static Logger log = LoggerFactory.getLogger(StartedStatusHandler.class);

    @Override
    public void handle(final DockerClient dockerClient, ContainerLaunchInfo containerLaunchInfo) {
        final String containerName = containerLaunchInfo.getContainerName();
        log.debug("Container " + containerName + " needs to be created since it's currently doesn't exist");

        final ImageBuildInfo imageBuildInfo = containerLaunchInfo.getImageBuildInfo();

        createImage(dockerClient, imageBuildInfo);

        createContainer(dockerClient, containerLaunchInfo, containerName, imageBuildInfo);

        startContainer(dockerClient, containerName);
    }

    private void createImage(DockerClient dockerClient, ImageBuildInfo imageBuildInfo) {
        if(!imageExists(dockerClient, imageBuildInfo)) {
            if(imageBuildInfo.useDockerfile()) {
                buildImage(dockerClient, imageBuildInfo);
            }
            else {
                pullImage(dockerClient, imageBuildInfo);
            }
        }
    }

    private boolean imageExists(DockerClient dockerClient, ImageBuildInfo imageBuildInfo) {
        final List<Image> existingImages = dockerClient.listImagesCmd().exec();
        for (Image existingImage : existingImages) {
            if(null != existingImage.getRepoTags()) {
                for (String existingImageTaggedName : existingImage.getRepoTags()) {
                    if(existingImageTaggedName.equals(imageBuildInfo.getTaggedImageName())) {
                        log.debug("Image " + imageBuildInfo.getTaggedImageName() + " already exists");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void buildImage(DockerClient dockerClient, ImageBuildInfo imageBuildInfo) {
        log.debug("Attempting to build image " + imageBuildInfo.getTaggedImageName());

        dockerClient.buildImageCmd(imageBuildInfo.getDockerfileDirectory())
                .withTag(imageBuildInfo.getTaggedImageName())
                .exec(new BuildImageResultCallback()).awaitImageId();

        log.info("Image " + imageBuildInfo.getTaggedImageName() + " built");
    }

    private void pullImage(DockerClient dockerClient, ImageBuildInfo imageBuildInfo) {
        log.debug("Attempting to pull image " + imageBuildInfo.getTaggedImageName());

        dockerClient.pullImageCmd(imageBuildInfo.getImageName()).withTag(imageBuildInfo.getTag()).exec(new PullImageResultCallback()).awaitSuccess();

        log.info("Image " + imageBuildInfo.getTaggedImageName() + " pulled");
    }

    private void createContainer(DockerClient dockerClient, ContainerLaunchInfo containerLaunchInfo, String containerName, ImageBuildInfo imageBuildInfo) {
        log.debug("Attempting to create container " + containerName);

        final CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(containerName)
                .withImage(imageBuildInfo.getTaggedImageName())
                .withName(containerLaunchInfo.getContainerName());

        if(!Strings.isNullOrEmpty(containerLaunchInfo.getCommand())) {
            createContainerCmd.withCmd(containerLaunchInfo.getCommand());
        }

        if(!containerLaunchInfo.getPortBindings().isEmpty()) {
            createContainerCmd.withPortBindings(containerLaunchInfo.getPortBindingsArray());
        }

        createContainerCmd.exec();

        log.info("Container " + containerName + " started");
    }

    private void startContainer(DockerClient dockerClient, String containerName) {
        log.debug("Attempting to start container " + containerName);

        dockerClient.startContainerCmd(containerName).exec();

        log.info("Container " + containerName + " started");
    }
}

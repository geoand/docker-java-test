package geoand.docker.integration.setup;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.NotFoundException;
import org.junit.Test;

/**
 * Created by gandrianakis on 1/12/2015.
 */
public class LauncherEndToEndIntegrationTest {

    private static final String IMAGE = "busybox";
    private static final String CONTAINER_NAME = "busybox-test";
    private static final String TAG = "1.24.1";

    private static final DockerClient dockerClient = new Launcher.Configuration(CONTAINER_NAME, IMAGE).createClient();

    @Test
    public void missingImage() {
        removeContainer(CONTAINER_NAME);
        removeImage(IMAGE, TAG);

        launchContainer(CONTAINER_NAME, IMAGE, TAG);

        assertContainerRunning(CONTAINER_NAME);
    }

    private void assertContainerRunning(String containerName) {

    }

    private void launchContainer(String containerName, String imageName, String imageTag) {
        new Launcher.Configuration(containerName, imageName).imageTag(imageTag).launch();
    }

    private void removeContainer(String containerName) {
        try {
            dockerClient.removeContainerCmd(containerName).withForce().exec();
        } catch (NotFoundException e) {

        }
    }

    private void removeImage(String imageName, String imageTag) {
        try {
            dockerClient.removeImageCmd(imageName + ":" + imageTag).withForce().exec();
        } catch (NotFoundException e) {

        }
    }
}

package geoand.docker.integration.setup.junit.rule;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.Container;
import geoand.docker.integration.setup.core.client.UnixSocketDockerClientCreator;
import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by gandrianakis on 7/12/2015.
 */
public class DockerRuleTest {

    private static final String IMAGE = "jsegura/tiny-redis";
    private static final String CONTAINER_NAME = "tiny-redis";
    private static final String TAG = "latest";

    private static final DockerClient dockerClient = new UnixSocketDockerClientCreator().create();


    @Rule
    public DockerRule dockerRule = new DockerRule(CONTAINER_NAME, IMAGE);

    @BeforeClass
    public static void setup() {
        removeContainer(CONTAINER_NAME);
        removeImage(IMAGE, TAG);
    }

    @AfterClass
    public static void teardown() {
        removeContainer(CONTAINER_NAME);
        removeImage(IMAGE, TAG);
    }

    @Test
    public void test() {
        assertThat(dockerClient.inspectContainerCmd(CONTAINER_NAME).exec().getState().isRunning()).isTrue();
    }

    protected static void removeContainer(String containerName) {
        try {
            dockerClient.removeContainerCmd(containerName).withForce().exec();
        } catch (NotFoundException e) {

        }
    }

    protected static void removeImage(String imageName, String imageTag) {
        try {
            dockerClient.removeImageCmd(taggedImageName(imageName, imageTag)).withForce().exec();
        } catch (NotFoundException e) {

        }
    }

    private static String taggedImageName(String imageName, String imageTag) {
        return imageName + ":" + imageTag;
    }

    protected void assertContainerRunning(Container container) {
        if(null == container) {
            Assert.fail("The container was not launched");
        }
        assertThat(container.getStatus()).contains("Up");
    }

}
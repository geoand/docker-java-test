package geoand.docker.integration.setup.core.launcher

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.NotFoundException
import com.github.dockerjava.api.model.Container
import com.github.dockerjava.core.command.PullImageResultCallback
import geoand.docker.integration.setup.core.test.support.ContainerUtil
import geoand.docker.integration.setup.core.test.support.OutputCapture
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

import static org.assertj.core.api.Assertions.assertThat

/**
 * Created by gandrianakis on 1/12/2015.
 */
abstract class AbstractFunctionalTest {

    protected static final String IMAGE = "jsegura/tiny-redis"
    protected static final String CONTAINER_NAME = "tiny-redis"
    protected static final String TAG = "2.8.17"
    protected static final String DOCKER_FILE = "tiny-redis"

    protected static final DockerClient dockerClient = new Launcher.Configuration(CONTAINER_NAME, IMAGE).createClient()

    @Before
    public void setUp() {  //ensure that the proper Status Handler classes are used, not the mocks
        System.setProperty("statusHandler.useCaching", "false")
    }

    @After
    public void tearDown() {
        System.clearProperty("statusHandler.useCaching")
    }

    @Rule
    public OutputCapture output = new OutputCapture()

    protected Container doLaunch(String containerName, String imageName, String imageTag) {
        return doLaunch(containerName, imageName, imageTag, null)
    }

    protected Container doLaunch(String containerName, String imageName, String imageTag, String dockerfile) {
        final def configuration = new Launcher.Configuration(containerName, imageName).imageTag(imageTag)
        if(dockerfile) {
            configuration.dockerfileDirectoryClasspath(dockerfile)
        }

        configuration.launch()

        return dockerClient.listContainersCmd().exec().find { container -> Arrays.asList(container.names).find { name -> name.contains(containerName)}}
    }

    protected void pullImage(String imageName, String imageTag) {
        dockerClient.pullImageCmd(imageName).withTag(imageTag).exec(new PullImageResultCallback()).awaitSuccess();
    }

    protected void removeContainer(String containerName) {
        try {
            dockerClient.removeContainerCmd(containerName).withForce().exec()
        } catch (NotFoundException e) {

        }
    }

    protected void removeImage(String imageName, String imageTag) {
        try {
            dockerClient.removeImageCmd(taggedImageName(imageName, imageTag)).withForce().exec()
        } catch (NotFoundException e) {

        }
    }

    protected void createContainer(String imageName, String imageTag, String containerName) {
        dockerClient.createContainerCmd(taggedImageName(imageName, imageTag)).withName(containerName).exec()
    }

    protected void startContainer(String containerName) {
        dockerClient.startContainerCmd(containerName).exec()
    }

    private String taggedImageName(String imageName, String imageTag) {
        return imageName + ":" + imageTag
    }

    protected void assertContainerRunning(Container container) {
        if(!container) {
            Assert.fail("The container was not launched")
        }
        assertThat(container.status).contains("Up")
    }

    protected void assertImagePulled(Container container) {
        assertThat(this.output.toString()).contains("pulled", container.image)
    }

    protected void assertImageNotPulled(Container container) {
        assertThat(this.output.toString()).doesNotContain("pulled")
    }

    protected void assertImageBuilt(Container container) {
        assertThat(this.output.toString()).contains("built")
    }

    protected void assertContainerCreated(Container container) {
        assertThat(this.output.toString()).contains("create container", ContainerUtil.getFirstContainerName(container))
    }

    protected void assertContainerNotCreated(Container container) {
        assertThat(this.output.toString()).doesNotContain("create container")
    }

    protected void assertContainerNotStarted(Container container) {
        assertThat(this.output.toString()).doesNotContain("already started")
    }
}

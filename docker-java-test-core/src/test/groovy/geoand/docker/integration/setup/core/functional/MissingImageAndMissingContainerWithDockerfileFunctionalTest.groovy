package geoand.docker.integration.setup.core.functional

import com.github.dockerjava.api.model.Container
import geoand.docker.integration.setup.core.launcher.AbstractFunctionalTest
import geoand.docker.integration.setup.core.launcher.Launcher
import org.junit.Test

/**
 * Created by gandrianakis on 1/12/2015.
 */
class MissingImageAndMissingContainerWithDockerfileFunctionalTest extends AbstractFunctionalTest {

    @Test
    public void test() {
        removeContainer(CONTAINER_NAME)
        removeImage(IMAGE, TAG)

        final Container container = doLaunch(CONTAINER_NAME, IMAGE, TAG, DOCKER_FILE)

        assertContainerRunning(container)
        assertImageNotPulled(container)
        assertImageBuilt(container)
        assertContainerCreated(container)
    }

}
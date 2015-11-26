package geoand.docker.integration.setup.core.functional

import com.github.dockerjava.api.model.Container
import geoand.docker.integration.setup.core.launcher.AbstractFunctionalTest
import org.junit.Test

/**
 * Created by gandrianakis on 1/12/2015.
 */
class ExistingImageAndRunningContainerFunctionalTest extends AbstractFunctionalTest {

    @Test
    public void test() {
        removeContainer(CONTAINER_NAME)
        removeImage(IMAGE, TAG)
        pullImage(IMAGE, TAG)
        createContainer(IMAGE, TAG, CONTAINER_NAME)

        final Container container = doLaunch(CONTAINER_NAME, IMAGE, TAG)

        assertContainerRunning(container)
        assertImageNotPulled(container)
        assertContainerNotCreated(container)
        assertContainerNotStarted(container)
    }

}
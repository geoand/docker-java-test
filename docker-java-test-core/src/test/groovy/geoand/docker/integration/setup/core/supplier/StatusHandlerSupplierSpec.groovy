package geoand.docker.integration.setup.core.supplier

import com.github.dockerjava.api.DockerClient
import geoand.docker.integration.setup.core.model.ContainerLaunchInfo
import geoand.docker.integration.setup.core.status.handler.StatusHandler
import geoand.docker.integration.setup.core.test.support.DummyStatusHandler
import org.mockito.internal.util.MockUtil
import spock.lang.Specification

/**
 * Created by gandrianakis on 26/11/2015.
 */
class StatusHandlerSupplierSpec extends Specification {

    def "mock strategy not configured"() {
        given:
            final def mockUtil = new MockUtil()

        expect:
            !mockUtil.isMock(StatusHandlerSupplier.get(DummyStatusHandler).get())
    }

    def "mock strategy configured"() {
        given:
            System.setProperty("statusHandler.useMock", "true")

        and:
            final def mockUtil = new MockUtil()

        expect:
            mockUtil.isMock(StatusHandlerSupplier.get(Dummy2StatusHandler).get())

        cleanup:
            System.clearProperty("statusHandler.useMock")
    }

    private class Dummy2StatusHandler implements StatusHandler {

        @Override
        void handle(DockerClient dockerClient, ContainerLaunchInfo containerLaunchInfo) {

        }
    }
}

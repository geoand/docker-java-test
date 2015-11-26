package geoand.docker.integration.setup.core.integration

import com.github.dockerjava.api.DockerClient
import com.github.tomakehurst.wiremock.junit.WireMockRule
import geoand.docker.integration.setup.core.launcher.Launcher
import geoand.docker.integration.setup.core.model.ContainerLaunchInfo
import geoand.docker.integration.setup.core.status.handler.NotRunningStatusHandler
import geoand.docker.integration.setup.core.status.handler.MissingStatusHandler
import geoand.docker.integration.setup.core.status.handler.StartedStatusHandler
import geoand.docker.integration.setup.core.supplier.StatusHandlerSupplier
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.internal.util.MockUtil

import static com.github.tomakehurst.wiremock.client.WireMock.*

/**
 * Created by gandrianakis on 26/11/2015.
 */
class HandlersInvokedStubDockerAPIIntegrationTest {

    static final int DOCKER_PORT = 8089
    static final String CONTAINER_NAME = "container"
    static final String IMAGE_NAME = "geoand/image"

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(DOCKER_PORT)

    final Launcher.Configuration configuration = new Launcher.Configuration(CONTAINER_NAME, IMAGE_NAME).dockerHostPort(DOCKER_PORT)

    @Before
    public void setUp() {
        System.setProperty("statusHandler.useMock", "true")
    }

    @After
    public void tearDown() {
        System.clearProperty("statusHandler.useMock")
    }

    @Test
    public void startedHandlerInvoked() {
        stubFor(get(urlPathEqualTo("/v1.19/containers/json"))
                .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [{
                             "Id": "id",
                             "Names":["/${CONTAINER_NAME}"],
                             "Image": "${IMAGE_NAME}",
                             "Command": "echo 1",
                             "Created": 1367854155,
                             "Status": "Up 4 hours",
                             "Ports": [{"PrivatePort": 2222, "PublicPort": 3333, "Type": "tcp"}],
                             "SizeRw": 12288,
                             "SizeRootFs": 0
                     }]
                """))
        )

        final StartedStatusHandler mockStartedStatusHandler = StatusHandlerSupplier.get(StartedStatusHandler).get()
        assert new MockUtil().isMock(mockStartedStatusHandler)


        configuration.launch()

        final ArgumentCaptor<ContainerLaunchInfo> captor = ArgumentCaptor.forClass(ContainerLaunchInfo)
        Mockito.verify(mockStartedStatusHandler).handle(Mockito.any(DockerClient), captor.capture())

        final ContainerLaunchInfo containerLaunchInfo = captor.value
        assert containerLaunchInfo.containerName == CONTAINER_NAME
    }

    @Test
    public void exitedHandlerInvoked() {
        stubFor(get(urlPathEqualTo("/v1.19/containers/json"))
                .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [{
                             "Id": "id",
                             "Names":["/${CONTAINER_NAME}"],
                             "Image": "${IMAGE_NAME}",
                             "Command": "echo 1",
                             "Created": 1367854155,
                             "Status": "Exited 3 minutes",
                             "SizeRw": 12288,
                             "SizeRootFs": 0
                     }]
                """))
        )

        final NotRunningStatusHandler mockExitedStatusHandler = StatusHandlerSupplier.get(NotRunningStatusHandler).get()
        assert new MockUtil().isMock(mockExitedStatusHandler)


        configuration.launch()

        final ArgumentCaptor<ContainerLaunchInfo> captor = ArgumentCaptor.forClass(ContainerLaunchInfo)
        Mockito.verify(mockExitedStatusHandler).handle(Mockito.any(DockerClient), captor.capture())

        final ContainerLaunchInfo containerLaunchInfo = captor.value
        assert containerLaunchInfo.containerName == CONTAINER_NAME
    }

    @Test
    public void missingHandlerInvoked() {
        stubFor(get(urlPathEqualTo("/v1.19/containers/json"))
                .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    []
                """))
        )

        final MissingStatusHandler mockMissingStatusHandler = StatusHandlerSupplier.get(MissingStatusHandler).get()
        assert new MockUtil().isMock(mockMissingStatusHandler)


        configuration.launch()

        final ArgumentCaptor<ContainerLaunchInfo> captor = ArgumentCaptor.forClass(ContainerLaunchInfo)
        Mockito.verify(mockMissingStatusHandler).handle(Mockito.any(DockerClient), captor.capture())

        final ContainerLaunchInfo containerLaunchInfo = captor.value
        assert containerLaunchInfo.containerName == CONTAINER_NAME
    }
}

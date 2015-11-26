package geoand.docker.integration.setup.core.launcher

import com.github.dockerjava.api.DockerClient
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.verification.LoggedRequest
import geoand.docker.integration.setup.core.launcher.strategy.LaunchStrategy
import geoand.docker.integration.setup.core.model.ContainerLaunchInfo
import groovy.json.JsonSlurper
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import static com.github.tomakehurst.wiremock.client.WireMock.*

/**
 * Created by gandrianakis on 2/12/2015.
 */
class PortBindingStubDockerAPIIntegrationTest {

    static final int DOCKER_PORT = 8089
    static final String CONTAINER_NAME = "container"
    static final String IMAGE_NAME = "geoand/image"
    static final int PORT_1 = 44444
    static final int PORT_2_CONTAINER = 55555
    static final int PORT_2_HOST = 55554

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(DOCKER_PORT)

    LaunchStrategy launchStrategy

    Launcher.Configuration configuration

    final JsonSlurper jsonSlurper = new JsonSlurper()

    @Before
    public void setUp() {
        launchStrategy = { DockerClient dockerClient, ContainerLaunchInfo containerLaunchInfo ->
            dockerClient.createContainerCmd(containerLaunchInfo.containerName)
                .withImage(containerLaunchInfo.imageBuildInfo.taggedImageName)
                .withName(containerLaunchInfo.containerName)
                .withPortBindings(containerLaunchInfo.portBindingsArray)
                .exec()
        } as LaunchStrategy

        configuration =
                new Launcher.Configuration(CONTAINER_NAME, IMAGE_NAME)
                        .dockerHostPort(DOCKER_PORT)
                        .portBinding(PORT_1)
                        .portBinding(PORT_2_HOST, PORT_2_CONTAINER)
                        .launchStrategy(launchStrategy)
    }

    @Test
    void test() {
        stubFor(post(urlPathEqualTo("/v1.19/containers/create"))
                .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "Id":"e90e34656806",
                        "Warnings":[]
                    }
                """))
        )

        configuration.launch()

        final List<LoggedRequest> createRequests = findAll(postRequestedFor(urlPathEqualTo("/v1.19/containers/create")))
        assert createRequests.size() == 1

        def json = jsonSlurper.parse(createRequests[0].body)
        assert json."HostConfig"."PortBindings"?."${PORT_1}/tcp"[0]?."HostPort" == Integer.toString(PORT_1)
        assert json."HostConfig"."PortBindings"?."${PORT_2_CONTAINER}/tcp"[0]?."HostPort" == Integer.toString(PORT_2_HOST)
    }
}

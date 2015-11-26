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
class CommandStubDockerAPIIntegrationTest {

    static final int DOCKER_PORT = 8089
    static final String CONTAINER_NAME = "container"
    static final String IMAGE_NAME = "geoand/image"
    static final String START_COMMAND = "start.sh 1234"

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
                .withCmd(containerLaunchInfo.command)
                .exec()
        } as LaunchStrategy

        configuration =
                new Launcher.Configuration(CONTAINER_NAME, IMAGE_NAME)
                        .dockerHostPort(DOCKER_PORT)
                        .command(START_COMMAND)
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
        assert json."Cmd"[0] == START_COMMAND
    }
}

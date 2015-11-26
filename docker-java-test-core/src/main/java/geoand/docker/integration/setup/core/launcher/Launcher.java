package geoand.docker.integration.setup.core.launcher;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import geoand.docker.integration.setup.core.client.DockerClientCreator;
import geoand.docker.integration.setup.core.client.LocalPortDockerClientCreator;
import geoand.docker.integration.setup.core.client.UnixSocketDockerClientCreator;
import geoand.docker.integration.setup.core.launcher.strategy.DefaultLaunchStrategy;
import geoand.docker.integration.setup.core.launcher.strategy.LaunchStrategy;
import geoand.docker.integration.setup.core.model.ContainerLaunchInfo;
import geoand.docker.integration.setup.core.model.ImageBuildInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gandrianakis on 23/11/2015.
 */
public abstract class Launcher {

    final static Logger log = LoggerFactory.getLogger(Launcher.class);

    public static class Configuration {
        private final ImageBuildInfo.Builder imageBuildInfoBuilder;
        private final String containerName;
        private String command = null;
        private final List<PortBinding> portBindings = new ArrayList<>();

        private DockerClientCreator dockerClientCreator = new UnixSocketDockerClientCreator();

        private LaunchStrategy launchStrategy = new DefaultLaunchStrategy();

        public Configuration(String containerName, String imageName) {
            this.imageBuildInfoBuilder = new ImageBuildInfo.Builder(imageName);
            this.containerName = containerName;
        }

        public Configuration dockerHostPort(int port) {
            dockerClientCreator = new LocalPortDockerClientCreator(port);
            return this;
        }

        public Configuration imageTag(String imageTag) {
            if(!Strings.isNullOrEmpty(imageTag)) {
                imageBuildInfoBuilder.tag(imageTag);
            }
            return this;
        }

        /**
         * Convenience method for adding a mapping a TCP port on the host's default IP to the same port of the container
         */
        public Configuration portBinding(int containerPort) {
            portBindings.add(new PortBinding(new Ports.Binding(containerPort), new ExposedPort(containerPort)));
            return this;
        }

        /**
         * Convenience method for adding a mapping a TCP port on the host's default IP to the specified port of the container
         */
        public Configuration portBinding(int hostPort, int containerPort) {
            portBindings.add(new PortBinding(new Ports.Binding(hostPort), new ExposedPort(containerPort)));
            return this;
        }

        /**
         * Full featured port binding
         */
        public Configuration portBinding(PortBinding portBinding) {
            portBindings.add(portBinding);
            return this;
        }

        public Configuration command(String command) {
            this.command = command;
            return this;
        }

        public Configuration dockerfileDirectoryClasspath(String dockerfileDirectory) {
            try {
                imageBuildInfoBuilder.dockerfileDirectory(new File(Thread.currentThread().getContextClassLoader().getResource(dockerfileDirectory).getFile()));
            } catch (Exception e) {
                log.error("Unable to use classpath directory: '{}' to build image from Dockerfile", dockerfileDirectory, e);
                throw e;
            }
            return this;
        }

        public Configuration dockerfileDirectorySystem(String dockerfileDirectory) {
            final File file = new File(dockerfileDirectory);
            if(!file.exists() || file.isDirectory()) {
                log.error("Unable to use system directory: '{}' to build image from Dockerfile", dockerfileDirectory);
                throw new RuntimeException("Unable to use file system directory " + dockerfileDirectory + " to build image");
            }

            imageBuildInfoBuilder.dockerfileDirectory(file);
            return this;
        }

        @VisibleForTesting
        Configuration launchStrategy(LaunchStrategy launchStrategy) {
            this.launchStrategy = launchStrategy;
            return this;
        }

        @VisibleForTesting
        DockerClient createClient() {
            return dockerClientCreator.create();
        }

        public void launch() {
            launchStrategy.launch(dockerClientCreator.create(), new ContainerLaunchInfo(containerName, imageBuildInfoBuilder.build(), command, portBindings));
        }
    }


}

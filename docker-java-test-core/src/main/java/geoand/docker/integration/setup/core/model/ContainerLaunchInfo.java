package geoand.docker.integration.setup.core.model;

import com.github.dockerjava.api.model.PortBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gandrianakis on 23/11/2015.
 */
public class ContainerLaunchInfo {

    final String containerName;
    final ImageBuildInfo imageBuildInfo;
    final String command;
    final List<PortBinding> portBindings;

    public ContainerLaunchInfo(String containerName, ImageBuildInfo imageBuildInfo, String command, List<PortBinding> portBindings) {
        this.containerName = containerName;
        this.imageBuildInfo = imageBuildInfo;
        this.command = command;
        this.portBindings = (null == portBindings ? new ArrayList<PortBinding>() : portBindings);
    }

    public String getContainerName() {
        return containerName;
    }

    public String getCanonicalContainerName() {
        return "/" + getContainerName();
    }

    public String getCommand() {
        return command;
    }

    public ImageBuildInfo getImageBuildInfo() {
        return imageBuildInfo;
    }

    public List<PortBinding> getPortBindings() {
        return portBindings;
    }

    public PortBinding[] getPortBindingsArray() {
        return portBindings.toArray(new PortBinding[portBindings.size()]);
    }
}

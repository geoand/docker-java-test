package geoand.docker.integration.setup.core.status.model.internal;

/**
 * Created by gandrianakis on 26/11/2015.
 */
public interface DockerStatusMatch {

    boolean matches(String dockerStatusStr);
}

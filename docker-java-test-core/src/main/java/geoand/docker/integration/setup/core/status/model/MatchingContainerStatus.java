package geoand.docker.integration.setup.core.status.model;

import geoand.docker.integration.setup.core.status.model.internal.DockerStatusMatch;

/**
 * Created by gandrianakis on 26/11/2015.
 */
public interface MatchingContainerStatus<T extends ContainerStatus<T>> extends ContainerStatus<T>, DockerStatusMatch {
}

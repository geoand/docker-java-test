package geoand.docker.integration.setup.core.status.model.internal;

import com.google.common.base.Predicate;
import geoand.docker.integration.setup.core.status.model.MatchingContainerStatus;

/**
 * Created by gandrianakis on 23/11/2015.
 */
public abstract class MatchingPredicateContainerStatus<T extends MatchingPredicateContainerStatus<T>> implements MatchingContainerStatus<T> {


    abstract Predicate<String> getPredicate();

    @Override
    public boolean matches(String dockerStatusStr) {
        return getPredicate().apply(dockerStatusStr);
    }
}

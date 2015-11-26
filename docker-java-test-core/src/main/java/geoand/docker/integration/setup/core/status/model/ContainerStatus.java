package geoand.docker.integration.setup.core.status.model;

import com.google.common.base.Supplier;
import geoand.docker.integration.setup.core.status.handler.StatusHandler;

/**
 * Created by gandrianakis on 24/11/2015.
 */
public interface ContainerStatus<T extends ContainerStatus<T>> {

    Supplier<StatusHandler<T>> getHandlerSupplier();
}

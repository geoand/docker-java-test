package geoand.docker.integration.setup.core.supplier;

import com.google.common.base.Supplier;
import geoand.docker.integration.setup.core.status.handler.StatusHandler;
import geoand.docker.integration.setup.core.status.model.ContainerStatus;

/**
 * Created by gandrianakis on 26/11/2015.
 */
public interface StatusHandlerClassInstantiationStrategy {

    <STATUS_T extends ContainerStatus<STATUS_T>, HANDLER_T extends StatusHandler<STATUS_T>> Supplier<StatusHandler<STATUS_T>> instantiate(Class<HANDLER_T> handlerClass);
}

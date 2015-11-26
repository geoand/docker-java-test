package geoand.docker.integration.setup.core.supplier;

import com.google.common.base.Supplier;
import geoand.docker.integration.setup.core.status.handler.StatusHandler;
import geoand.docker.integration.setup.core.status.model.ContainerStatus;
import org.mockito.Mockito;

/**
 * Created by gandrianakis on 26/11/2015.
 *
 * This class provides mock implementations of each Handler
 * The class is never used directly, but instead is instantiated
 * only if it's present on the class path.
 * @see geoand.docker.integration.setup.core.supplier.StatusHandlerSupplier
 */
public class MockStatusHandlerSupplierStrategy implements StatusHandlerClassInstantiationStrategy{

    @Override
    public <STATUS_T extends ContainerStatus<STATUS_T>, HANDLER_T extends StatusHandler<STATUS_T>> Supplier<StatusHandler<STATUS_T>> instantiate(final Class<HANDLER_T> handlerClass) {
        return new Supplier<StatusHandler<STATUS_T>>() {
            @Override
            public StatusHandler<STATUS_T> get() {
                return Mockito.mock(handlerClass);
            }
        };
    }
}

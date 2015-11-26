package geoand.docker.integration.setup.core.supplier;

import com.google.common.base.Supplier;
import geoand.docker.integration.setup.core.status.handler.StatusHandler;
import geoand.docker.integration.setup.core.status.model.ContainerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gandrianakis on 26/11/2015.
 */
public class NoArgsConstructorStatusHandlerClassInstantiationStrategy implements StatusHandlerClassInstantiationStrategy {

    final static Logger log = LoggerFactory.getLogger(StatusHandlerSupplier.class);

    @Override
    public <STATUS_T extends ContainerStatus<STATUS_T>, HANDLER_T extends StatusHandler<STATUS_T>> Supplier<StatusHandler<STATUS_T>> instantiate(final Class<HANDLER_T> handlerClass) {
        return new Supplier<StatusHandler<STATUS_T>>() {
            @Override
            public StatusHandler<STATUS_T> get() {
                try {
                    return handlerClass.newInstance();
                } catch (Exception e) {
                    log.error("Unable to create Handler class for " + handlerClass.getSimpleName());
                    throw new RuntimeException(e);
                }
            }
        };
    }
}

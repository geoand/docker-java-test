package geoand.docker.integration.setup.core.status.model;

import com.google.common.base.Supplier;
import geoand.docker.integration.setup.core.status.handler.MissingStatusHandler;
import geoand.docker.integration.setup.core.status.handler.StatusHandler;
import geoand.docker.integration.setup.core.supplier.StatusHandlerSupplier;

/**
 * Created by gandrianakis on 26/11/2015.
 */
public class MissingContainerStatus implements ContainerStatus<MissingContainerStatus>{

    @Override
    public Supplier<StatusHandler<MissingContainerStatus>> getHandlerSupplier() {
        return StatusHandlerSupplier.get(MissingStatusHandler.class);
    }
}

package geoand.docker.integration.setup.core.status.model;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import geoand.docker.integration.setup.core.status.handler.StartedStatusHandler;
import geoand.docker.integration.setup.core.status.handler.StatusHandler;
import geoand.docker.integration.setup.core.status.model.internal.AbstractStartsWithMatchingNameContainerStatus;
import geoand.docker.integration.setup.core.supplier.StatusHandlerSupplier;

import java.util.Set;

/**
 * Created by gandrianakis on 23/11/2015.
 */
public class StartedContainerStatus extends AbstractStartsWithMatchingNameContainerStatus<StartedContainerStatus> {


    @Override
    protected Set<String> getMatchingNames() {
        return Sets.newHashSet("started", "up");
    }


    @Override
    public Supplier<StatusHandler<StartedContainerStatus>> getHandlerSupplier() {
        return StatusHandlerSupplier.get(StartedStatusHandler.class);
    }
}

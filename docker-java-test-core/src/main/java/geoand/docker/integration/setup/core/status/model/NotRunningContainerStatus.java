package geoand.docker.integration.setup.core.status.model;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import geoand.docker.integration.setup.core.status.handler.NotRunningStatusHandler;
import geoand.docker.integration.setup.core.status.handler.StatusHandler;
import geoand.docker.integration.setup.core.status.model.internal.AbstractStartsWithMatchingNameContainerStatus;
import geoand.docker.integration.setup.core.supplier.StatusHandlerSupplier;

import java.util.Set;

/**
 * Created by gandrianakis on 23/11/2015.
 */
public class NotRunningContainerStatus extends AbstractStartsWithMatchingNameContainerStatus<NotRunningContainerStatus> {


    @Override
    protected Set<String> getMatchingNames() {
        return Sets.newHashSet("exited", "stopped", "created");
    }


    @Override
    public Supplier<StatusHandler<NotRunningContainerStatus>> getHandlerSupplier() {
        return StatusHandlerSupplier.get(NotRunningStatusHandler.class);
    }
}

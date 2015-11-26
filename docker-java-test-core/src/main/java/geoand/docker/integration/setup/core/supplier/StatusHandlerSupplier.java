package geoand.docker.integration.setup.core.supplier;

import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import geoand.docker.integration.setup.core.status.handler.StatusHandler;
import geoand.docker.integration.setup.core.status.model.ContainerStatus;
import geoand.docker.integration.setup.core.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * Created by gandrianakis on 25/11/2015.
 */
public abstract class StatusHandlerSupplier {

    private static final String MOCK_STRATEGY_CLASS_NAME = "geoand.docker.integration.setup.core.supplier.MockStatusHandlerSupplierStrategy";

    private final static Logger log = LoggerFactory.getLogger(StatusHandlerSupplier.class);

    private static final StatusHandlerClassInstantiationStrategy defaultStrategy = new NoArgsConstructorStatusHandlerClassInstantiationStrategy();

    /**
     * The following elaborate construct ensures that no matter how many times get() is called,
     * when the arguments are the same, the exact same StatusHandler is returned
     */
    private static final LoadingCache<Class<? extends StatusHandler>, Supplier<? extends StatusHandler<? extends ContainerStatus>>>
            statusHandlerCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<Class<? extends StatusHandler>, Supplier<? extends StatusHandler<? extends ContainerStatus>>>() {
                @Override
                public Supplier<? extends StatusHandler<? extends ContainerStatus>> load(Class<? extends StatusHandler> handlerClass) throws Exception {
                    log.debug("Loading cache invoked for " + handlerClass.getSimpleName());

                    return Suppliers.memoize(getInstantiationStrategy().instantiate(handlerClass));
                }
            });

    @SuppressWarnings("unchecked")
    public static <STATUS_T extends ContainerStatus<STATUS_T>, HANDLER_T extends StatusHandler<STATUS_T>> Supplier<StatusHandler<STATUS_T>> get(final Class<HANDLER_T> handlerClass) {

        try {
            if(isStatusHandlerCachingEnabled()) {
                log.debug("StatusHandler caching was enabled so returning a supplier that will always return the same value when invoked with class" + handlerClass);
                return (Supplier<StatusHandler<STATUS_T>>) statusHandlerCache.get(handlerClass);
            }

            return getInstantiationStrategy().instantiate(handlerClass);
        } catch (ExecutionException e) {
            log.error("Unable to load StatusHandler");
            throw new RuntimeException(e);
        }
    }

    private static StatusHandlerClassInstantiationStrategy getInstantiationStrategy() {
        final ClassLoader classLoader = StatusHandlerSupplier.class.getClassLoader();

        if(ClassUtil.isPresent(MOCK_STRATEGY_CLASS_NAME, classLoader) && isMockStrategyConfigured()) {
            return useMockStrategy(MOCK_STRATEGY_CLASS_NAME, classLoader);
        }

        //TODO add other types of strategies if needed
        return defaultStrategy;
    }

    private static boolean isMockStrategyConfigured() {
        return Boolean.parseBoolean(System.getProperty("statusHandler.useMock"));
    }

    private static boolean isStatusHandlerCachingEnabled() {
        final String useStatusHandlerCachingStrValue = System.getProperty("statusHandler.useCaching");
        return Strings.isNullOrEmpty(useStatusHandlerCachingStrValue) || Boolean.parseBoolean(useStatusHandlerCachingStrValue);
    }

    private static StatusHandlerClassInstantiationStrategy useMockStrategy(String mockStrategyClassName, ClassLoader classLoader) {
        log.debug(String.format("Mock status handler configuration was enabled, so attempting to return `%s`", MOCK_STRATEGY_CLASS_NAME));
        try {
            return (StatusHandlerClassInstantiationStrategy) Class.forName(mockStrategyClassName, true, classLoader).newInstance();
        } catch (Exception e) {
            log.error("Unable to load " + mockStrategyClassName);
            throw new RuntimeException(e);
        }
    }

}

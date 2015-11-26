package geoand.docker.integration.setup.core.status.model.internal;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.util.Set;

/**
 * Created by gandrianakis on 24/11/2015.
 */
public abstract class AbstractStartsWithMatchingNameContainerStatus<T extends AbstractStartsWithMatchingNameContainerStatus<T>> extends MatchingPredicateContainerStatus<T> {

    protected abstract Set<String> getMatchingNames();

    private Supplier<Predicate<String>> predicateSupplier = Suppliers.memoize(new Supplier<Predicate<String>>() {
        @Override
        public Predicate<String> get() {
            return new Predicate<String>() {
                @Override
                public boolean apply(String input) {
                    if(null == input) {
                        return false;
                    }

                    final String lowerCase = input.toLowerCase();

                    for (String candidateName : getMatchingNames()) {
                        if(lowerCase.startsWith(candidateName.toLowerCase())) {
                            return true;
                        }
                    }

                    return false;
                }
            };
        }
    });

    @Override
    Predicate<String> getPredicate() {
        return predicateSupplier.get();
    }
}

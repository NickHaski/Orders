package com.phone.orders.core;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Converter<SOURCE, TARGET>  {
    TARGET convert(final SOURCE source);
    default Collection<TARGET> convert(final Collection<SOURCE> sources) {
        return sources.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
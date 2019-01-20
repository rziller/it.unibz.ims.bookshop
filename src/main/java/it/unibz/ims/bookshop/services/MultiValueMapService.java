package it.unibz.ims.bookshop.services;

import org.springframework.util.MultiValueMap;

import java.util.*;

public class MultiValueMapService {

    public static <S, T> Optional<T> getFirstParamaterValue(MultiValueMap<S, T> map, String parameterName) {
        T result = null;
        List<T> paramaterValues = map.get(parameterName);
        return (paramaterValues.size() > 0) ? Optional.ofNullable( paramaterValues.get(0) ) : Optional.ofNullable(result);
    }
}

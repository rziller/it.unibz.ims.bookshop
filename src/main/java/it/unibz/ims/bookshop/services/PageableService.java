package it.unibz.ims.bookshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PageableService {

    @Autowired
    private Environment env;

    public PageableService() { }

    public Pageable createPageRequest(Map<String, String> queryParameters) {
        int page = Integer.valueOf( queryParameters.getOrDefault("page", "0") );
        int size = Integer.valueOf( queryParameters.getOrDefault("size", "0") );

        if (page <= 0) page = 0;
        if (size <= 0) size = Integer.valueOf( env.getProperty("bookshop.product-list.default-page-size") );

        return PageRequest.of(page, size);
    }
}

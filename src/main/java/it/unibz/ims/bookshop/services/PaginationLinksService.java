package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.models.PaginationLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaginationLinksService {

    @Autowired
    PageableService pageableService;

    public PaginationLinksService() {}

    public List<PaginationLink> create(int totalPages, String requestURL, Map<String, String> queryParameters) {
        Pageable pageable = pageableService.createPageRequest(queryParameters);
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        List<PaginationLink> paginationLinks = this.composePaginationLinks(currentPage, pageSize,
                totalPages, requestURL, queryParameters);

        return paginationLinks;
    }

    private ArrayList<PaginationLink> composePaginationLinks(int currentPage, int pageSize, int totalPages,
                                                             String requestURL, Map<String, String> queryParameters) {
        ArrayList<PaginationLink> links = new ArrayList<>();
        
        if (currentPage > 0) {
            PaginationLink prevLink = new PaginationLink();
            prevLink.setPrevious(true);
            prevLink.setText("Previous");
            prevLink.setHref( this.composeHrefAttributeValue(currentPage - 1, pageSize, requestURL, queryParameters) );
            links.add(prevLink);
        }

        for (int i = 0; i < totalPages; i++)  {
            PaginationLink paginationLink = new PaginationLink();
            paginationLink.setText( String.valueOf(i + 1) );
            paginationLink.setHref( this.composeHrefAttributeValue(i, pageSize, requestURL, queryParameters) );
            links.add(paginationLink);
        }

        if (currentPage < (totalPages - 1)) {
            PaginationLink prevLink = new PaginationLink();
            prevLink.setNext(true);
            prevLink.setText("Next");
            prevLink.setHref( this.composeHrefAttributeValue(currentPage + 1, pageSize, requestURL, queryParameters) );
            links.add(prevLink);
        }

        return links;
    }

    private String composeHrefAttributeValue(int page, int size, String requestURL, Map<String, String> queryParameters) {
        if (queryParameters.containsKey("page")) {
            queryParameters.replace("page", String.valueOf(page));
        } else {
            queryParameters.put("page", String.valueOf(page));
        }

        if (queryParameters.containsKey("size")) {
            queryParameters.replace("size", String.valueOf(size));
        } else {
            queryParameters.put("size", String.valueOf(size));
        }

        String queryParametersString = "";

        for( String key : queryParameters.keySet() ) {
            queryParametersString += key + "=" + queryParameters.get(key) + "&";
        }

        queryParametersString = queryParametersString.replaceAll("&$", "");

        return (queryParametersString != "") ? requestURL + "?" + queryParametersString : requestURL;
    }
}

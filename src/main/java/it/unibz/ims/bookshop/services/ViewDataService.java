package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.models.PaginationLink;
import it.unibz.ims.bookshop.models.ViewData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ViewDataService<T> {

    @Autowired
    private PaginationLinksService paginationLinksService;

    public ViewData createWithoutPagination(T dbResult) {
        ViewData<T> viewData = new ViewData();
        viewData.setPaginationLinks(null);
        viewData.setContent(dbResult);
        return viewData;
    }

    public ViewData createWithoutPagination(Optional<T> dbResult) {
        ViewData<T> viewData = new ViewData();
        viewData.setPaginationLinks(null);

        if ( dbResult.isPresent() )
        {
            viewData.setContent( dbResult.get() );
        }
        else
        {
            viewData.setContent(null);
        }

        return viewData;
    }

    public ViewData createWithoutPagination(List<T> dbResultList) {
        ViewData<List<T>> viewData = new ViewData();
        viewData.setContent(dbResultList);
        viewData.setPaginationLinks(null);
        return viewData;
    }

    public ViewData createWithoutPagination(Page<T> dbResultPage) {
        ViewData<List<T>> viewData = new ViewData();
        viewData.setContent( dbResultPage.getContent() );
        viewData.setPaginationLinks(null);
        return viewData;
    }

    public ViewData createWithPagination(Page<T> dbResultPage, HttpServletRequest request, Map<String, String> queryParameters) {
        ViewData<List<T>> viewData = new ViewData();

        List<PaginationLink> paginationLinks = paginationLinksService.create(
                dbResultPage.getTotalPages(),
                request.getRequestURL().toString(),
                queryParameters
        );

        viewData.setContent( dbResultPage.getContent() );
        viewData.setPaginationLinks(paginationLinks);
        return viewData;
    }
}

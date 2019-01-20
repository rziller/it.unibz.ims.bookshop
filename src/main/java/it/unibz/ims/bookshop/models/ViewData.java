package it.unibz.ims.bookshop.models;

import java.util.List;

public class ViewData<T> {
    private T content;
    private List<PaginationLink> paginationLinks;
    private int shoppingCartSize;

    public ViewData() {}

    public T getContent() {
        return this.content;
    }

    public void setContent(T data) {
        this.content = data;
    }

    public List<PaginationLink> getPaginationLinks() {
        return this.paginationLinks;
    }

    public void setPaginationLinks(List<PaginationLink> paginationLinks) {
        this.paginationLinks = paginationLinks;
    }

    public int getShoppingCartSize() {
        return this.shoppingCartSize;
    }

    public void setShoppingCartSize(int shoppingCartSize) {
        this.shoppingCartSize = shoppingCartSize;
    }
}

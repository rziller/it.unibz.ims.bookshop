package it.unibz.ims.bookshop.models;

public class PaginationLink {
    private String href;
    private String text;
    private boolean isPrevious;
    private boolean isNext;

    public PaginationLink() {

    }

    public String getHref() {
        return this.href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isPrevious() {
        return this.isPrevious;
    }

    public void setPrevious(boolean previous) {
        isPrevious = previous;
    }

    public boolean isNext() {
        return this.isNext;
    }

    public void setNext(boolean next) {
        isNext = next;
    }
}

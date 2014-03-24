package org.n3r.es.query;

public class EsQueryPage {

    private int current = 1;

    private int size = EsQueryHelper.DEFAULT_SIZE;

    private int total;

    public EsQueryPage() {
    }

    public EsQueryPage(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPages() {
        int totalPages = size <= 0 ? 1 : (total + size - 1) / size;
        return totalPages < 1 ? 1 : totalPages;
    }

    public int calcStartIndex() {
        if (current < 1) current = 1;
        int totalPages = getTotalPages();
        if (current > totalPages) current = totalPages;
        return size <= 0 ? 0 : (current - 1) * size;
    }

}

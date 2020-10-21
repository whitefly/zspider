package simples;

public class PageItem {
    String title;
    String context;

    public PageItem(String title, String context) {
        this.title = title;
        this.context = context;
    }

    @Override
    public String toString() {
        return "PageItem{" +
                "title='" + title + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}

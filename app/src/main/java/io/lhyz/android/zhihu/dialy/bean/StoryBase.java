package io.lhyz.android.zhihu.dialy.bean;

public class StoryBase {
    protected String id;
    protected String ga_prefix;
    protected String title;

    public String getId() {
        return id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

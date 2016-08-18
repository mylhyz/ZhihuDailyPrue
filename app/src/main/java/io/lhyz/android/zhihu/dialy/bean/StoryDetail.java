package io.lhyz.android.zhihu.dialy.bean;

public class StoryDetail {
    private String body;
    private String image_source;
    private String title;
    private String image;
    private String id;
    private String share_url;
    private String[] css;

    public String getBody() {
        return body;
    }

    public String getImage_source() {
        return image_source;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String[] getCss() {
        return css;
    }

    public void setCss(String[] css) {
        this.css = css;
    }
}

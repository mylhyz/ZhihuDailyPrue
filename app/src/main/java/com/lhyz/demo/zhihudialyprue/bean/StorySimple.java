package com.lhyz.demo.zhihudialyprue.bean;

import java.io.Serializable;

public class StorySimple  implements Serializable {
    private String image;
    private String title;
    private String id;

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }
}

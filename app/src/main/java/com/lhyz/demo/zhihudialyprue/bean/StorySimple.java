package com.lhyz.demo.zhihudialyprue.bean;

import java.io.Serializable;

public class StorySimple  implements Serializable {
    private String image;
    private String title;

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

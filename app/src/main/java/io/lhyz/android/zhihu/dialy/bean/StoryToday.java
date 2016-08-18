package io.lhyz.android.zhihu.dialy.bean;

public class StoryToday extends StoryBase {

    private String[] images;
    private String multipic;

    public String[] getImages() {
        return images;
    }

    public String getMultipic() {
        return multipic;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public void setMultipic(String multipic) {
        this.multipic = multipic;
    }
}

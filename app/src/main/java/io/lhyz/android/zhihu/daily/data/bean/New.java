/*
 * Copyright (c) 2016 lhyz Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lhyz.android.zhihu.daily.data.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class New {
    private String body;
    @SerializedName("image_source")
    private
    String imageSource;
    private String title;
    private String image;
    @SerializedName("share_url")
    private
    String shareURL;
    private List<Avatar> recommenders;
    private int type;
    private long id;
    @SerializedName("css")
    private
    List<String> cssURL;

    public New(String body, String imageSource, String title, String image,
               String shareURL, List<Avatar> recommenders, int type, long id,
               List<String> cssURL) {
        this.body = body;
        this.imageSource = imageSource;
        this.title = title;
        this.image = image;
        this.shareURL = shareURL;
        this.recommenders = recommenders;
        this.type = type;
        this.id = id;
        this.cssURL = cssURL;
    }

    public String getBody() {
        return body;
    }

    public String getImageSource() {
        return imageSource;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getShareURL() {
        return shareURL;
    }

    public List<Avatar> getRecommenders() {
        return recommenders;
    }

    public int getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public List<String> getCssURL() {
        return cssURL;
    }
}

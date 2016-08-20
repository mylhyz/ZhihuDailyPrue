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
package io.lhyz.android.zhihu.dialy.data.bean;

import java.util.List;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class Story {
    String title;
    //For Normal Story
    List<String> images;
    //For TopStory
    String image;
    int type;
    long id;

    public Story(String title, List<String> images, String image, int type, long id) {
        this.title = title;
        this.images = images;
        this.image = image;
        this.type = type;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getImages() {
        return images;
    }

    public String getImage() {
        return image;
    }

    public int getType() {
        return type;
    }

    public long getId() {
        return id;
    }
}

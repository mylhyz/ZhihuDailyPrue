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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * hello,android
 * Created by lhyz on 2016/8/28.
 */
@DatabaseTable(tableName = "normal")
public class Normal {
    @DatabaseField
    String title;
    //For Normal Story
    List<String> images;
    @DatabaseField
    String image;
    @DatabaseField
    int type;
    @DatabaseField(id = true)
    long id;

    @DatabaseField
    String date;

    public Normal() {
        //OrmLite
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

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

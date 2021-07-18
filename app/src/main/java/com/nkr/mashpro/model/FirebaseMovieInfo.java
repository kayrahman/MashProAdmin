package com.nkr.mashpro.model;



public class FirebaseMovieInfo {
    String video_url;
    String video_ref;
    String img_url;
    String movie_title;
    String movie_year;
    String description;
    String type;
    String tag;
    String creator;

    public FirebaseMovieInfo(){}




    public FirebaseMovieInfo(String video_url, String video_ref, String img_url,
                             String movie_title, String movie_year, String description,
                             String type, String tag, String creator
                             ){

        this.video_url = video_url;
        this.video_ref = video_ref;
        this.img_url = img_url;
        this.movie_title = movie_title;
        this.movie_year = movie_year;
        this.description = description;
        this.type = type;
        this.tag = tag;
        this.creator = creator;

    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_ref() {
        return video_ref;
    }

    public void setVideo_ref(String video_ref) {
        this.video_ref = video_ref;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getMovie_year() {
        return movie_year;
    }

    public void setMovie_year(String movie_year) {
        this.movie_year = movie_year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

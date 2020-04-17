package com.iocl.lpg.customer.imageSliderView.view;

import java.io.Serializable;

public class News implements Serializable  {
    @SuppressWarnings("compatibility:-9165012123361881074")
    private static final long serialVersionUID = 1L;

    public News() {
        super();
    }
        private String news;

    public void setNews(String news) {
        this.news = news;
    }

    public String getNews() {
        return news;
    }
    

    public News(String news) {
        this.news = news;
    }
}

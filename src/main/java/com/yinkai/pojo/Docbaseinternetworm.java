package com.yinkai.pojo;

import java.io.Serializable;

public class Docbaseinternetworm implements Serializable{

    private String id;
    private String title;
    private String content;
    private String date;
    private String author;
    private int views;
    private String source;
    private String url;
    private String md5;

    public Docbaseinternetworm() {
    }

    public Docbaseinternetworm(String id, String title, String content, String date, String source, String url) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.source = source;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getViews() {
        return views;
    }
    public void setViews(int views) {
        this.views = views;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    @Override
    public String toString() {
        return "Doc [id=" + id + ", title=" + title + ", content=" + content
                + ", date=" + date + ", author=" + author + ", views=" + views
                + ", source=" + source + "]";
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}

package com.cc.milkalbum.model;

import com.cc.milkalbum.utils.ConstantUtils;

/**
 * Created by Administrator on 2017/2/7.
 */
public class Girl {
    private int id;
    private String authorName;
    private String authorHeadImg;
    private String title;
    private String catalog;
    private String issue;
    private int pictureCount;

    public Girl(){
        this.id = 0;
        this.authorName = "test name";
        this.authorHeadImg = "";
        this.catalog = "";
        this.title = "test title";
        this.issue = "";
        this.pictureCount = 0;
    }

    public Girl(int id, String authorName, String authorHeadImg, String title, String catalog, String issue, int pictureCount){
        this.id = id;
        this.authorName = authorName;
        this.authorHeadImg = authorHeadImg;
        this.title = title;
        this.catalog = catalog;
        this.issue = issue;
        this.pictureCount = pictureCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorHeadImg() {
        return authorHeadImg;
    }

    public void setAuthorHeadImg(String authorHeadImg) {
        this.authorHeadImg = authorHeadImg;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public int getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(int pictureCount) {
        this.pictureCount = pictureCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl(int index){
        if(index >= pictureCount)
            return "";
        return ConstantUtils.PictureUrlHeader + catalog + "/" + issue + "/" + index + ".jpg";
    }
}

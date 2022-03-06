package com.example.yourquerybuddy;

public class ProjectList {

    String title;
    String description;

    String projectLink;

    Integer count;

    public ProjectList(){}

    public ProjectList(String title, String description,Integer count,String projectLink) {
        this.title = title;
        this.description = description;
        this.count=count;
        this.projectLink=projectLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCount() {
        return count;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setLink(String projectLink) {
        this.projectLink = projectLink;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

}

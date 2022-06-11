package com.example.launchdinner;

public class Board { // BoardVO
    long id;
    String title;
    String content;
    String localDateTime;
    String comusermVO;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getComusermVO() {
        return comusermVO;
    }

    public void setComusermVO(String comusermVO) {
        this.comusermVO = comusermVO;
    }
}

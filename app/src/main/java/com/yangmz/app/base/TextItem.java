package com.yangmz.app.base;

public class TextItem {
    Long imageId = null;
    String imageAddress = null;
    String text = null;
    public TextItem(Long imageId, String text){
        this.imageId = imageId;
        this.text = text;
    }
    public TextItem(){
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    @Override
    public String toString() {
        return "TextItem{" +
                "imageId=" + imageId +
                ", imageAddress='" + imageAddress + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

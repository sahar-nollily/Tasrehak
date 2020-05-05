package com.ehelp.tasrehak;

public class ListRowModel {
    private int image;
    private String RequestName;
    private String RequesState;
    private String RequesID;


    public ListRowModel(int image, String requestName, String requesState, String requesID) {
        this.image = image;
        RequestName = requestName;
        RequesState = requesState;
        RequesID = requesID;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getRequestName() {
        return RequestName;
    }

    public void setRequestName(String requestName) {
        RequestName = requestName;
    }

    public String getRequesState() {
        return RequesState;
    }

    public void setRequesState(String requesState) {
        RequesState = requesState;
    }

    public String getRequesID() {
        return RequesID;
    }

    public void setRequesID(String requesID) {
        RequesID = requesID;
    }

    @Override
    public String toString() {
        return RequestName +"\n" + RequesID;
    }
}
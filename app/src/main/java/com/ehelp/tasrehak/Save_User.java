package com.ehelp.tasrehak;

public class Save_User {

    private String First_Name;
    private String Last_Name;
    private String ID ;
    private String TypeID;
    private String Phone_Number;
    private String Date_of_Birth;
    private String Password;



    public Save_User(){}

    public Save_User(String first_Name, String last_Name, String ID, String typeID, String phone_Number, String date_of_Birth, String password) {
        First_Name = first_Name;
        Last_Name = last_Name;
        this.ID = ID;
        TypeID = typeID;
        Phone_Number = phone_Number;
        Date_of_Birth = date_of_Birth;
        Password = password;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public String getID() {
        return ID;
    }

    public String getTypeID() {
        return TypeID;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public String getDate_of_Birth() {
        return Date_of_Birth;
    }

    public String getPassword() {
        return Password;
    }
}

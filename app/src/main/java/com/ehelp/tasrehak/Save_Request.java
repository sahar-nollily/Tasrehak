package com.ehelp.tasrehak;

public class Save_Request {

    private String Request_ID;
    private String RequestState;
    private String First_Name;
    private String Last_Name;
    private String ID ;
    private String TypeID;
    private String Phone_Number;
    private String Date_of_Birth;
    private String Date;
    private String Time_From;
    private String Time_To;
    private String Reason;
    private String File_Created;



    public Save_Request(){}

    public Save_Request(String request_ID, String requestState, String first_Name, String last_Name, String ID, String typeID, String phone_Number, String date_of_Birth, String date, String time_From, String time_To, String reason, String file_Created) {
        Request_ID = request_ID;
        RequestState = requestState;
        First_Name = first_Name;
        Last_Name = last_Name;
        this.ID = ID;
        TypeID = typeID;
        Phone_Number = phone_Number;
        Date_of_Birth = date_of_Birth;
        Date = date;
        Time_From = time_From;
        Time_To = time_To;
        Reason = reason;
        File_Created = file_Created;
    }

    public String getRequest_ID() {
        return Request_ID;
    }

    public String getRequestState() {
        return RequestState;
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

    public String getDate() {
        return Date;
    }

    public String getTime_From() {
        return Time_From;
    }

    public String getTime_To() {
        return Time_To;
    }

    public String getReason() {
        return Reason;
    }

    public String getFile_Created() {
        return File_Created;
    }
}

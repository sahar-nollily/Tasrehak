package com.ehelp.tasrehak;

public class UploadPDF {

    String Request_ID;
    String pdf_url ;
    String id ;


    public UploadPDF(){}

    public UploadPDF(String request_ID, String pdf_url, String id) {
        Request_ID = request_ID;
        this.pdf_url = pdf_url;
        this.id = id;
    }

    public String getRequest_ID() {
        return Request_ID;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public String getId() {
        return id;
    }
}

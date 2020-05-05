package com.ehelp.tasrehak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class PermissionDetail extends AppCompatActivity implements TextEncodingCallback {


    TextView fName, lName, ID, phone, date, timeFrom, timeTo, dBirth, reason, TypeID , Request_ID , Request_State, File_Created;
    Button Show ;
    String RequestID;
    private DatabaseReference mDatabase;
    Bitmap bmp, scale, barcodeScale;
    private ImageSteganography imageSteganography;
    private TextEncoding textEncoding;
    private Bitmap encoded_image;
    private Bitmap original_image;
    Date dateObj;
    String User_ID;
    DateFormat dateFormat, timeFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.base);
        scale = Bitmap.createScaledBitmap(bmp,1200,2150,false);

        TypeID = findViewById(R.id.TypeID);
        fName = findViewById(R.id.FirstName);
        lName = findViewById(R.id.LastName);
        ID = findViewById(R.id.TxtID);
        phone = findViewById(R.id.PhoneNo);
        date = findViewById(R.id.TxtDate);
        timeFrom = findViewById(R.id.TxtTimeFrom);
        timeTo = findViewById(R.id.TxtTimeTo);
        dBirth = findViewById(R.id.DateBirth);
        reason = findViewById(R.id.TxtReason);
        Request_ID = findViewById(R.id.Request_ID);
        Request_State  = findViewById(R.id.Request_State);
        File_Created = findViewById(R.id.file_created);
        Show = findViewById(R.id.btn);

        RequestID = getIntent().getExtras().getString("Request_ID");
        Request_ID.setText(RequestID);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Requests").child(RequestID);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String RequestState = dataSnapshot.child("requestState").getValue().toString();
                String First_Name = dataSnapshot.child("first_Name").getValue().toString();
                String Last_Name = dataSnapshot.child("last_Name").getValue().toString();
                User_ID = dataSnapshot.child("id").getValue().toString();
                String Type_ID = dataSnapshot.child("typeID").getValue().toString();
                String Phone_Number = dataSnapshot.child("phone_Number").getValue().toString();
                String Date_of_Birth = dataSnapshot.child("date_of_Birth").getValue().toString();
                String Date = dataSnapshot.child("date").getValue().toString();
                String Time_From = dataSnapshot.child("time_From").getValue().toString();
                String Time_To = dataSnapshot.child("time_To").getValue().toString();
                String Reason = dataSnapshot.child("reason").getValue().toString();
                String FileCreated = dataSnapshot.child("file_Created").getValue().toString();

                TypeID.setText(Type_ID);
                fName.setText(First_Name);
                lName.setText(Last_Name);
                ID.setText(User_ID);
                phone.setText(Phone_Number);
                date.setText(Date);
                dBirth.setText(Date_of_Birth);
                timeFrom.setText(Time_From);
                timeTo.setText(Time_To);
                reason.setText(Reason);
                File_Created.setText(FileCreated);
                Request_State.setText(RequestState);
                Request_ID.setText(RequestID);

                if(RequestState.equals("Accepted")) {
                    Show.setVisibility(View.VISIBLE);
                }

                    Show.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final ProgressDialog loading = ProgressDialog.show(PermissionDetail.this, "Loading...", "Please Wait", false, false);

                            if(File_Created.getText().toString().equals("0")){
                            create();
                            // ضيفي اسم االباركود هنا oraginal_image
                            //User_ID = ID.getText().toString();
                            barcodeScale = Bitmap.createScaledBitmap(original_image,600,250,false);
                            createPDF();
                            mDatabase.child("file_Created").setValue("1");
                            }

                            Intent intent = new Intent(PermissionDetail.this, Pdf_View.class);
                            intent.putExtra("Request_ID", RequestID);
                            startActivity(intent);

                            loading.dismiss();


                        }

                    });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }


    private void createPDF() {

        dateObj = new Date();
        PdfDocument doc = new PdfDocument();
        Paint background = new Paint();
        Paint current = new Paint();
        Paint info = new Paint();

        // settings on page
        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200, 2000, 1).create();
        // apply setting on PDF doc
        PdfDocument.Page myPage1 = doc.startPage(myPageInfo1);

        // images
        Canvas canvas = myPage1.getCanvas();
        canvas.drawBitmap(scale, 1, 1, background);
        canvas.drawBitmap(barcodeScale, 50, 1650, background);

        // current date and time
        current.setTextSize(40);
        dateFormat = new SimpleDateFormat("d/M/yyyy");
        timeFormat = new SimpleDateFormat("h:mm");
        canvas.drawText("Date: " + dateFormat.format(dateObj) + ", Time: " + timeFormat.format(dateObj), 5, 1990, current);

        info.setTextAlign(Paint.Align.LEFT);
        info.setTextSize(50);
        canvas.drawText("" + fName.getText() + " " + lName.getText(), 220, 700, info);
        canvas.drawText("" + TypeID.getText(), 260, 810, info);// Done
        canvas.drawText("" + ID.getText(), 770, 810, info);
        canvas.drawText(""+dBirth.getText(),370,920,info);
        canvas.drawText("" + phone.getText(),420 , 1030, info);
        canvas.drawText("" + date.getText(), 190, 1145, info);
        canvas.drawText("" + timeFrom.getText() + " To " + timeTo.getText(), 820, 1145, info);



        String s = reason.getText().toString();

        StringTokenizer tokens  = new StringTokenizer(s);
        int numWords = tokens.countTokens();

        int inc= 10 , x =0 , y = 1210;
        String str = "";
        final int newLine = 90;

        while(tokens.hasMoreElements()) {

            str += tokens.nextElement().toString() + " ";
            numWords --;
            x ++;

            if (x == inc){
                inc += 10;
                y += newLine;
                canvas.drawText(" " + str, 50, y, info);
                str = " ";
            }
        }
        y += newLine;
        canvas.drawText(" " + str, 50, y, info);


        doc.finishPage(myPage1);
        File file = new File(System.getenv("EXTERNAL_STORAGE"),RequestID + ".PDF");


        try{
            doc.writeTo(new FileOutputStream(file));

            FirebaseStorage storage = FirebaseStorage.getInstance();
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

            // Create a storage reference from our app
            StorageReference storageRef = storage.getReferenceFromUrl("gs://e-help-7a06a.appspot.com");


            Uri uri = Uri.fromFile(file);
            StorageReference riversRef = storageRef.child("pdf/"+uri.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(uri);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uri.isComplete());
                    Uri url = uri.getResult();
                    String key = databaseReference.push().getKey();
                    UploadPDF uploadpdf = new UploadPDF(RequestID, url.toString(), User_ID);
                    databaseReference.child(key).setValue(uploadpdf);

                }
            });

        }catch(IOException e){
            e.printStackTrace();
        }
        doc.close();
    }


    private void create(){

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(RequestID, BarcodeFormat.CODABAR, 500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            original_image = barcodeEncoder.createBitmap(bitMatrix);

            //ImageSteganography Object instantiation
            imageSteganography = new ImageSteganography("Sahar",
                    "11111",
                    original_image);
            //TextEncoding object Instantiation
            textEncoding = new TextEncoding(PermissionDetail.this, PermissionDetail.this);
            //Executing the encoding
            textEncoding.execute(imageSteganography);


        }
        catch (WriterException e) {
            e.printStackTrace();
        }


    }

    // Override method of TextEncodingCallback

    @Override
    public void onStartTextEncoding() {
        //Whatever you want to do at the start of text encoding
    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        //By the end of textEncoding

        if (result != null && result.isEncoded()) {
            encoded_image = result.getEncoded_image();
            //Toast.makeText(CreatePremission.this, "Encoded Successfully", Toast.LENGTH_LONG).show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReferenceFromUrl("gs://e-help-7a06a.appspot.com");

            // Create a reference to "mountains.jpg"
            StorageReference mountainsRef = storageRef.child("img/"+RequestID+".PNG");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            encoded_image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    //createNewPost(imageUrl);
                                }
                            });
                        }
                    }
                }});
        }

    }


}

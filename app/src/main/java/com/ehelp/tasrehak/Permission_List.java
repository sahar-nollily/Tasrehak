package com.ehelp.tasrehak;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;

public class Permission_List extends AppCompatActivity {


    public static final Integer images = R.drawable.logo;
    ListView listView;
    List listRowItems;
    String Id ;


    CustomListViewAdapter customListViewAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_list);

        listView = (ListView) findViewById(R.id.list);

        Id = getIntent().getExtras().getString("ID");
        load();






        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customListViewAdapter.getView(position, view, parent);
                String request_id = customListViewAdapter.RequestID.getText().toString();

                goToDetails(request_id.substring(11));




            }});




        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = listView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " selected");
                // Calls toggleSelection method from ListViewAdapter Class
                customListViewAdapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.delete_menu_option, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:
                        AlertDialog.Builder AlertX= new AlertDialog.Builder(Permission_List.this);
                        AlertX.setTitle("تأكيد الحذف ");
                        AlertX.setMessage("هل انت متأكد ؟");
                        AlertX.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SparseBooleanArray selected = customListViewAdapter.getSelectedIds();

                                // Captures all selected ids with a loop
                                for (int i = (selected.size() - 1); i >= 0; i--) {
                                    if (selected.valueAt(i)) {
                                        ListRowModel selectedListItem = (ListRowModel) customListViewAdapter.getItem(selected.keyAt(i));
                                        //int request_id = selectedListItem.getRequesID();
                                        //show_history(request_id);
                                        // Remove selected items using ids
                                        customListViewAdapter.remove(selectedListItem);
                                    }
                                }
                                mode.finish();
                            }
                        });
                        AlertX.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertX.show();
                        return true;
                    default:
                        return false;
                    // call getSelectedIds method from customListViewAdapter

                }



            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                customListViewAdapter.removeSelection();
            }
        });

    }



    public void goToDetails(String id){

        Intent in = new Intent(this , PermissionDetail.class);
        in.putExtra("Request_ID",id);
        startActivity(in);

    }




    public void load() {
        final ProgressDialog loading = ProgressDialog.show(this, "", "Please Wait ......", false, false);

        DatabaseReference Permission = FirebaseDatabase.getInstance().getReference("Requests");
        Query query = Permission.orderByChild("id").equalTo(Id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listRowItems = new ArrayList();
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    String name = childSnapshot.child("first_Name").getValue(String.class);
                    String State = childSnapshot.child("requestState").getValue(String.class);
                    String request_id = childSnapshot.child("request_ID").getValue(String.class);
                    ListRowModel item = new ListRowModel(images, name, State, request_id);
                    listRowItems.add(item);
                }

                customListViewAdapter = new CustomListViewAdapter(Permission_List.this, R.layout.list_item_row, listRowItems);
                listView.setAdapter(customListViewAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        loading.dismiss();
    }

/*
    public void show_history(final int booking_id) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, History_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Permission_List.this, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Toast.makeText(Permission_List.this, "نأسف تأكد من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("booking_id", booking_id+"");
                return params;
            }};

        requestQueue.add(stringRequest);



    }
*/
}

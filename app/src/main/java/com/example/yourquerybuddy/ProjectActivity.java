package com.example.yourquerybuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProjectActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterProjects.RecyclerViewClickListener listener;
    AdapterProjects adapterProjects;
    ArrayList<ProjectList> list;
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    ProgressDialog progressDialog;
    DatabaseReference noticeCounT=FirebaseDatabase.getInstance().getReference();
    FloatingActionButton OpenDialog;
    Integer count;
    String pos;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
//        LayoutInflater layoutInflater = getLayoutInflater();
//        View v = layoutInflater.inflate(R.layout.notice_crud,layout);
        Objects.requireNonNull(getSupportActionBar()).hide();
        bottomNavigationView = findViewById(R.id.bottomNavigationViewNotices);
        bottomNavigationView.setSelectedItemId(R.id.allNotices);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Fetching data");
        progressDialog.show();

        // Recycler view
        recyclerView=findViewById(R.id.noticesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OpenDialog=findViewById(R.id.openDialogButton);

//        noticeCounT.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
////                    count = snapshot.child("count").getValue(Integer.class);
//                    pos=snapshot.child("Users").child(uid).child("position").getValue(String.class);
//                    if(pos.equals("Admin")){
//                        OpenDialog.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//

        OpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(ProjectActivity.this);
                dialog.setContentView(R.layout.project_crud);

                TextInputEditText noticeTitle=dialog.findViewById(R.id.noticeTitle);
                TextInputEditText noticeDescription=dialog.findViewById(R.id.noticeDescription);
                TextInputEditText noticeLink=dialog.findViewById(R.id.noticeLink);
                Button actionButton=dialog.findViewById(R.id.addNotice);
                noticeCounT= FirebaseDatabase.getInstance().getReference();

                noticeCounT.child("projectCount").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            count = snapshot.getValue(Integer.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String title,desc,link;
                        title = Objects.requireNonNull(noticeTitle.getText()).toString();
                        desc = Objects.requireNonNull(noticeDescription.getText()).toString();
                        link = Objects.requireNonNull(noticeLink.getText()).toString();
                        if(title.isEmpty() || desc.isEmpty()){
                            Toast.makeText(ProjectActivity.this, "All fields are mandatory!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Map<String, Object> notices = new HashMap<>();
                            notices.put("title", title);
                            notices.put("description", desc);
                            notices.put("uidFav", uid+false);
                            notices.put("uidProject", uid);
                            notices.put("projectLink", link);
                            count++;
                            notices.put("count",count);
//                            notices.put("isFavourite",false);
                            String Count = count.toString();

                            Map<String, Object> noticeCount = new HashMap<>();
                            noticeCount.put("projectCount", count);
                            noticeCounT.updateChildren(noticeCount);


                            db.collection("Projects").document(Count).set(notices).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProjectActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ProjectActivity.this, ProjectActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(ProjectActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
                dialog.show();
            }
        });

        list = new ArrayList<ProjectList>();
//        Toast.makeText(this, count, Toast.LENGTH_SHORT).show();
        setOnClickListener();
        adapterProjects = new AdapterProjects(this,list,listener);
        db=FirebaseFirestore.getInstance();
        recyclerView.setAdapter(adapterProjects);

        EventChangeListener();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.allNotices:
                    startActivity(new Intent(ProjectActivity.this, ProjectActivity.class));
                    finish();
                    break;
                case R.id.myNotices:
                    startActivity(new Intent(ProjectActivity.this, MyProjects.class));
                    finish();
                    break;
                case R.id.starred:
                    startActivity(new Intent(ProjectActivity.this,Starred.class));
                    finish();
                    break;
            }
            return true;
        });
    }

    private void setOnClickListener() {
        listener=new AdapterProjects.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position){
                Toast.makeText(ProjectActivity.this, "Longggg", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProjectActivity.this,CommentPanel.class));
                finish();
            }

            @Override
            public void onLongClick(View v, int position) {
//                Dialog dialog = new Dialog(NoticeActivity.this);
//                dialog.setContentView(R.layout.notice_crud);
//                TextView heading = dialog.findViewById(R.id.headingCrud);
//                TextInputEditText noticeTitle=dialog.findViewById(R.id.noticeTitle);
//                TextInputEditText noticeDescription=dialog.findViewById(R.id.noticeDescription);
//                Button actionButton=dialog.findViewById(R.id.addNotice);
//                actionButton.setText("Update notice");
//                heading.setText(list.get(position).getTitle());
//
//                dialog.show();
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                intent.putExtra("title",list.get(position).getTitle());
//                intent.putExtra("description",list.get(position).getDescription());
//                startActivity(intent);
////                layout.addView(v);
//                noticeCounT.child("Users").child(uid).child("starCount").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()) {
//                            starCount = snapshot.getValue(Integer.class);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                starCount=1;

//                noticeCounT.child("noticeCount").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()) {
//                            count = snapshot.getValue(Integer.class);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
                String Position = (position+1) + "";
                Toast.makeText(ProjectActivity.this, Position, Toast.LENGTH_SHORT).show();
                db.collection("Projects").document(Position)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            Boolean isFavourite = documentSnapshot.getBoolean("isFavourite");
                                String title = documentSnapshot.getString("title");
                                String desc = documentSnapshot.getString("description");
                                String uidNotice = documentSnapshot.getString("uidProject");
                                String uidFav = documentSnapshot.getString("uidFav");
                                String projectLink = documentSnapshot.getString("projectLink");
                                if (uidFav.equals(uidNotice+false)) {
                                    Toast.makeText(ProjectActivity.this, "Favourite: "+false, Toast.LENGTH_SHORT).show();
                                    new AlertDialog.Builder(ProjectActivity.this)
                                            .setMessage("Do you want to add this to favourites?")
                                            .setCancelable(true)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Map<String, Object> notices = new HashMap<>();
//                                            notices.put("isFavourite", true);
                                                    notices.put("description", desc);
                                                    notices.put("title", title);
                                                    notices.put("uidFav",uidNotice+true);
                                                    notices.put("count",position+1);
                                                    notices.put("uidProject",uidNotice);
                                                    notices.put("projectLink",projectLink);
//                                                position++;
//                                                String counT = count.toString();

//                                                    Map<String, Object> noticeCount = new HashMap<>();
//                                                    noticeCount.put("starCount", starCount);
//                                                    noticeCounT.child("Users").child(uid).updateChildren(noticeCount);

                                                    db.collection("Projects").document(Position).set(notices).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ProjectActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(ProjectActivity.this, Starred.class));
                                                                finish();
                                                            } else {
                                                                Toast.makeText(ProjectActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("No", null)
                                            .show();
                                }
                                else{
                                    Toast.makeText(ProjectActivity.this, "Favourite "+ true, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProjectActivity.this, "Failed to fetch data" + e, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        };
    }

    private void EventChangeListener() {

        db.collection("Projects")
                .orderBy("count")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(ProjectActivity.this, "Snapshot error", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            for(DocumentChange dc : value.getDocumentChanges()){

                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();

                                if(dc.getType() == DocumentChange.Type.ADDED){
                                    list.add(dc.getDocument().toObject(ProjectList.class));
                                }
                                adapterProjects.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProjectActivity.this,HomeActivity.class));
        finish();
    }
}
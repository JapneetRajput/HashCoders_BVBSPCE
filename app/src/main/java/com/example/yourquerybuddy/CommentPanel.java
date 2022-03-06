package com.example.yourquerybuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommentPanel extends AppCompatActivity {
    Button addComment;
    DatabaseReference realtimeDB= FirebaseDatabase.getInstance().getReference();
    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    String userName,userType;
    FirebaseFirestore db;
    Integer Position,commentCount;
    String title,desc,projectLink,uidNotice,uidFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_panel);
        addComment = findViewById(R.id.buttonComment);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(CommentPanel.this);
                dialog.setContentView(R.layout.comment);

                TextInputEditText comment=dialog.findViewById(R.id.comment);
                Button addComment = dialog.findViewById(R.id.addComment);
                realtimeDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
        //                    count = snapshot.child("count").getValue(Integer.class);
                            userName=snapshot.child("Users").child(uid).child("username").getValue(String.class);
                            userType=snapshot.child("Users").child(uid).child("userType").getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Position = Integer.parseInt(getIntent().getStringExtra("count"));
                DocumentReference documentReference = db.collection("Projects").document(Position.toString());
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        commentCount = Integer.parseInt(Objects.requireNonNull(value.getString("commentCount")));
                    }
                });
                db.collection("Projects").document(Position.toString())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            Boolean isFavourite = documentSnapshot.getBoolean("isFavourite");
                                title = documentSnapshot.getString("title");
                                desc = documentSnapshot.getString("description");
                                uidNotice = documentSnapshot.getString("uidProject");
                                uidFav = documentSnapshot.getString("uidFav");
                                projectLink = documentSnapshot.getString("projectLink");
                                commentCount = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("commentCount")));
                                if (uidFav.equals(uidNotice+false)) {
                                    Toast.makeText(CommentPanel.this, "Favourite: "+false, Toast.LENGTH_SHORT).show();
                                    new AlertDialog.Builder(CommentPanel.this)
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
                                                    notices.put("count",Position);
                                                    notices.put("uidProject",uidNotice);
                                                    notices.put("projectLink",projectLink);
                                                    notices.put("commentCount",commentCount);
//                                                position++;
//                                                String counT = count.toString();

//                                                    Map<String, Object> noticeCount = new HashMap<>();
//                                                    noticeCount.put("starCount", starCount);
//                                                    noticeCounT.child("Users").child(uid).updateChildren(noticeCount);

                                                    db.collection("Projects").document(String.valueOf(Position)).set(notices).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(CommentPanel.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(CommentPanel.this, Starred.class));
                                                                finish();
                                                            } else {
                                                                Toast.makeText(CommentPanel.this, "Update failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("No", null)
                                            .show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CommentPanel.this, "Failed to fetch data" + e, Toast.LENGTH_SHORT).show();
                            }
                        });
//                db.collection("Projects")
//                    .whereEqualTo("count",Position)
//                    .get().addOn
//                db.collection("Projects").document(Po).set(notices).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(ProjectActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(ProjectActivity.this, ProjectActivity.class));
//                            finish();
//                        } else {
//                            Toast.makeText(CommentPanel.this, "Update failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                addComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String commentMsg;
                        commentMsg = Objects.requireNonNull(comment.getText()).toString();
                        if(commentMsg.isEmpty()){
                            Toast.makeText(CommentPanel.this, "All fields are mandatory!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Map<String, Object> notices = new HashMap<>();
                            notices.put("userName", userName);
                            notices.put("userType", userType);
                            notices.put("commentMsg", commentMsg);
                            notices.put("uid", uid);
                            notices.put("count", commentCount);
                            commentCount++;
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
            }
        });
    }
}
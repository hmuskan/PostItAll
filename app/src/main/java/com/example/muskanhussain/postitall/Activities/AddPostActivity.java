package com.example.muskanhussain.postitall.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.muskanhussain.postitall.Model.Blog;
import com.example.muskanhussain.postitall.R;
import com.example.muskanhussain.postitall.Utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private ImageButton addImageButton;
    private EditText postTitle, postDescription;
    private Button submitButton;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Uri mImageUri;
    private ProgressDialog progressDialog;
    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("MBlog");
        addImageButton = findViewById(R.id.postImageButton);
        postTitle = findViewById(R.id.postTitleAdd);
        postDescription = findViewById(R.id.postDescriptionAdd);
        submitButton = findViewById(R.id.addPostButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Constants.GALLERY_CODE);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPost();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            addImageButton.setImageURI(mImageUri);
        }
    }

    private void submitPost() {
        progressDialog.setMessage("Adding Post..");
        progressDialog.show();
        final String titleVal = postTitle.getText().toString().trim();
        final String descVal = postDescription.getText().toString().trim();

        if(!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal)
                && mImageUri != null) {
            StorageReference filePath = mStorage.child("MBlog_images").
                    child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            DatabaseReference newPost = databaseReference.push();

                            Map<String, String> dataToSave = new HashMap<>();
                            dataToSave.put("postTitle", titleVal);
                            dataToSave.put("postDesc", descVal);
                            dataToSave.put("postImage", uri.toString());
                            dataToSave.put("timeStamp", String.valueOf(java.lang.System.currentTimeMillis()));
                            dataToSave.put("userId", mUser.getUid());
                            newPost.setValue(dataToSave);
                            progressDialog.dismiss();
                            startActivity(new Intent(AddPostActivity.this, PostListActivity.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });



                }
            });

        }
    }
}

package com.example.muskanhussain.postitall.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.muskanhussain.postitall.R;
import com.example.muskanhussain.postitall.Utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstName, lastName, email, password;
    private Uri imageUri = null;
    private Button createButton;
    private DatabaseReference databaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private ImageButton profilePic;
    private StorageReference mFirebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference().child("MUsers");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics");
        progressDialog = new ProgressDialog(this);
        firstName = findViewById(R.id.firstNameCreate);
        lastName = findViewById(R.id.lastNameCreate);
        email = findViewById(R.id.emailCreate);
        password = findViewById(R.id.passwordCreate);
        profilePic = findViewById(R.id.profilePictureBtn);
        createButton = findViewById(R.id.createAccountCreate);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Constants.GALLERY_CODE);
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount() {
        final String fname = firstName.getText().toString().trim();
        final String lname = lastName.getText().toString().trim();
        final String emailField = email.getText().toString().trim();
        final String passwordField = password.getText().toString().trim();

        if(!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) &&
                !TextUtils.isEmpty(emailField) && !TextUtils.isEmpty(passwordField)) {
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(emailField, passwordField)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if(authResult != null)
                            {   StorageReference imagePath = mFirebaseStorage.child("MBlog_Profile_Pics")
                                    .child(imageUri.getLastPathSegment());
                                imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String userId = mAuth.getCurrentUser().getUid();
                                        DatabaseReference currentUserDB = databaseReference.child(userId);
                                        currentUserDB.child("firstName").setValue(fname);
                                        currentUserDB.child("lastName").setValue(lname);
                                        currentUserDB.child("image").setValue(imageUri.toString());
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(CreateAccountActivity.this, PostListActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });


                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.GALLERY_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);

        }
    }
}

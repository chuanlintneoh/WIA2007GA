package com.example.ecoventur.ui.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecoventur.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditProfilePictureActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_picture);

        updatePicture();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Profile Picture...");

        Button changeProfilePicButton = findViewById(R.id.changeProfilePicButton);
        changeProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        ImageView back = findViewById(R.id.backprofile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void updatePicture() {
        ImageView profilepic = findViewById(R.id.picture);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String profilePicUrl = document.getString("profilePicUrl");
                            if (profilePicUrl != null && !profilePicUrl.isEmpty()){
                                Glide.with(profilepic.getContext()).load(profilePicUrl).into(profilepic);
                            }
                        }
                    }
                });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            updateProfilePicture(selectedImageUri);
        }
    }

    private void updateProfilePicture(Uri imageUri) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            progressDialog.show(); // Show progress dialog

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + userId + "/profile.jpg");

            // Upload the file and add an onSuccessListener to get the download URL
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    // Store the download URL in Firestore
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("profilePicUrl", downloadUri.toString());

                                    db.collection("users").document(userId)
                                            .update(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    updatePicture();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditProfilePictureActivity.this, "Profile picture updated successfully.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss(); // Dismiss progress dialog on failure
                                                    Toast.makeText(EditProfilePictureActivity.this, "Failed to update profile picture.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss(); // Dismiss progress dialog on failure
                            Toast.makeText(EditProfilePictureActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
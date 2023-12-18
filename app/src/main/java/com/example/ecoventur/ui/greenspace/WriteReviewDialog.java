package com.example.ecoventur.ui.greenspace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.example.ecoventur.R;

import java.util.UUID;

public class WriteReviewDialog {
    private Activity activity;
    private ActivityResultLauncher<Intent> ARL;
    private View dialogView;
    private ImageView IVReviewImage;
    private LinearProgressIndicator progressBar;
    private Button btnTakePhoto, btnSelectPhoto, btnRemovePhoto, btnCancel, btnSubmit;
    private RatingBar ratingBar;
    private EditText ETReview;
    private Uri reviewImageUri = null;
    public interface WriteReviewDialogListener {
        void onCancelClicked();
        void onSubmitClicked(Uri imageUri, float rating, String review);
    }
    private WriteReviewDialogListener listener;
    public WriteReviewDialog (Activity activity, ActivityResultLauncher<Intent> ARL, WriteReviewDialogListener listener) {
        this.activity = activity;
        this.ARL = ARL;
        this.listener = listener;
        show();
    }
    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = LayoutInflater.from(activity);
        dialogView = inflater.inflate(R.layout.dialog_write_review, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        initializeWidgets();

        btnTakePhoto.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ARL.launch(intent);
            handleReviewImage(intent);
        });
        btnSelectPhoto.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            ARL.launch(intent);
            handleReviewImage(intent);
        });
        btnRemovePhoto.setOnClickListener(view -> {
            removeReviewImage();
        });

        btnCancel.setOnClickListener(view -> {
            if (listener != null) listener.onCancelClicked();
            alertDialog.dismiss();
        });
        btnSubmit.setOnClickListener(view -> {
            float rating = ratingBar.getRating();
            String review = ETReview.getText().toString();
            if (listener != null) listener.onSubmitClicked(reviewImageUri, rating, review);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }
    private void initializeWidgets() {
        IVReviewImage = dialogView.findViewById(R.id.IVReviewImage);
        progressBar = dialogView.findViewById(R.id.progressBar);
        btnTakePhoto = dialogView.findViewById(R.id.btnTakePhoto);
        btnSelectPhoto = dialogView.findViewById(R.id.btnSelectPhoto);
        btnRemovePhoto = dialogView.findViewById(R.id.btnRemovePhoto);
        ratingBar = dialogView.findViewById(R.id.ratingBar);
        ETReview = dialogView.findViewById(R.id.ETReview);
        btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnSubmit = dialogView.findViewById(R.id.btnSubmit);
    }
    public void setReviewImageUri(Uri reviewImageUri) {
        Glide.with(dialogView).load(reviewImageUri).into(IVReviewImage);
    }
    public void handleReviewImage(Intent data) {
        if (data != null && data.getData() != null) {
            reviewImageUri = data.getData();
            setReviewImageUri(reviewImageUri);
        }
    }
    public void removeReviewImage() {
        reviewImageUri = null;
        IVReviewImage.setImageResource(R.drawable.upload_placeholder);
    }
    public void uploadImage() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("reviewImages/" + UUID.randomUUID().toString());
        storageReference.putFile(reviewImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(activity, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(activity, "Image upload failed!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setMax(Math.toIntExact(taskSnapshot.getTotalByteCount()));
                        progressBar.setProgress(Math.toIntExact(taskSnapshot.getBytesTransferred()));
                    }
                });
    }
}

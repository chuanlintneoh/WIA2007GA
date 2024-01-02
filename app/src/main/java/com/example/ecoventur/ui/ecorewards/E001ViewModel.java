package com.example.ecoventur.ui.ecorewards;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class E001ViewModel extends ViewModel {
    private MutableLiveData<List<Voucher>> vouchers = new MutableLiveData<>();
    private MutableLiveData<String> redeemedVoucherTitle = new MutableLiveData<>();
    private MutableLiveData<String> redeemedVoucherImageURL = new MutableLiveData<>();
    private MutableLiveData<Integer> redeemedVoucherCoins = new MutableLiveData<>();

    // Getter methods for LiveData
    public LiveData<List<Voucher>> getVouchers() {
        return vouchers;
    }

    public LiveData<String> getRedeemedVoucherTitle() {
        return redeemedVoucherTitle;
    }

    public LiveData<String> getRedeemedVoucherImageURL() {
        return redeemedVoucherImageURL;
    }

    public LiveData<Integer> getRedeemedVoucherCoins() {
        return redeemedVoucherCoins;
    }

    // Setter methods to set the redeemed voucher details
    public void setRedeemedVoucherDetails(String title, String imageURL, int coins) {
        redeemedVoucherTitle.postValue(title);
        redeemedVoucherImageURL.postValue(imageURL);
        redeemedVoucherCoins.postValue(coins);
    }

    // Setter method to set the list of vouchers
    public void setVouchers(List<Voucher> vouchersList) {
        vouchers.postValue(vouchersList);
    }

    public LiveData<Integer> getUserEcoCoins() {
        MutableLiveData<Integer> userEcoCoins = new MutableLiveData<>();

        // Replace 'uaPJZguefgcNGyl0Ig2sy1Yq6tu1' with the actual user ID
        DocumentReference userDocRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document("uaPJZguefgcNGyl0Ig2sy1Yq6tu1");

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Long ecoCoinsLong = snapshot.getLong("ecocoin");
                    int ecoCoins = ecoCoinsLong != null ? ecoCoinsLong.intValue() : 0;
                    userEcoCoins.postValue(ecoCoins);
                } else {
                    // Handle the case where the document doesn't exist
                    userEcoCoins.postValue(0); // Set a default value or handle the error
                }
            } else {
                // Handle the case where fetching user's EcoCoins fails or task result is null
                userEcoCoins.postValue(0); // Set a default value or handle the error
            }
        });

        return userEcoCoins;
    }


}

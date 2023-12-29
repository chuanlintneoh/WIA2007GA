package com.example.ecoventur.ui.ecorewards;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class E002ViewModel extends ViewModel {
    private FirebaseFirestore db;
    private MutableLiveData<List<Transaction>> spending;
    private MutableLiveData<List<Transaction>> earning;
    private MutableLiveData<Integer> balance;

    public E002ViewModel() {
        db = FirebaseFirestore.getInstance();
        spending = new MutableLiveData<>();
        earning = new MutableLiveData<>();
        balance = new MutableLiveData<>();
    }

    public LiveData<List<Transaction>> getSpending() {
        return spending;
    }

    public LiveData<List<Transaction>> getEarning() {
        return earning;
    }

    public LiveData<Integer> getBalance() {
        return balance;
    }

    public void fetchDataFromFirestore() {
        fetchSpendingFromFirestore();
        fetchEarningFromFirestore();
    }

    private void fetchSpendingFromFirestore() {
        db.collection("users").document("uaPJZguefgcNGyl0Ig2sy1Yq6tu1")
                .collection("spending")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Transaction> spendingList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String voucherTitle = document.getString("title");
                        Long ecoCoinsLong = document.getLong("ecoCoin");
                        int ecoCoins = (ecoCoinsLong != null) ? ecoCoinsLong.intValue() : 0;

                        Transaction transaction = new Transaction(voucherTitle, ecoCoins);
                        spendingList.add(transaction);
                    }
                    spending.setValue(spendingList);
                });
    }

    private void fetchEarningFromFirestore() {
        db.collection("users").document("uaPJZguefgcNGyl0Ig2sy1Yq6tu1")
                .collection("earning")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Transaction> earningList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String voucherTitle = document.getString("title");
                        Long earningCoinsLong = document.getLong("ecoCoin");
                        int earningCoins = (earningCoinsLong != null) ? earningCoinsLong.intValue() : 0;

                        Transaction earning = new Transaction(voucherTitle, earningCoins);
                        earningList.add(earning);
                    }
                    earning.setValue(earningList);
                });
    }

    public void calculateBalance() {
        db.collection("users").document("uaPJZguefgcNGyl0Ig2sy1Yq6tu1")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long ecocoin = documentSnapshot.getLong("ecocoin");
                        if (ecocoin != null) {
                            LiveData<List<Transaction>> spendingData = spending;
                            LiveData<List<Transaction>> earningData = earning;

                            spendingData.observeForever(spendingList -> {
                                earningData.observeForever(earningList -> {
                                    if (spendingList != null && earningList != null) {
                                        int totalSpending = calculateTotalSpending(spendingList);
                                        int totalEarning = calculateTotalEarning(earningList);

                                        int finalBalance = ecocoin.intValue() + totalEarning - totalSpending;
                                        balance.setValue(finalBalance);

                                        // Update ecocoin field in Firestore with the new calculated balance
                                        updateEcocoinFieldInFirestore(finalBalance);
                                    }
                                });
                            });
                        }
                    }
                });
    }

    private void updateEcocoinFieldInFirestore(int finalBalance) {
        // Update the ecocoin field in Firestore
        db.collection("users").document("uaPJZguefgcNGyl0Ig2sy1Yq6tu1")
                .update("ecocoin", finalBalance)
                .addOnSuccessListener(aVoid -> Log.d("E002ViewModel", "Ecocoin field updated successfully"))
                .addOnFailureListener(e -> Log.e("E002ViewModel", "Error updating ecocoin field: " + e.getMessage()));
    }



    private int calculateTotalSpending(List<Transaction> spendingList) {
        int totalSpending = 0;
        for (Transaction transaction : spendingList) {
            totalSpending += transaction.getEcoCoins(); // Use getEcoCoins() method
        }
        return totalSpending;
    }

    private int calculateTotalEarning(List<Transaction> earningList) {
        int totalEarning = 0;
        for (Transaction transaction : earningList) {
            totalEarning += transaction.getEcoCoins(); // Use getEcoCoins() method
        }
        return totalEarning;
    }

}

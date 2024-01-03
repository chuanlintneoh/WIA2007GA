package com.example.ecoventur.ui.ecorewards.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecoventur.ui.ecorewards.models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewEcocoinsBalanceViewModel extends ViewModel {
    private FirebaseFirestore db;
    private MutableLiveData<List<Transaction>> spending;
    private MutableLiveData<List<Transaction>> earning;
    private MutableLiveData<Integer> balance;
    private String UID;

    public ViewEcocoinsBalanceViewModel() {
        db = FirebaseFirestore.getInstance();
        spending = new MutableLiveData<>();
        earning = new MutableLiveData<>();
        balance = new MutableLiveData<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = user.getUid();
        } else {
            Log.e("E002ViewModel", "Error retrieving UID");
        }
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
        db.collection("users").document(UID)
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
        db.collection("users").document(UID)
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
        LiveData<List<Transaction>> spendingData = getSpending();
        LiveData<List<Transaction>> earningData = getEarning();

        spendingData.observeForever(spendingList -> {
            earningData.observeForever(earningList -> {
                if (spendingList != null && earningList != null) {
                    int totalSpending = calculateTotalSpending(spendingList);
                    int totalEarning = calculateTotalEarning(earningList);

                    // Calculate the balance without querying Firestore ecocoin value
                    int newBalance = totalEarning - totalSpending;

                    // Adding 500 to the calculated newBalance
                    newBalance += 500;

                    // Get the existing ecocoin value from the balance LiveData
                    Integer currentBalance = balance.getValue();

                    if (currentBalance == null || newBalance != currentBalance) {
                        // If there's a change in balance, update LiveData and Firestore
                        balance.setValue(newBalance);
                        updateEcocoinFieldInFirestore(newBalance);
                    }
                }
            });
        });
    }

    public void observeSpendingList(LiveData<List<Transaction>> spendingLiveData) {
        spendingLiveData.observeForever(spendingList -> {
            if (spendingList != null && !spendingList.isEmpty()) {
                calculateBalance();
            }
        });
    }

    public void observeEarningList(LiveData<List<Transaction>> earningLiveData) {
        earningLiveData.observeForever(earningList -> {
            if (earningList != null && !earningList.isEmpty()) {
                calculateBalance();
            }
        });
    }

    private void updateEcocoinFieldInFirestore(int finalBalance) {
        // Update the ecocoin field in Firestore
        db.collection("users").document(UID)
                .update("ecocoin", finalBalance)
                .addOnSuccessListener(aVoid -> Log.d("E002ViewModel", "Ecocoin field updated successfully"))
                .addOnFailureListener(e -> Log.e("E002ViewModel", "Error updating ecocoin field: " + e.getMessage()));
    }

    private int calculateTotalSpending(List<Transaction> spendingList) {
        if (spendingList == null) {
            return 0; // Return 0 if the spending list is null
        }

        int totalSpending = 0;
        for (Transaction transaction : spendingList) {
            if (transaction != null) {
                totalSpending += transaction.getEcoCoins();
            }
        }
        return totalSpending;
    }

    private int calculateTotalEarning(List<Transaction> earningList) {
        if (earningList == null) {
            return 0; // Return 0 if the earning list is null
        }

        int totalEarning = 0;
        for (Transaction transaction : earningList) {
            if (transaction != null) {
                totalEarning += transaction.getEcoCoins();
            }
        }
        return totalEarning;
    }
}


package com.example.ecoventur.ui.ecorewards;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoventur.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class e00101 extends Fragment {

    private E00101ViewModel viewModelE00101;
    private VoucherAdapter voucherAdapter;
    private FirebaseFirestore db;
    String userId = "uaPJZguefgcNGyl0Ig2sy1Yq6tu1";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelE00101 = new ViewModelProvider(requireActivity()).get(E00101ViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();

        // Set the title for the Toolbar in the hosting activity
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Redeem Voucher");
        }
        View view = inflater.inflate(R.layout.fragment_e00101, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.voucherView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        voucherAdapter = new VoucherAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(voucherAdapter);

        db = FirebaseFirestore.getInstance();

        observeViewModel();
        fetchVouchersFromFirestore();

        return view;
    }



    private void fetchVouchersFromFirestore() {
        // Reference the user document under 'users'
        DocumentReference userRef = db.collection("users").document(userId);

        // Reference the 'activeVoucher' collection under the user document
        CollectionReference activeVoucherCollectionRef = userRef.collection("activeVoucher");
        CollectionReference pastVoucherCollectionRef = userRef.collection("pastVoucher");

        // Get the current timestamp
        Timestamp currentTimestamp = Timestamp.now();

        activeVoucherCollectionRef.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Voucher> voucherList = new ArrayList<>();
                ArrayList<DocumentSnapshot> expiredVouchers = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    try {
                        String voucherTitle = document.getString("voucherTitle");

                        // Retrieve the timestamp field as a Firestore Timestamp
                        Timestamp timestamp = document.getTimestamp("timestamp");

                        if (timestamp != null) {
                            // Add 30 days to the expiry date
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(timestamp.toDate());
                            calendar.add(Calendar.DAY_OF_YEAR, 30);

                            // Get the date after adding 30 days and convert it back to a Timestamp
                            Timestamp expiryTimestamp = new Timestamp(calendar.getTime());

                            if (isVoucherExpired(expiryTimestamp.toDate())) {
                                // Add expired voucher to the list
                                expiredVouchers.add(document);
                            } else {
                                String imageUrl = document.getString("imgURL1");
                                Voucher voucher = new Voucher(voucherTitle, expiryTimestamp, imageUrl);
                                voucherList.add(voucher);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("FetchVouchers", "Error while fetching vouchers: " + e.getMessage());
                    }
                }

                // Move expired vouchers to pastVoucher collection
                moveExpiredVouchers(expiredVouchers, userRef);

                // Update ViewModel with fetched active voucher data
                viewModelE00101.setActiveVouchers(voucherList);
            } else {
                Log.d("FetchVouchers", "Error getting vouchers: " + task.getException());
            }
        });
    }




    private boolean isVoucherExpired(Date expiryDate) {
        // Logic to check if the voucher has expired based on the current date
        // Compare 'expiryDate' with the current date
        Date currentDate = Calendar.getInstance().getTime();
        return currentDate.after(expiryDate);
    }

    private void moveExpiredVouchers(ArrayList<DocumentSnapshot> expiredVouchers, DocumentReference userRef) {
        CollectionReference pastVoucherCollectionRef = userRef.collection("pastVoucher");
        CollectionReference activeVoucherCollectionRef = userRef.collection("activeVoucher");

        for (DocumentSnapshot expiredVoucher : expiredVouchers) {
            String voucherName = expiredVoucher.getString("voucherTitle");
            String voucherImageURL = expiredVoucher.getString("imgURL1");

            Long ecoCoinsLong = expiredVoucher.getLong("ecoCoins");
            int ecoCoins = (ecoCoinsLong != null) ? ecoCoinsLong.intValue() : 0;

            Map<String, Object> expiredVoucherData = expiredVoucher.getData();

            // Check if the voucher is expired
            if (isVoucherExpired(expiredVoucher.getTimestamp("timestamp").toDate())) {
                // Move the expired voucher document from activeVoucher to pastVoucher
                pastVoucherCollectionRef.add(expiredVoucherData)
                        .addOnSuccessListener(result -> {
                            // Successfully moved to pastVoucher
                            // Delete the expired voucher from activeVoucher collection
                            activeVoucherCollectionRef.document(expiredVoucher.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("e00101 Fragment", "Expired voucher moved to pastVoucher and deleted from activeVoucher");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.d("e00101 Fragment", "Failed to delete expired voucher from activeVoucher: " + e.getMessage());
                                    });
                        })
                        .addOnFailureListener(e -> {
                            // Failed to move voucher to pastVoucher
                            Log.d("e00101 Fragment", "Failed to move expired voucher to pastVoucher: " + e.getMessage());
                        });
            }
        }
    }



    private void observeViewModel() {
        viewModelE00101.getActiveVouchers().observe(getViewLifecycleOwner(), vouchersList -> {
            if (vouchersList != null && !vouchersList.isEmpty()) {
                Log.d("e00101 Fragment", "Received vouchers list with items");
                // Update RecyclerView with fetched data from ViewModel
                voucherAdapter.updateList(vouchersList);
            } else {
                // No vouchers or error fetching data
                Log.d("e00101 Fragment", "Received empty vouchers list or null");
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve ViewModel instance
        viewModelE00101 = new ViewModelProvider(requireActivity()).get(E00101ViewModel.class);

        // Retrieve the voucher name from the bundle passed from e001 fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            String voucherName = bundle.getString("voucherTitle");
            boolean isActiveVoucher = bundle.getBoolean("isActiveVoucher");
            String voucherImage = bundle.getString("imgURL1");
            int ecoCoins = bundle.getInt("ecoCoins");
            if (isActiveVoucher) {
                // Set the redeemed voucher details into the ViewModel
                viewModelE00101.setRedeemedVoucherDetails(voucherName, voucherImage, ecoCoins);

                // Voucher is active, store in activeVouchers collection
                storeVoucherInFirestore(db.collection("users").document(userId).collection("activeVoucher"),
                        userId, voucherName, voucherImage, ecoCoins);
            } else {
                // Voucher is past, store in pastVouchers collection
                storeVoucherInFirestore(db.collection("pastVoucher"), userId, voucherName, voucherImage, ecoCoins);
            }
        }
    }


    private void storeVoucherInFirestore(CollectionReference voucherCollectionRef, String userId, String voucherName, String voucherImageURL, int ecoCoins) {
        Timestamp timestamp = Timestamp.now();

        // Create a new document with an auto-generated ID in the specified collection
        Map<String, Object> voucherData = new HashMap<>();
        voucherData.put("voucherTitle", voucherName);
        voucherData.put("imgURL1", voucherImageURL);
        voucherData.put("timestamp", timestamp);
        voucherData.put("ecoCoins", ecoCoins);

        // Reference the user document under 'users'
        DocumentReference userRef = db.collection("users").document(userId);

        // Add the voucher data to the specified collection
        voucherCollectionRef.add(voucherData)
                .addOnSuccessListener(documentReference -> {
                    // Voucher added successfully
                    Toast.makeText(requireContext(), "Voucher added to Firestore", Toast.LENGTH_SHORT).show();

                    // Additionally, if you want to update the 'spending' collection
                    // Reference the 'spending' collection for the user
                    CollectionReference spendingCollectionRef = userRef.collection("spending");

                    // Create a document in the 'spending' collection with the voucher details
                    Map<String, Object> spendingData = new HashMap<>();
                    spendingData.put("title", voucherName);
                    spendingData.put("ecoCoin", ecoCoins);
                    spendingData.put("timestamp", timestamp);

                    spendingCollectionRef.add(spendingData)
                            .addOnSuccessListener(spendingDocumentReference -> {
                                // Spendings updated successfully
                                Toast.makeText(requireContext(), "Spending updated", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Failed to update spendings
                                Toast.makeText(requireContext(), "Failed to update spending", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    // Failed to add voucher
                    Toast.makeText(requireContext(), "Failed to add voucher to Firestore", Toast.LENGTH_SHORT).show();
                });
    }



    private void updateActiveVouchersAndSpendings(String voucherName, int ecoCoins, String userId) {
        // Get current timestamp as a formatted string
        Timestamp timestamp = Timestamp.now();
        // Reference the user document under 'users'
        DocumentReference userRef = db.collection("users").document(userId);

        // Create a Firestore transaction
        db.runTransaction(transaction -> {
                    // Update active vouchers in Firestore under the user-specific collection
                    DocumentReference activeVoucherRef = userRef.collection("activeVoucher").document(voucherName);
                    Map<String, Object> activeVoucherData = new HashMap<>();
                    activeVoucherData.put("voucherTitle", voucherName);
                    activeVoucherData.put("imgURL1", viewModelE00101.getRedeemedVoucherImageURL().getValue());
                    activeVoucherData.put("ecoCoins", ecoCoins);
                    activeVoucherData.put("timestamp", timestamp);

                    transaction.set(activeVoucherRef, activeVoucherData);

                    // Update spendings in Firestore under the user-specific collection
                    DocumentReference spendingRef = userRef.collection("spending").document(voucherName);
                    Map<String, Object> spendingData = new HashMap<>();
                    spendingData.put("title", voucherName);
                    spendingData.put("ecoCoin", ecoCoins);
                    spendingData.put("timestamp", timestamp);

                    transaction.set(spendingRef, spendingData);

                    // Return a result to signify transaction success
                    return true;
                })
                .addOnSuccessListener(result -> {
                    // Both collections updated successfully
                    Toast.makeText(requireContext(), "Voucher and spendings updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to update one or both collections
                    Toast.makeText(requireContext(), "Failed to update voucher and spendings", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Transaction failed: " + e.getMessage());
                });
    }

}



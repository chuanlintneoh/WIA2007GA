package com.example.ecoventur.ui.transit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoventur.R;

import com.example.ecoventur.databinding.FragmentTransitBinding;
import com.example.ecoventur.ui.transit.adapters.AllChallengesAdapter;
import com.example.ecoventur.ui.transit.adapters.ChallengingAdapter;
import com.example.ecoventur.ui.transit.adapters.CompletedAdapter;
import com.example.ecoventur.ui.transit.model.AllChallenges;
import com.example.ecoventur.ui.transit.model.Challenging;
import com.example.ecoventur.ui.transit.model.Completed;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import com.google.firebase.firestore.EventListener;

import java.util.Date;
import java.util.List;
import java.util.Collections;

public class TransitFragment extends Fragment {

    private FragmentTransitBinding binding;
    FirebaseFirestore db;
    String userID;

    //All Challenges
    List <AllChallenges> allChallengesList;
    private RecyclerView allChallengesRecyclerView;
    private AllChallengesAdapter allChallengesAdapter;

    //All Challenging
    List <Challenging> allChallengingList;
    private RecyclerView allChallengingRecyclerView;
    private ChallengingAdapter allChallengingAdapter;

    //Completed
    List<Completed> allCompletedList;
    RecyclerView allCompletedRecyclerView;
    CompletedAdapter allCompletedAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transit, container, false);

        userID = "uaPJZguefgcNGyl0Ig2sy1Yq6tu1";

        db = FirebaseFirestore.getInstance();



        //Initialize RecyclerView and Set Layout Manager for AllChallenges
        //Initialize adpater and set it to RecyclerView
        allChallengesRecyclerView = root.findViewById(R.id.all_challenges);
        allChallengesList = new ArrayList<>();
        allChallengesAdapter = new AllChallengesAdapter(getContext(),allChallengesList);
        allChallengesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allChallengesRecyclerView.setAdapter(allChallengesAdapter);

        allChallengingRecyclerView = root.findViewById(R.id.all_challenging);
        allChallengingList = new ArrayList<>();
        allChallengingAdapter = new ChallengingAdapter(getContext(), allChallengingList);
        allChallengingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allChallengingRecyclerView.setAdapter(allChallengingAdapter);
        allChallengingAdapter.setHorizontalLayoutManager(allChallengingRecyclerView);

        allCompletedRecyclerView = root.findViewById(R.id.all_completed_challenges);
        allCompletedList = new ArrayList<>();
        allCompletedAdapter = new CompletedAdapter(getContext(),allCompletedList);
        allCompletedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allCompletedRecyclerView.setAdapter(allCompletedAdapter);

        //Fetch data from firestore
        fetchAllChallenging(userID);
        fetchAllCompleted(userID);


        return root;
    }

    private void fetchAllChallenges() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("FirebaseFetch (All Challenges)", "Fetching all challenges from Firestore...");

        db.collection("challenges")
                .orderBy("startDate")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        int totalRecord = task.getResult().size();;  // Get the number of records
                        Log.d("FirebaseFetch (All Challenges)", "Number of records in 'challenges' collection: " + totalRecord);

                        int recordCount = 0;

                        try {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // Check if the challenge has not expired
                                Date currentDate = new Date();
                                Date startDate = document.getDate("startDate");

                                if (startDate != null && !startDate.before(currentDate)){
                                    if (!isChallengeInChallengingList(document.getId())){

                                        Log.d("FirebaseFetch (All Challenges)", "Document ID (All Challenges): " + document.getId());

                                        AllChallenges allChallenges = document.toObject(AllChallenges.class);
                                        allChallengesList.add(allChallenges);
                                        recordCount++;
                                    }

                                }
                            }

                            allChallengesAdapter.notifyDataSetChanged();

                            // Log the message with the record count
                            String message = recordCount + " records in challenges collection fetched from Firebase";
                            Log.d("FirebaseFetch (All Challenges)", message);

                        } catch (Exception e) {
                            Log.e("FirebaseFetch (All Challenges)", "Exception while processing documents: " + e.getMessage());
                        }

                    } else {
                        Log.e("FirebaseFetch (All Challenges)", "Error getting documents: ", task.getException());
                        Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchAllChallenging(String userID) {
        Log.d("FirebaseFetch (Challenging)", "Fetching challenging items from Firestore for user: " + userID);

        db.collection("users").document(userID).collection("challenging")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            int recordCount = documents.size(); // Get the number of records
                            Log.d("FirebaseFetch (Challenging)", "Number of records in 'challenging' collection: " + recordCount);

                            for (DocumentSnapshot document : documents) {
                                DocumentReference challengingIDRef = document.getDocumentReference("challengingID");
                                fetchSpecificChallengingChallenge(challengingIDRef);
                            }

                            // Log the message with the record count
                            String message = recordCount + " records fetched from Firebase";
                            Log.d("FirebaseFetch (Challenging)", message);

                            fetchAllChallenges();
                        } catch (Exception e) {
                            Log.e("FirebaseFetch (Challenging)", "Exception while processing documents: " + e.getMessage());
                        }
                    } else {
                        Log.e("FirebaseFetch (Challenging)", "Error getting documents: ", task.getException());
                        Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void fetchSpecificChallengingChallenge(DocumentReference challengingIDRef) {
        Log.d("FirebaseFetch (Challenging)", "Fetching specific challenging item from Firestore with ID: " + challengingIDRef);

        challengingIDRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // DocumentSnapshot data extraction
                    String imageUrl = document.getString("imageUrl");
                    String title = document.getString("title");
                    Date endDate = document.getDate("endDate");
                    String challengingID = document.getId();

                    // Check if the challenge is still valid based on endDate
                    Date currentDate = new Date();
                    if (endDate != null && currentDate.before(endDate)) {
                        // Create a Challenging object with the retrieved data
                        Challenging challenging = new Challenging(imageUrl, title, endDate, challengingID);

                        // Add the Challenging object to your list or adapter
                        allChallengingList.add(challenging);
                        allChallengingAdapter.notifyDataSetChanged();

                        Log.d("FirebaseFetch (Challenging)", "Challenging data fetched: " + document.getData());

                        // Log the contents of allChallengingList
                        Log.d("FirebaseFetch (Challenging)", "allChallengingList: " + allChallengingList.toString());

                        // Sort the list based on end date
                        Collections.sort(allChallengingList, (c1, c2) -> {
                            Date endDate1 = c1.getEndDate();
                            Date endDate2 = c2.getEndDate();

                            Log.d("FirebaseFetch (Challenging)", "EndDate1: " + endDate1 + ", EndDate2: " + endDate2);

                            // Adjust the comparison logic as needed
                            return endDate1.compareTo(endDate2);
                        });

                        Log.d("FirebaseFetch (Challenging)", "Challenging list sorted");

                        // Log the contents of allChallengingList
                        Log.d("FirebaseFetch (Challenging)", "allChallengingList (After Sorting): " + allChallengingList.toString());

                        // Notify adapter after sorting
                        allChallengingAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("FirebaseFetch (Challenging)", "No such challenging document");
                }
            } else {
                Log.e("FirebaseFetch (Challenging)", "Error getting challenging document: ", task.getException());
                Toast.makeText(getActivity(), "Error fetching challenging data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAllCompleted(String userID) {
        Log.d("FirebaseFetch (Completed)", "Fetching completed items from Firestore for user: " + userID);

        db.collection("users").document(userID).collection("completed")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            int recordCount = documents.size(); // Get the number of records
                            Log.d("FirebaseFetch (Completed)", "Number of records in 'completed' collection: " + recordCount);

                            for (DocumentSnapshot document : documents) {
                                DocumentReference challengingIDRef = document.getDocumentReference("completedID");
                                fetchSpecificCompletedChallenge(challengingIDRef);
                            }

                            // Log the message with the record count
                            String message = recordCount + " records fetched from Firebase";
                            Log.d("FirebaseFetch (Completed)", message);
                        } catch (Exception e) {
                            Log.e("FirebaseFetch (Completed)", "Exception while processing documents: " + e.getMessage());
                        }
                    } else {
                        Log.e("FirebaseFetch (Completed)", "Error getting documents: ", task.getException());
                        Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchSpecificCompletedChallenge(DocumentReference challengingIDRef) {
        Log.d("FirebaseFetch (Completed)", "Fetching specific completed item from Firestore with ID: " + challengingIDRef);

        challengingIDRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // DocumentSnapshot data extraction
                    String imageUrl = document.getString("imageUrl");
                    String title = document.getString("title");
                    Date endDate = document.getDate("endDate");
                    Date startDate = document.getDate("startDate");
                    List<String> tags = (List<String>) document.get("tags");

                    // Create a Completed object with the retrieved data
                    Completed completed = new Completed(imageUrl, tags, title, startDate, endDate);

                    // Add the Completed object to your list or adapter
                    allCompletedList.add(completed);
                    allCompletedAdapter.notifyDataSetChanged();

                    Log.d("FirebaseFetch (Completed)", "Completed data fetched: " + document.getData());

                    // Log the contents of allChallengingList
                    Log.d("FirebaseFetch (Challenging)", "allChallengingList: " + allCompletedList.toString());

                    // Sort the list based on end date
                    Collections.sort(allCompletedList, (c1, c2) -> {
                        Date startDate1 = c1.getStartDate();
                        Date startDate2 = c2.getStartDate();

                        Log.d("FirebaseFetch (Challenging)", "EndDate1: " + startDate1 + ", EndDate2: " + startDate2);

                        // Adjust the comparison logic as needed
                        return startDate1.compareTo(startDate2);
                    });

                    Log.d("FirebaseFetch (Challenging)", "Challenging list sorted");

                    // Log the contents of allChallengingList
                    Log.d("FirebaseFetch (Challenging)", "allChallengingList (After Sorting): " + allCompletedList.toString());

                    // Notify adapter after sorting
                    allCompletedAdapter.notifyDataSetChanged();

                } else {
                    Log.d("FirebaseFetch (Completed)", "No such completed document");
                }
            } else {
                Log.e("FirebaseFetch (Completed)", "Error getting completed document: ", task.getException());
                Toast.makeText(getActivity(), "Error fetching completed data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //method to check whether the challenges is currently challenging by the user or not
    private boolean isChallengeInChallengingList(String challengeId) {
        for (Challenging challenging : allChallengingList) {
            String challengingId = challenging.getChallengingID();
            Log.d("ChallengingListComparison", "Challenging ID: " + challengingId + ", Challenge ID: " + challengeId);
            if (challenging.getChallengingID().equals(challengeId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
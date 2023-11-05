package com.example.mobisi.firebase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Firebase {
    static FirebaseFirestore database;

    public static  FirebaseFirestore getFirebaseFirestore(){
        if (database == null){
            database = FirebaseFirestore.getInstance();
        }

        return database;
    }

}

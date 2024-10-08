package com.pi.supplybridge.data.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseService {
    val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val authInstance: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
}

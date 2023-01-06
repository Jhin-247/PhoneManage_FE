package com.b18dccn562.phonemanager.utils

import com.b18dccn562.phonemanager.common.Constants
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

fun getRealtimeDatabase(): FirebaseDatabase {
    return Firebase.database
}

fun signUpNewAccountVariables(email: String) {
    val database = getRealtimeDatabase()
    val ref = email.replace(".", "_")
    database.getReference(ref).child(Constants.FirebaseReference.CHANGE_LIMIT).setValue(false)
    database.getReference(ref).child(Constants.FirebaseReference.HAS_NOTIFICATION).setValue(false)
}

fun pushPartnerRequestNotification(partnerEmail: String) {
    val database = getRealtimeDatabase()
    val ref = partnerEmail.replace(".", "_")
    database.getReference(ref).child(Constants.FirebaseReference.HAS_NOTIFICATION).setValue(true)
}

fun createClass(classId: Long) {
    val database = getRealtimeDatabase()
    val ref = "Class"
    database.getReference(ref).child(classId.toString()).child("is_started").setValue(false)
}

fun startClass(classId: Long){
    val database = getRealtimeDatabase()
    val ref = "Class"
    database.getReference(ref).child(classId.toString()).child("is_started").setValue(true)
}

fun endClass(classId: Long){
    val database = getRealtimeDatabase()
    val ref = "Class"
    database.getReference(ref).child(classId.toString()).child("is_started").setValue(false)
}


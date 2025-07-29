package com.example.ecartapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth

class ECartViewModel: ViewModel() {
    val auth= Firebase.auth
    private val fireStore = Firebase.firestore
    private val _eCartState = mutableStateOf<ECartState>(ECartState.Unauthenticated)
    val eCartState: State<ECartState> = _eCartState
    init {
        checkUser()
    }
    fun checkUser(){
        if(auth.currentUser==null){
            _eCartState.value= ECartState.Unauthenticated
        }else{
            _eCartState.value= ECartState.Authenticated
        }
    }
    fun sign(name: String,email: String,password: String){
            _eCartState.value= ECartState.Loading
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{result->
                if(result.isSuccessful){
                    val profile = UserProfileChangeRequest.Builder().setDisplayName(name).build()
                    auth.currentUser?.updateProfile(profile)?.addOnCompleteListener{
                        if(it.isSuccessful){
                            _eCartState.value= ECartState.Authenticated
                        }else{
                            _eCartState.value= ECartState.Error(it.exception?.message?: "404")
                        }
                    }
                }else{
                    _eCartState.value= ECartState.Error(result.exception?.message?: "404")
                }
            }
    }
    fun login(email: String,password: String){
        _eCartState.value= ECartState.Loading
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{result->
            if(result.isSuccessful){
                        _eCartState.value= ECartState.Authenticated
            }else{
                _eCartState.value= ECartState.Error(result.exception?.message?: "404")
            }
        }
    }
    fun signOut(){
        auth.signOut()
        _eCartState.value= ECartState.Unauthenticated
    }
}
sealed class ECartState{
    object Authenticated: ECartState()
    object Unauthenticated: ECartState()
    object Loading: ECartState()
    data class Error(val error: String): ECartState()
}
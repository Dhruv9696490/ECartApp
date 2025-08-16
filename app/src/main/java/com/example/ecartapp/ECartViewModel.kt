package com.example.ecartapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ecartapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


class ECartViewModel: ViewModel() {
    val auth= FirebaseAuth.getInstance()
    private val fireStore = Firebase.firestore
    private val _eCartState = mutableStateOf< ECartState?>(null)
    val eCartState: State<ECartState?> = _eCartState
    fun sign(name: String,email: String,password: String,phoneNumber: String){
            _eCartState.value= ECartState.Loading
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{result->
                if(result.isSuccessful){
                    val uid = result.result?.user?.uid
                    val userModel = UserModel(name,email,uid!!,emptyMap(),phoneNumber,)
                    fireStore.collection("users").document(uid).set(userModel)
                        .addOnCompleteListener {result->
                            if (result.isSuccessful){
                                _eCartState.value= ECartState.Authenticated
                            }
                           else{
                                _eCartState.value= ECartState.Error(result.exception?.message?: "404")
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
            if(result.isSuccessful && result.result!=null ){
                        _eCartState.value= ECartState.Authenticated
            }else{
                _eCartState.value= ECartState.Error(result.exception?.message?: "404")
            }
        }
    }
    fun signOut(){
        FirebaseAuth.getInstance().signOut()
        _eCartState.value= ECartState.Unauthenticated
    }
}
sealed class ECartState{
    object Authenticated: ECartState()
    object Unauthenticated: ECartState()
    object Loading: ECartState()
    data class Error(val error: String): ECartState()
}
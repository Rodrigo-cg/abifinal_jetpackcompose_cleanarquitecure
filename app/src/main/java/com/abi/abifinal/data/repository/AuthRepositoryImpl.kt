package com.abi.abifinal.data.repository

import com.abi.abifinal.domain.model.Response
import com.abi.abifinal.domain.model.User
import com.abi.abifinal.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth): AuthRepository {
    override val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Response<FirebaseUser> {

        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
            Response.Succes(result.user!!)
        }catch (e: Exception){
            e.printStackTrace()
            Response.Faliure(e)
        }

    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun singUp(user: User): Response<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            Response.Succes(result.user!!)
        }catch (e: Exception){
            e.printStackTrace()
            Response.Faliure(e
            )
        }
    }

}
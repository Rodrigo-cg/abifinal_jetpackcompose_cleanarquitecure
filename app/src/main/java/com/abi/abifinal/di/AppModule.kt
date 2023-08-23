package com.abi.abifinal.di

import com.abi.abifinal.core.Constants.POST
import com.abi.abifinal.core.Constants.USERS
import com.abi.abifinal.data.repository.AuthRepositoryImpl
import com.abi.abifinal.data.repository.UsersRepositoryImpl
import com.abi.abifinal.domain.repository.AuthRepository
import com.abi.abifinal.domain.repository.UsersRepository
import com.abi.abifinal.domain.use_cases.auth.AuthUseCase
import com.abi.abifinal.domain.use_cases.auth.GetCurrentUser
import com.abi.abifinal.domain.use_cases.auth.Login
import com.abi.abifinal.domain.use_cases.auth.Logout
import com.abi.abifinal.domain.use_cases.auth.SingUp
import com.abi.abifinal.domain.use_cases.users.Create
import com.abi.abifinal.domain.use_cases.users.GetUserById
import com.abi.abifinal.domain.use_cases.users.SaveImage
import com.abi.abifinal.domain.use_cases.users.Update
import com.abi.abifinal.domain.use_cases.users.UsersUseCases
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    @Named(USERS)
    fun providesUsersRef(db: FirebaseFirestore): CollectionReference = db.collection(USERS)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun providesUserRepository(impl: UsersRepositoryImpl): UsersRepository = impl

    @Provides
    @Singleton
    fun provideAuthCases(repository: AuthRepository) = AuthUseCase(
        getCurrentUser = GetCurrentUser(repository),
        login = Login(repository),
        logout = Logout(repository),
        singUp = SingUp(repository)
    )

    @Provides
    @Singleton
    fun provideUsersUseCases(repository: UsersRepository) = UsersUseCases(
        create = Create(repository),
        getUserById = GetUserById(repository),
        update = Update(repository),
        saveImage = SaveImage(repository)
    )

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    @Named(USERS)
    fun provideStorageUserRed(storage: FirebaseStorage): StorageReference = storage.reference.child(USERS)

    @Provides
    @Singleton
    @Named(POST)
    fun providesPostRef(db: FirebaseFirestore): CollectionReference = db.collection(POST)

    @Provides
    @Singleton
    @Named(POST)
    fun provideStoragePostRed(storage: FirebaseStorage):StorageReference = storage.reference.child(POST)

    /*@Provides
    fun providePostRepository(impl: PostRepoImpl): PostRepository = impl

    @Provides
    fun providePostUseCases(repository: PostRepository) = PostUseCases(
        createPost = CreatePost(repository),
        getPosts = GetPosts(repository),
        getPostByIdUser = GetPostByIdUser(repository),
        deletePost = DeletePost(repository),
        updatePost = UpdatePost(repository),
        likePost = LikePost(repository),
        dislikePost = DislikePost(repository)
    )*/

}
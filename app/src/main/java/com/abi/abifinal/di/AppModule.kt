package com.abi.abifinal.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.abi.abifinal.core.Constants.POST
import com.abi.abifinal.core.Constants.USERS
import com.abi.abifinal.data.ble.SensoresRecividorBleManager
import com.abi.abifinal.data.bleresults.SensoresRecevidorManager
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
import com.abi.abifinal.domain.use_cases.users.GetCurrentUserLocationUseCase
import com.abi.abifinal.domain.use_cases.users.GetParametersBle
import com.abi.abifinal.domain.use_cases.users.GetUserById
import com.abi.abifinal.domain.use_cases.users.SaveImage
import com.abi.abifinal.domain.use_cases.users.Update
import com.abi.abifinal.domain.use_cases.users.UsersUseCases
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideBluetoohAdapteEsp32(@ApplicationContext context: Context): BluetoothAdapter {
        val manager=context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter
    }
    @Provides
    @Singleton
    fun provideTempHumidityReceiveManagerEsp32(
        @ApplicationContext context: Context,
        bluetoothAdapter: BluetoothAdapter
    ): SensoresRecevidorManager {
        return SensoresRecividorBleManager(bluetoothAdapter,context)
    }
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Named(USERS)
    fun providesUsersRef(db: FirebaseFirestore): CollectionReference = db.collection(USERS)

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl):AuthRepository = impl

    @Provides
    fun providesUserRepository(impl: UsersRepositoryImpl): UsersRepository = impl

    @Provides
    fun provideAuthCases(repository: AuthRepository) = AuthUseCase(
        getCurrentUser = GetCurrentUser(repository),
        login = Login(repository),
        logout = Logout(repository),
        singUp = SingUp(repository)
    )

    @Provides
    fun provideUsersUseCases(repository: UsersRepository) = UsersUseCases(
        create = Create(repository),
        getUserById = GetUserById(repository),
        update = Update(repository),
        saveImage = SaveImage(repository),
        getParametersBle = GetParametersBle(repository),
        getCurrentUserLocationUseCase = GetCurrentUserLocationUseCase(repository)
    )

    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase=FirebaseDatabase.getInstance()

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Named(USERS)
    fun provideStorageUserRed(storage: FirebaseStorage):StorageReference = storage.reference.child(USERS)

    @Provides
    @Named(POST)
    fun providesPostRef(db: FirebaseFirestore): CollectionReference = db.collection(POST)

    @Provides
    @Named(POST)
    fun provideStoragePostRed(storage: FirebaseStorage):StorageReference = storage.reference.child(POST)

    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}
/*    @Provides
    fun providePostRepository(impl: PostRepositoryImpl): PostRepository = impl

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
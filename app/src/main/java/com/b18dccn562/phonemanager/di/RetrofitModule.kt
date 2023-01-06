package com.b18dccn562.phonemanager.di

import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.network.services.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
        return okHttpClient.addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.ApiReferences.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideAccountService(retrofit: Retrofit): AccountService {
        return retrofit.create(AccountService::class.java)
    }

    @Provides
    @Singleton
    fun provideClassService(retrofit: Retrofit): ClassService {
        return retrofit.create(ClassService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationService(retrofit: Retrofit): NotificationService {
        return retrofit.create(NotificationService::class.java)
    }

    @Provides
    @Singleton
    fun provideRequestService(retrofit: Retrofit): RequestService {
        return retrofit.create(RequestService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppUsageService(retrofit: Retrofit): AppUsageService {
        return retrofit.create(AppUsageService::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideSignUpService(retrofit: Retrofit): SignUpService {
//        return retrofit.create(SignUpService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideChangeUserInformationService(retrofit: Retrofit): UserService {
//        return retrofit.create(UserService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideSignInService(retrofit: Retrofit): SignInService {
//        return retrofit.create(SignInService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideAppService(retrofit: Retrofit): AppService {
//        return retrofit.create(AppService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideParentService(retrofit: Retrofit): ParentService {
//        return retrofit.create(ParentService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideNotificationService(retrofit: Retrofit): NotificationService {
//        return retrofit.create(NotificationService::class.java)
//    }

}
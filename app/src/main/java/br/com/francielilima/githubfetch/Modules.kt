package br.com.francielilima.githubfetch

import android.content.Context
import androidx.room.Room
import br.com.francielilima.githubfetch.BuildConfig.DEBUG
import br.com.francielilima.githubfetch.database.Database
import br.com.francielilima.githubfetch.database.NodeDao
import br.com.francielilima.githubfetch.database.RepositoryDao
import br.com.francielilima.githubfetch.features.home.HomeViewModel
import br.com.francielilima.githubfetch.network.ApiInterface
import br.com.francielilima.githubfetch.network.ApiRepository
import br.com.francielilima.githubfetch.network.ApiRepositoryImplementation
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    fun provideApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
    single { provideApi(get()) }
}

val networkModule = module {

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        if (DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        okHttpClientBuilder.build()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single { provideHttpClient() }
    single {
        val baseUrl = androidContext().getString(R.string.base_url)
        provideRetrofit(get(), baseUrl)
    }
}

val databaseModule = module {

    fun provideDatabase(context: Context): Database {
        return Room.databaseBuilder(context.applicationContext, Database::class.java, "database.db")
            .build()
    }

    fun provideRepositoryDao(database: Database): RepositoryDao {
        return database.repositoryDao()
    }

    fun provideNodeDao(database: Database): NodeDao {
        return database.nodeDao()
    }

    single { provideDatabase(androidContext()) }
    single { provideRepositoryDao(get()) }
    single { provideNodeDao(get()) }
}


val repositoryModule = module {

    fun provideApiRepository(
        apiInterface: ApiInterface,
        database: Database
    ): ApiRepository {
        return ApiRepositoryImplementation(apiInterface, database)
    }
    single { provideApiRepository(get(), get()) }

}

val viewModelModule = module {

    viewModel {
        HomeViewModel(apiRepository = get())
    }
}
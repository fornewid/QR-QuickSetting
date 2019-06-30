package soup.qr.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import soup.qr.data.local.BarcodeDao
import soup.qr.data.local.BarcodeDatabase
import soup.qr.data.local.BarcodeLocalDataSource
import soup.qr.data.local.BarcodeLocalDataSourceImpl
import javax.inject.Singleton

@Module
class LocalDataModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(dao: BarcodeDao): BarcodeLocalDataSource {
        return BarcodeLocalDataSourceImpl(dao)
    }

    @Singleton
    @Provides
    fun provideDao(database: BarcodeDatabase): BarcodeDao {
        return database.barcodeDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): BarcodeDatabase {
        return Room
            .databaseBuilder(context.applicationContext, BarcodeDatabase::class.java, "qr.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}

package soup.qr.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import soup.qr.data.local.BarcodeDao
import soup.qr.data.local.BarcodeDatabase
import soup.qr.data.local.QrLocalDataSource
import soup.qr.data.local.QrLocalDataSourceImpl

@Module
class LocalDataModule {

    @Provides
    fun provideLocalDataSource(dao: BarcodeDao): QrLocalDataSource {
        return QrLocalDataSourceImpl(dao)
    }

    @Provides
    fun provideDao(database: BarcodeDatabase): BarcodeDao {
        return database.barcodeDao()
    }

    @Provides
    fun provideDatabase(application: Application): BarcodeDatabase {
        return Room
            .databaseBuilder(application.applicationContext, BarcodeDatabase::class.java, "qr.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}

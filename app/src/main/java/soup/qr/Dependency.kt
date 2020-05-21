package soup.qr

import android.app.Application
import android.content.Context
import androidx.room.Room
import soup.qr.data.BarcodeRepository
import soup.qr.data.BarcodeRepositoryImpl
import soup.qr.data.local.BarcodeDatabase
import soup.qr.data.local.BarcodeLocalDataSourceImpl

class Dependency(application: Application) {

    private val repository: BarcodeRepository = createRepository(application.applicationContext)

    private fun createRepository(context: Context): BarcodeRepository {
        return BarcodeRepositoryImpl(
            BarcodeLocalDataSourceImpl(
                dao = createDatabase(context).barcodeDao()
            )
        )
    }

    private fun createDatabase(context: Context): BarcodeDatabase {
        return Room
            .databaseBuilder(context, BarcodeDatabase::class.java, "qr.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {

        val repository: BarcodeRepository
            get() = BarcodeApplication.dependency.repository
    }
}

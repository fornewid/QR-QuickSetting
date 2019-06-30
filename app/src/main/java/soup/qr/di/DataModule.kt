package soup.qr.di

import dagger.Module
import dagger.Provides
import soup.qr.data.BarcodeRepository
import soup.qr.data.BarcodeRepositoryImpl
import soup.qr.data.local.BarcodeLocalDataSource
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideRepository(
        localDataSource: BarcodeLocalDataSource
    ): BarcodeRepository = BarcodeRepositoryImpl(localDataSource)
}

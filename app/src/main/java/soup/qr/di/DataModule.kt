package soup.qr.di

import dagger.Module
import dagger.Provides
import soup.qr.data.QrRepository
import soup.qr.data.QrRepositoryImpl
import soup.qr.data.local.QrLocalDataSource
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideRepository(
        localDataSource: QrLocalDataSource
    ): QrRepository = QrRepositoryImpl(localDataSource)
}

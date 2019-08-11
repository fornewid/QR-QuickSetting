package soup.qr.data.local

import io.reactivex.Completable
import io.reactivex.Observable
import soup.qr.model.BarcodeHistory

interface BarcodeLocalDataSource {

    fun getHistories(): Observable<List<BarcodeHistory>>

    fun addHistory(history: BarcodeHistory): Completable

    fun deleteHistory(history: BarcodeHistory): Completable
}

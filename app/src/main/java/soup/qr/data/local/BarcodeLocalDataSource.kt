package soup.qr.data.local

import io.reactivex.Completable
import io.reactivex.Observable
import soup.qr.data.local.entity.BarcodeHistory

interface BarcodeLocalDataSource {

    fun getHistories(): Observable<List<BarcodeHistory>>

    fun addHistory(history: BarcodeHistory): Completable
}

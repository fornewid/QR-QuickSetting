package soup.qr.data

import soup.qr.data.local.BarcodeLocalDataSource

class BarcodeRepositoryImpl (private val local: BarcodeLocalDataSource) : BarcodeRepository

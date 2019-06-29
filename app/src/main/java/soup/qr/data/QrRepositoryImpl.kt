package soup.qr.data

import soup.qr.data.local.QrLocalDataSource

class QrRepositoryImpl (private val local: QrLocalDataSource) : QrRepository

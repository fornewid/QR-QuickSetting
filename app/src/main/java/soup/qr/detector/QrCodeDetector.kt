package soup.qr.detector

import soup.qr.model.QrCode

interface QrCodeDetector : Detector {

    fun setCallback(callback: Callback?)

    interface Callback {

        fun onIdle()

        fun onDetected(qrCode: QrCode)

        fun onDetectFailed()
    }
}

package soup.qr.detector

import soup.qr.detector.output.QrCode

interface QrCodeDetector : Detector {

    fun setCallback(callback: Callback?)

    interface Callback {

        fun onDetected(qrCode: QrCode)

        fun onDetectFailed()
    }
}

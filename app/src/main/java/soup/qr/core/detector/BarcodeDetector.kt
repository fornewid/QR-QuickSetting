package soup.qr.core.detector

import soup.qr.model.Barcode

interface BarcodeDetector : Detector {

    fun setCallback(callback: Callback?)

    interface Callback {

        fun onDetected(barcode: Barcode)

        fun onDetectFailed()
    }
}

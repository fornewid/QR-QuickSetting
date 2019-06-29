package soup.qr.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
sealed class QrCode : Parcelable {
    abstract val format: Int
    abstract val rawValue: String

    @Parcelize
    class Unknown(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()

    @Parcelize
    class ContactInfoQrCode(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()

    @Parcelize
    class EmailQrCode(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()

    @Parcelize
    class IsbnQrCode(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()

    @Parcelize
    class Phone(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()

    @Parcelize
    class Product(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()

    @Parcelize
    class Sms(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()

    @Parcelize
    class Text(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()

    @Parcelize
    class Url(
        override val format: Int,
        override val rawValue: String,
        val url: String
    ) : QrCode()

    @Parcelize
    class Wifi(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()

    @Parcelize
    class GeoQrCode(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()

    @Parcelize
    class CalenderEvent(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()

    @Parcelize
    class DriverLicense(
        override val format: Int,
        override val rawValue: String
    ) : QrCode()
}

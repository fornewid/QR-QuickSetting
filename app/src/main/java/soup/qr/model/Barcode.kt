package soup.qr.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class Barcode : Parcelable {
    abstract val format: Int
    abstract val rawValue: String

    @Parcelize
    class Unknown(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()

    @Parcelize
    class ContactInfo(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()

    @Parcelize
    class Email(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()

    @Parcelize
    class Isbn(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()

    @Parcelize
    class Phone(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()

    @Parcelize
    class Product(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()

    @Parcelize
    class Sms(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()

    @Parcelize
    class Text(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()

    @Parcelize
    class Url(
        override val format: Int,
        override val rawValue: String,
        val url: String
    ) : Barcode()

    @Parcelize
    class Wifi(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()

    @Parcelize
    class Geo(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()

    @Parcelize
    class CalenderEvent(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()

    @Parcelize
    class DriverLicense(
        override val format: Int,
        override val rawValue: String
    ) : Barcode()
}

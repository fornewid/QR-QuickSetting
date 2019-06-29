package soup.qr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import soup.qr.ui.BaseActivity

class QrActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

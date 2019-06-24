package soup.qr

import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class QrTile : TileService() {

    override fun onTileAdded() {
        super.onTileAdded()
        updateTileUi()
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTileUi()
    }

    override fun onClick() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivityAndCollapse(intent)
    }

    private fun updateTileUi() {
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.updateTile()
    }
}

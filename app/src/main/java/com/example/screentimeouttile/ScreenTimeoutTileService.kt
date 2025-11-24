package com.example.screentimeouttile

import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log

class ScreenTimeoutTileService : TileService() {

    // 30 seconds in milliseconds
    private val SHORT_TIMEOUT = 30000 
    
    // Int.MAX_VALUE is ~24 days. Android treats this as "effectively infinite" 
    // because real -1 is often rejected by newer OS versions.
    private val INFINITE_TIMEOUT = Int.MAX_VALUE 

    override fun onClick() {
        super.onClick()

        // 1. Check Permissions
        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivityAndCollapse(intent)
            return
        }

        // 2. Get Current State
        // We default to SHORT if we can't read the setting
        val current = try {
            Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
        } catch (e: Exception) {
            SHORT_TIMEOUT
        }

        // 3. Toggle Logic
        // If current is roughly larger than an hour (3600000ms), assume infinite mode.
        val isCurrentlyInfinite = current > 3600000 
        
        val newTimeout = if (isCurrentlyInfinite) SHORT_TIMEOUT else INFINITE_TIMEOUT

        // 4. Apply New Setting
        try {
            Settings.System.putInt(
                contentResolver,
                Settings.System.SCREEN_OFF_TIMEOUT,
                newTimeout
            )
        } catch (e: Exception) {
            Log.e("ScreenTimeout", "Failed to set timeout", e)
        }

        // 5. Update UI
        updateTileState(newTimeout)
    }

    override fun onStartListening() {
        super.onStartListening()
        // Sync tile state with system settings every time the drawer is opened
        val current = try {
            Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
        } catch (e: Exception) {
            SHORT_TIMEOUT
        }
        updateTileState(current)
    }

    private fun updateTileState(timeout: Int) {
        val tile = qsTile ?: return

        // If timeout is > 60 minutes, we treat it as Infinite for display purposes
        val isInfinite = timeout > 3600000

        if (isInfinite) {
            tile.state = Tile.STATE_ACTIVE // Highlights the tile (White/Accent color)
            tile.label = "Always On"
            tile.icon = Icon.createWithResource(this, R.drawable.ic_infinite)
        } else {
            tile.state = Tile.STATE_INACTIVE // Dims the tile (Greyed out)
            tile.label = "30 Sec"
            tile.icon = Icon.createWithResource(this, R.drawable.ic_clock)
        }

        tile.updateTile()
    }
}
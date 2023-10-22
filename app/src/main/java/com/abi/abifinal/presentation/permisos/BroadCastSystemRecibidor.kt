package com.abi.abifinal.presentation.permisos

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import android.content.Context

@Composable
fun BroadCastSystemRecibidor(
    systemAction: String,
    onSystemEvent:(intent:Intent)->Unit
){
    val context= LocalContext.current

    val currentOnSystemEvent2 by rememberUpdatedState(onSystemEvent)

    DisposableEffect(context, systemAction){
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, intent: Intent?) {
                if (intent != null) {
                    currentOnSystemEvent2(intent)
                }
            }
        }

        context.registerReceiver(broadcast, intentFilter)

        onDispose {
            context.unregisterReceiver(broadcast)
        }

    }
}
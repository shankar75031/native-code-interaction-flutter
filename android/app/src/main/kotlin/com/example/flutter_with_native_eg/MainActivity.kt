package com.example.flutter_with_native_eg

import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val CHANNEL = "course.flutter.dev/battery"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if(call.method.equals("getBatteryLevel")){
                val batteryLevel = getBatteryLevel()
                if(batteryLevel != -1){
                    result.success(batteryLevel)
                }else{
                    result.error("UNAVILABLE", "Could not fetch battery level", null)
                }
            }else{
                result.notImplemented()
            }
        }
    }

    private fun getBatteryLevel(): Int {
        var batteryLevel = -1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(BATTERY_SERVICE) as BatteryManager
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            batteryLevel = ((intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    ?: -1) * 100) / (intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1)
        }
        return batteryLevel
    }
}

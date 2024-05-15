// TODO: Update to match your plugin's package name.
package org.godotengine.plugin.android.gethardware

import android.annotation.TargetApi
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.util.Log
import android.widget.Toast
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.UsedByGodot
import java.io.BufferedReader
import java.io.FileReader

class GodotAndroidPlugin(godot: Godot): GodotPlugin(godot) {

    override fun getPluginName() = BuildConfig.GODOT_PLUGIN_NAME

    /**
     * Example showing how to declare a method that's used by Godot.
     *
     * Shows a 'Hello World' toast.
     */
    @UsedByGodot
    private fun helloWorld() {
        runOnUiThread {
            Toast.makeText(activity, "Hello World", Toast.LENGTH_LONG).show()
            Log.v(pluginName, "Hello World")
        }
    }
    @UsedByGodot
    fun getTotalRAM(): Int {
        try {
            val activityManager = activity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            val totalRAMMegabytes = memoryInfo.totalMem / (1024 * 1024 )

            Log.d("TotalRAM", "Total RAM: $totalRAMMegabytes")
            // return memoryInfo.totalMem
            val totalRAMInt = totalRAMMegabytes.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()

            return totalRAMInt
        } catch (e: Exception) {
            Log.e("TotalRAM", "Error retrieving total RAM", e)
            return -1
        }
    }

    @UsedByGodot
    private fun getCPUInfo(): String {
        val supportedABIs = Build.SUPPORTED_ABIS
        return "CPU Architecture: ${supportedABIs.joinToString()}"
    }


    @UsedByGodot
    fun getCPUSpeed(): String {
        var cpuSpeed = ""
        try {
            val reader = BufferedReader(FileReader("/proc/cpuinfo"))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (line!!.contains("BogoMIPS")) {
                    val parts = line!!.split(":")
                    if (parts.size > 1) {
                        cpuSpeed = parts[1].trim()
                        break
                    }
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cpuSpeed
    }

    @UsedByGodot
    fun getNonVolatileMemoryInfo(): Int {
        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        val statFs = StatFs(externalStorageDirectory.path)

        val totalMemory = statFs.blockCountLong * statFs.blockSizeLong
        val totalMemoryInGB = totalMemory / (1024 * 1024)
        val totalMemoryInt = totalMemoryInGB.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()

        return totalMemoryInt
    }


    @UsedByGodot
    fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE
    }
}

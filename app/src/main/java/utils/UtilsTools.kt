package utils

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import java.io.InputStreamReader

object UtilsTools {
    fun parseJsonFromAssets(context: Context, fileName: String, type: Class<*>): Any? {
        return try {
            val assetManager: AssetManager = context.assets
            val inputStream = assetManager.open(fileName)
            val reader = InputStreamReader(inputStream, "UTF-8")
            val jsonString = reader.readText()
            Gson().fromJson(jsonString, type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun parseJsonFromAssets2(context: Context, fileName: String): String {
        return try {
            val assetManager: AssetManager = context.assets
            val inputStream = assetManager.open(fileName)
            val reader = InputStreamReader(inputStream, "UTF-8")
            return reader.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
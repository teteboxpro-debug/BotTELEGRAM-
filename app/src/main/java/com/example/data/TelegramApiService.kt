package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class TelegramBotInfo(
    val isValid: Boolean,
    val id: Long? = null,
    val firstName: String = "",
    val username: String = "",
    val errorMessage: String = "",
    val rawResponse: String = ""
)

class TelegramApiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .build()

    suspend fun checkBotToken(token: String): TelegramBotInfo = withContext(Dispatchers.IO) {
        val cleanToken = token.trim()
        if (cleanToken.isEmpty() || !cleanToken.contains(":")) {
            return@withContext TelegramBotInfo(
                isValid = false,
                errorMessage = "Invalid Token Format. Token must contain ':' separating ID and hash."
            )
        }

        val url = "https://api.telegram.org/bot$cleanToken/getMe"
        try {
            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val body = response.body?.string().orEmpty()
                if (response.isSuccessful && body.isNotEmpty()) {
                    val json = JSONObject(body)
                    if (json.optBoolean("ok", false)) {
                        val result = json.getJSONObject("result")
                        return@withContext TelegramBotInfo(
                            isValid = true,
                            id = result.optLong("id"),
                            firstName = result.optString("first_name", "Bot"),
                            username = result.optString("username", ""),
                            rawResponse = body
                        )
                    }
                }
                // Check error description
                var errorDesc = "HTTP Error ${response.code}"
                if (body.isNotEmpty()) {
                    try {
                        val errJson = JSONObject(body)
                        errorDesc = errJson.optString("description", errorDesc)
                    } catch (_: Exception) {
                        errorDesc = body.take(150)
                    }
                }
                return@withContext TelegramBotInfo(
                    isValid = false,
                    errorMessage = errorDesc,
                    rawResponse = body
                )
            }
        } catch (e: Exception) {
            return@withContext TelegramBotInfo(
                isValid = false,
                errorMessage = e.localizedMessage ?: "Connection failed (Check network/firewall)"
            )
        }
    }
}

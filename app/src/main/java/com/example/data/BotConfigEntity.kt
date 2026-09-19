package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bot_config")
data class BotConfigEntity(
    @PrimaryKey val id: Int = 1,
    val botToken: String = "8544041324:AAFynDxXGE3B0fy5KuaE8dNpYar-DnAnZMY",
    val botId: String = "8544041324",
    val botName: String = "Linkvip_5bot",
    val botUsername: String = "Linkvip_5bot",
    val adminUsername: String = "teteboxvip2",
    val startText: String = "🌟 **Welcome to the Official Channel Bot** 🌟\n\nPlease accept our rules to continue:\n1. Respect all members.\n2. No spam or external links.\n\n👇 **Click Accept to proceed** 👇",
    val startPhotoUrl: String = "https://graph.org/file/982debac24a1b4d9868fe-508f86f2a0e58c6dbf.jpg",
    val acceptText: String = "✅ **You have successfully accepted the rules!**\n\nChoose an option below to continue:",
    val acceptPhotoUrl: String = "https://graph.org/file/93048ab4f3edc47bbbaa8-dcccc0c3a0b7a644cf.jpg",
    val freeChannelLink: String = "https://rentry.co/Teteboxvip",
    val storePaymentLink: String = "https://t.me/SoSoeteboxvipbot?start=buy_O_LAOyTP05",
    val websiteLink: String = "https://eteboxvip.carrd.co/",
    val declineText: String = "❌ **Sorry, you cannot proceed without accepting the rules.**\n\nPlease click below to try again.",
    val declinePhotoUrl: String = "https://graph.org/file/e26e4b9a5521a4302823a-54870e5a9d8ac9ed68.jpg",
    val lastChecked: Long = 0L,
    val isTokenValid: Boolean? = null,
    val statusMessage: String = "Ready"
)

@Entity(tableName = "token_history")
data class TokenHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val token: String,
    val dateAdded: Long = System.currentTimeMillis(),
    val status: String = "ACTIVE", // ACTIVE, REPLACED, BANNED
    val note: String = ""
)

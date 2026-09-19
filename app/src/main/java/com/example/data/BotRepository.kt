package com.example.data

import kotlinx.coroutines.flow.Flow

class BotRepository(
    private val botDao: BotDao,
    private val apiService: TelegramApiService = TelegramApiService()
) {
    val configFlow: Flow<BotConfigEntity?> = botDao.getBotConfig()
    val tokenHistoryFlow: Flow<List<TokenHistoryEntity>> = botDao.getAllTokens()

    suspend fun initializeDefaultsIfNeeded() {
        val existing = botDao.getBotConfigSync()
        if (existing == null) {
            val defaultConfig = BotConfigEntity()
            botDao.insertOrUpdateConfig(defaultConfig)
            botDao.insertTokenHistory(
                TokenHistoryEntity(
                    token = defaultConfig.botToken,
                    status = "ACTIVE",
                    note = "Initial bot token provided"
                )
            )
        }
    }

    suspend fun updateConfig(config: BotConfigEntity) {
        botDao.insertOrUpdateConfig(config)
    }

    suspend fun swapBotToken(newToken: String, note: String = "Swapped via Anti-Ban Vault"): TelegramBotInfo {
        val clean = newToken.trim()
        val info = apiService.checkBotToken(clean)
        val current = botDao.getBotConfigSync() ?: BotConfigEntity()

        val updated = current.copy(
            botToken = clean,
            botName = if (info.isValid && info.firstName.isNotEmpty()) info.firstName else current.botName,
            botUsername = if (info.isValid && info.username.isNotEmpty()) info.username else current.botUsername,
            lastChecked = System.currentTimeMillis(),
            isTokenValid = info.isValid,
            statusMessage = if (info.isValid) "Connected as @${info.username}" else "Error: ${info.errorMessage}"
        )
        botDao.insertOrUpdateConfig(updated)

        // Archive old tokens
        val historyEntry = TokenHistoryEntity(
            token = clean,
            status = if (info.isValid) "ACTIVE" else "INVALID",
            note = note
        )
        botDao.insertTokenHistory(historyEntry)
        return info
    }

    suspend fun verifyCurrentToken(): TelegramBotInfo {
        val current = botDao.getBotConfigSync() ?: BotConfigEntity()
        val info = apiService.checkBotToken(current.botToken)
        val updated = current.copy(
            botName = if (info.isValid && info.firstName.isNotEmpty()) info.firstName else current.botName,
            botUsername = if (info.isValid && info.username.isNotEmpty()) info.username else current.botUsername,
            lastChecked = System.currentTimeMillis(),
            isTokenValid = info.isValid,
            statusMessage = if (info.isValid) "Connected as @${info.username}" else "Error: ${info.errorMessage}"
        )
        botDao.insertOrUpdateConfig(updated)
        return info
    }

    suspend fun resetToDefault() {
        val defaultConfig = BotConfigEntity()
        botDao.insertOrUpdateConfig(defaultConfig)
    }
}

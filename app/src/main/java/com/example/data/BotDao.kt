package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BotDao {
    @Query("SELECT * FROM bot_config WHERE id = 1 LIMIT 1")
    fun getBotConfig(): Flow<BotConfigEntity?>

    @Query("SELECT * FROM bot_config WHERE id = 1 LIMIT 1")
    suspend fun getBotConfigSync(): BotConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateConfig(config: BotConfigEntity)

    @Query("SELECT * FROM token_history ORDER BY id DESC")
    fun getAllTokens(): Flow<List<TokenHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTokenHistory(tokenHistory: TokenHistoryEntity)

    @Query("UPDATE token_history SET status = 'REPLACED' WHERE id != :activeId AND status = 'ACTIVE'")
    suspend fun archiveOldTokens(activeId: Int)

    @Query("UPDATE token_history SET status = :status WHERE token = :token")
    suspend fun updateTokenStatus(token: String, status: String)
}

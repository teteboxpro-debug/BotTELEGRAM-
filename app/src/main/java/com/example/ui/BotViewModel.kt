package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BotConfigEntity
import com.example.data.BotDatabase
import com.example.data.BotRepository
import com.example.data.TelegramBotInfo
import com.example.data.TokenHistoryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class BotScreenState {
    START,
    ACCEPTED,
    DECLINED
}

enum class AppNavTab {
    SIMULATOR,
    TOKEN_VAULT,
    FLOW_EDITOR,
    DEPLOY_CODE
}

data class BotUiState(
    val selectedTab: AppNavTab = AppNavTab.SIMULATOR,
    val simulationState: BotScreenState = BotScreenState.START,
    val isCheckingApi: Boolean = false,
    val lastCheckResult: TelegramBotInfo? = null,
    val userMessage: String? = null
)

class BotViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BotRepository

    val botConfig: StateFlow<BotConfigEntity?>
    val tokenHistory: StateFlow<List<TokenHistoryEntity>>

    private val _uiState = MutableStateFlow(BotUiState())
    val uiState: StateFlow<BotUiState> = _uiState.asStateFlow()

    init {
        val db = BotDatabase.getDatabase(application)
        repository = BotRepository(db.botDao())
        botConfig = repository.configFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
        tokenHistory = repository.tokenHistoryFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        viewModelScope.launch {
            repository.initializeDefaultsIfNeeded()
        }
    }

    fun selectTab(tab: AppNavTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun triggerSimulatorAction(action: String) {
        when (action) {
            "/start" -> _uiState.value = _uiState.value.copy(simulationState = BotScreenState.START)
            "/accept" -> _uiState.value = _uiState.value.copy(simulationState = BotScreenState.ACCEPTED)
            "/decline" -> _uiState.value = _uiState.value.copy(simulationState = BotScreenState.DECLINED)
        }
    }

    fun checkCurrentToken() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCheckingApi = true)
            val result = repository.verifyCurrentToken()
            _uiState.value = _uiState.value.copy(
                isCheckingApi = false,
                lastCheckResult = result,
                userMessage = if (result.isValid) "✅ Token is Active on Telegram: @${result.username}" else "⚠️ Telegram Error: ${result.errorMessage}"
            )
        }
    }

    fun swapToken(newToken: String, note: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCheckingApi = true)
            val result = repository.swapBotToken(newToken, note)
            _uiState.value = _uiState.value.copy(
                isCheckingApi = false,
                lastCheckResult = result,
                userMessage = if (result.isValid) "✅ Token swapped successfully! Bot @${result.username} is connected." else "Token updated, but API reported: ${result.errorMessage}"
            )
        }
    }

    fun updateConfig(updated: BotConfigEntity) {
        viewModelScope.launch {
            repository.updateConfig(updated)
            _uiState.value = _uiState.value.copy(userMessage = "Config saved successfully!")
        }
    }

    fun resetDefaults() {
        viewModelScope.launch {
            repository.resetToDefault()
            _uiState.value = _uiState.value.copy(userMessage = "Reset to original defaults!")
        }
    }

    fun clearUserMessage() {
        _uiState.value = _uiState.value.copy(userMessage = null)
    }
}

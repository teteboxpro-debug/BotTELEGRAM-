package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.BotConfigEntity
import com.example.ui.AppNavTab
import com.example.ui.BotViewModel
import com.example.ui.components.BotSimulatorScreen
import com.example.ui.components.DeployGuideScreen
import com.example.ui.components.FlowEditorScreen
import com.example.ui.components.TokenVaultScreen
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TelegramBotStudioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TelegramBotStudioTheme {
                MainAppScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen(
    viewModel: BotViewModel = viewModel()
) {
    val botConfig by viewModel.botConfig.collectAsStateWithLifecycle()
    val tokenHistory by viewModel.tokenHistory.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = uiState.selectedTab == AppNavTab.SIMULATOR,
                    onClick = { viewModel.selectTab(AppNavTab.SIMULATOR) },
                    icon = { Icon(Icons.Default.SmartToy, contentDescription = "المحاكي") },
                    label = { Text("المحاكي") },
                    modifier = Modifier.testTag("tab_simulator")
                )
                NavigationBarItem(
                    selected = uiState.selectedTab == AppNavTab.TOKEN_VAULT,
                    onClick = { viewModel.selectTab(AppNavTab.TOKEN_VAULT) },
                    icon = { Icon(Icons.Default.Shield, contentDescription = "خزنة التوكن") },
                    label = { Text("خزنة التوكن") },
                    modifier = Modifier.testTag("tab_token_vault")
                )
                NavigationBarItem(
                    selected = uiState.selectedTab == AppNavTab.FLOW_EDITOR,
                    onClick = { viewModel.selectTab(AppNavTab.FLOW_EDITOR) },
                    icon = { Icon(Icons.Default.Edit, contentDescription = "الرسائل") },
                    label = { Text("الرسائل") },
                    modifier = Modifier.testTag("tab_flow_editor")
                )
                NavigationBarItem(
                    selected = uiState.selectedTab == AppNavTab.DEPLOY_CODE,
                    onClick = { viewModel.selectTab(AppNavTab.DEPLOY_CODE) },
                    icon = { Icon(Icons.Default.Code, contentDescription = "الكود والنشر") },
                    label = { Text("الكود والنشر") },
                    modifier = Modifier.testTag("tab_deploy_code")
                )
            }
        }
    ) { innerPadding ->
        val config = botConfig
        if (config == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = TelegramBlue)
            }
        } else {
            when (uiState.selectedTab) {
                AppNavTab.SIMULATOR -> {
                    BotSimulatorScreen(
                        config = config,
                        simulationState = uiState.simulationState,
                        onAction = { action -> viewModel.triggerSimulatorAction(action) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                AppNavTab.TOKEN_VAULT -> {
                    TokenVaultScreen(
                        config = config,
                        tokenHistory = tokenHistory,
                        isCheckingApi = uiState.isCheckingApi,
                        lastCheckResult = uiState.lastCheckResult,
                        onCheckApi = { viewModel.checkCurrentToken() },
                        onSwapToken = { newToken, note -> viewModel.swapToken(newToken, note) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                AppNavTab.FLOW_EDITOR -> {
                    FlowEditorScreen(
                        config = config,
                        onSave = { updated -> viewModel.updateConfig(updated) },
                        onReset = { viewModel.resetDefaults() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                AppNavTab.DEPLOY_CODE -> {
                    DeployGuideScreen(
                        config = config,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

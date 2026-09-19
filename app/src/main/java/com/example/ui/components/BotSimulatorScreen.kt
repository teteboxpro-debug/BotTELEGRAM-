package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.data.BotConfigEntity
import com.example.ui.BotScreenState
import com.example.ui.theme.TelegramAccentGreen
import com.example.ui.theme.TelegramAccentRed
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TelegramBubble
import com.example.ui.theme.TelegramChatBg

@Composable
fun BotSimulatorScreen(
    config: BotConfigEntity,
    simulationState: BotScreenState,
    onAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val currentPhotoUrl = when (simulationState) {
        BotScreenState.START -> config.startPhotoUrl
        BotScreenState.ACCEPTED -> config.acceptPhotoUrl
        BotScreenState.DECLINED -> config.declinePhotoUrl
    }

    val currentCaption = when (simulationState) {
        BotScreenState.START -> config.startText
        BotScreenState.ACCEPTED -> config.acceptText
        BotScreenState.DECLINED -> config.declineText
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Telegram Chat Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(TelegramBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "Bot Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = config.botName.ifEmpty { "Official Channel Bot" },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = TelegramBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "bot • @${config.botUsername.ifEmpty { "ChannelBot" }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { onAction("/start") },
                    modifier = Modifier.testTag("reset_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Restart Bot Simulation",
                        tint = TelegramBlue
                    )
                }
            }
        }

        // Status banner
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "معاينة حية وتفاعلية لرسائل البوت على تيليجرام",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Surface(
                    color = when (simulationState) {
                        BotScreenState.START -> TelegramBlue
                        BotScreenState.ACCEPTED -> TelegramAccentGreen
                        BotScreenState.DECLINED -> TelegramAccentRed
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = when (simulationState) {
                            BotScreenState.START -> "/start (الترحيب)"
                            BotScreenState.ACCEPTED -> "/accept (مقبول)"
                            BotScreenState.DECLINED -> "/decline (مرفوض)"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Chat Container
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(TelegramChatBg)
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Telegram Message Bubble with Photo + Caption + Inline Keyboard
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TelegramBubble),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("telegram_message_card")
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Photo
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(Color(0xFF1E293B)),
                            contentAlignment = Alignment.Center
                        ) {
                            SubcomposeAsyncImage(
                                model = currentPhotoUrl,
                                contentDescription = "Bot Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                loading = {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(32.dp),
                                            color = TelegramBlue
                                        )
                                    }
                                },
                                error = {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.SmartToy,
                                            contentDescription = "Photo fallback",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(36.dp)
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            text = "صورة البوت\n$currentPhotoUrl",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.LightGray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            )
                        }

                        // Caption Text
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = currentCaption,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    lineHeight = 22.sp,
                                    color = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Inline Buttons Markup based on state
                            when (simulationState) {
                                BotScreenState.START -> {
                                    // Row 1: I Accept
                                    TelegramInlineButton(
                                        text = "✅ I Accept",
                                        isHighlighted = true,
                                        highlightColor = TelegramAccentGreen,
                                        testTag = "btn_accept",
                                        onClick = { onAction("/accept") }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    // Row 2: I Decline
                                    TelegramInlineButton(
                                        text = "❌ I Decline",
                                        isHighlighted = false,
                                        highlightColor = TelegramAccentRed,
                                        testTag = "btn_decline",
                                        onClick = { onAction("/decline") }
                                    )
                                }

                                BotScreenState.ACCEPTED -> {
                                    // Button 1: Free Channel Link
                                    TelegramInlineUrlButton(
                                        text = "𝐟𝐫𝐞𝐞 𝐜𝐡𝐚𝐧𝐧𝐞𝐥 𝗹𝗶𝗻𝗸",
                                        url = config.freeChannelLink,
                                        testTag = "btn_free_channel",
                                        onClick = {
                                            openUrlSafely(context, config.freeChannelLink)
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    // Button 2: Store & Payment
                                    TelegramInlineUrlButton(
                                        text = "💳 𝗦𝘁𝗼𝗿𝗲 & 𝗣𝗮𝘆𝗺𝗲𝗻𝘁",
                                        url = config.storePaymentLink,
                                        testTag = "btn_store_payment",
                                        onClick = {
                                            openUrlSafely(context, config.storePaymentLink)
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    // Button 3: Visit Website
                                    TelegramInlineUrlButton(
                                        text = "🌐 Visit Website",
                                        url = config.websiteLink,
                                        testTag = "btn_website",
                                        onClick = {
                                            openUrlSafely(context, config.websiteLink)
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    // Back to start test button
                                    FilledTonalButton(
                                        onClick = { onAction("/start") },
                                        modifier = Modifier.fillMaxWidth().testTag("btn_back_to_start")
                                    ) {
                                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(6.dp))
                                        Text("إعادة الاختبار من البداية")
                                    }
                                }

                                BotScreenState.DECLINED -> {
                                    TelegramInlineButton(
                                        text = "🔙 Back to Start",
                                        isHighlighted = true,
                                        highlightColor = TelegramBlue,
                                        testTag = "btn_back_start",
                                        onClick = { onAction("/start") }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Interactive Hint
                Surface(
                    color = Color(0x330088CC),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "💡 كيف يعمل هذا البوت؟",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TelegramBlue
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "• عند تشغيل البوت يظهر أمر الترحيب والشروط.\n• النقر على ✅ I Accept يفتح روابط القناة والمتجر والموقع.\n• النقر على ❌ I Decline يطلب من المستخدم العودة والموافقة.\n• البيانات والروابط محفوظة في خزنة التطبيق ويمكن تبديل التوكن فوراً عند أي حظر!",
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TelegramInlineButton(
    text: String,
    isHighlighted: Boolean,
    highlightColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF243447),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isHighlighted) 1.5.dp else 1.dp,
                color = if (isHighlighted) highlightColor.copy(alpha = 0.6f) else Color(0x33FFFFFF),
                shape = RoundedCornerShape(10.dp)
            )
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = if (isHighlighted) highlightColor else Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TelegramInlineUrlButton(
    text: String,
    url: String,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF203247),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TelegramBlue.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = Color(0xFF7DD3FC),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.OpenInBrowser,
                contentDescription = "External Link",
                tint = Color(0xFF7DD3FC),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private fun openUrlSafely(context: android.content.Context, url: String) {
    try {
        val parsed = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(parsed))
        context.startActivity(intent)
    } catch (_: Exception) {
    }
}

package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BotConfigEntity
import com.example.data.TelegramBotInfo
import com.example.data.TokenHistoryEntity
import com.example.ui.theme.TelegramAccentGreen
import com.example.ui.theme.TelegramAccentRed
import com.example.ui.theme.TelegramBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TokenVaultScreen(
    config: BotConfigEntity,
    tokenHistory: List<TokenHistoryEntity>,
    isCheckingApi: Boolean,
    lastCheckResult: TelegramBotInfo?,
    onCheckApi: () -> Unit,
    onSwapToken: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showToken by remember { mutableStateOf(false) }
    var newTokenInput by remember { mutableStateOf("") }
    var noteInput by remember { mutableStateOf("") }
    var showSwapDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Direct Question Answer Card (High-priority reassurance)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
            ),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, TelegramBlue.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth().testTag("anti_ban_explanation_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(TelegramBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Shield",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "نعم! بياناتك في أمان تام عند الحظر",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "إجابة استفسارك بخصوص تغيير التوكن والبيانات",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "• تيليجرام عند حظر أي بوت، يقوم بحظر المعرّف (Token / @Username) فقط.\n" +
                            "• قواعد البيانات، رسائل الترحيب، الروابط، والمشتركين مخزنة بشكل مستقل في نظامك (SQLite / Room).\n" +
                            "• عندما يتعرض البوت للحظر، كل ما عليك فعله هو فتح @BotFather وصنع بوت جديد وأخذ التوكن الجديد ووضعه هنا، وسيعمل فوراً مع نفس الرسائل والقواعد دون فقدان أي شيء!\n" +
                            "• ملاحظة: البوت الرسمي لا يحتاج API ID أو API Hash؛ يحتاج فقط Bot Token.",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active Bot Status Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("active_bot_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "Key",
                            tint = TelegramBlue,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "التوكن النشط حالياً",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Surface(
                        color = when (config.isTokenValid) {
                            true -> TelegramAccentGreen.copy(alpha = 0.2f)
                            false -> TelegramAccentRed.copy(alpha = 0.2f)
                            null -> Color(0xFF64748B).copy(alpha = 0.2f)
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = when (config.isTokenValid) {
                                    true -> Icons.Default.CheckCircle
                                    false -> Icons.Default.Error
                                    null -> Icons.Default.HelpOutline
                                },
                                contentDescription = null,
                                tint = when (config.isTokenValid) {
                                    true -> TelegramAccentGreen
                                    false -> TelegramAccentRed
                                    null -> Color(0xFF64748B)
                                },
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = when (config.isTokenValid) {
                                    true -> "نشط على تيليجرام"
                                    false -> "غير صالح / محظور"
                                    null -> "لم يتم الفحص بعد"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = when (config.isTokenValid) {
                                    true -> TelegramAccentGreen
                                    false -> TelegramAccentRed
                                    null -> Color(0xFF64748B)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bot ID Display
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "معرف البوت (Username):",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "@${config.botUsername}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = TelegramBlue
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Admin Account Display
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "حساب الأدمن المسؤول:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "@${config.adminUsername}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = TelegramAccentGreen
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Token Display Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (showToken) config.botToken else maskToken(config.botToken),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        Row {
                            IconButton(onClick = { showToken = !showToken }) {
                                Icon(
                                    imageVector = if (showToken) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle token visibility",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(onClick = {
                                copyToClipboard(context, config.botToken, "تم نسخ التوكن")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Token",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Check API Button
                Button(
                    onClick = onCheckApi,
                    enabled = !isCheckingApi,
                    modifier = Modifier.fillMaxWidth().testTag("check_api_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue)
                ) {
                    if (isCheckingApi) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("جارِ الاتصال بخوادم تيليجرام...")
                    } else {
                        Icon(imageVector = Icons.Default.NetworkCheck, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("فحص صلاحية التوكن (getMe API)")
                    }
                }

                // Check Result Banner
                if (lastCheckResult != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = if (lastCheckResult.isValid) TelegramAccentGreen.copy(alpha = 0.15f) else TelegramAccentRed.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (lastCheckResult.isValid) "نتيجة فحص Telegram الرسمية: البوت يعمل!" else "نتيجة فحص Telegram الرسمية: فشل الاتصال",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (lastCheckResult.isValid) TelegramAccentGreen else TelegramAccentRed
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = if (lastCheckResult.isValid) {
                                    "• الاسم: ${lastCheckResult.firstName}\n• المعرف: @${lastCheckResult.username}\n• ID: ${lastCheckResult.id}"
                                } else {
                                    lastCheckResult.errorMessage
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hot Swap Token Section (The Anti-Ban Solution)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("token_hotswap_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Swap",
                        tint = TelegramBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تبديل التوكن السريع (عند تعرض البوت للحظر)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "إذا تم حظر بوتك، فقط أدخل التوكن الجديد هنا واضغط زر التبديل. سيتم تفعيل البوت الجديد فوراً مع الاحتفاظ بجميع الرسائل والروابط كما هي!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = newTokenInput,
                    onValueChange = { newTokenInput = it },
                    label = { Text("توكن البوت الجديد (من @BotFather)") },
                    placeholder = { Text("123456789:ABCdefGhIJKlmNoPQRsTUVwxyZ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_new_token")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    label = { Text("ملاحظة التبديل (اختياري)") },
                    placeholder = { Text("مثلاً: تم استبدال البوت بعد الحظر") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_swap_note")
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (newTokenInput.trim().isNotEmpty()) {
                            onSwapToken(newTokenInput.trim(), noteInput.ifEmpty { "Anti-Ban Token Swap" })
                            newTokenInput = ""
                            noteInput = ""
                            Toast.makeText(context, "تم حفظ وتفعيل التوكن الجديد بنجاح!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "يرجى إدخال التوكن أولاً", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_confirm_swap"),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramAccentGreen)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("حفظ وتبديل التوكن فوراً مع بقاء البيانات")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // History of Tokens Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "سجل التوكنات السابقة",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (tokenHistory.isEmpty()) {
                    Text(
                        text = "لا توجد توكنات سابقة مسجلة.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    val sdf = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
                    tokenHistory.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = maskToken(item.token),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "${item.note.ifEmpty { "توكن" }} • ${sdf.format(Date(item.dateAdded))}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                color = if (item.token == config.botToken) TelegramAccentGreen.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (item.token == config.botToken) "النشط" else "سابق",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (item.token == config.botToken) TelegramAccentGreen else Color.Gray,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun maskToken(token: String): String {
    if (token.length <= 14) return token
    val prefix = token.take(6)
    val suffix = token.takeLast(6)
    return "$prefix••••••••••••$suffix"
}

private fun copyToClipboard(context: Context, text: String, toastMsg: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Telegram Bot Token", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
}

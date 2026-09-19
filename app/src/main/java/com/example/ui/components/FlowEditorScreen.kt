package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.BotConfigEntity
import com.example.ui.theme.TelegramAccentGreen
import com.example.ui.theme.TelegramBlue

@Composable
fun FlowEditorScreen(
    config: BotConfigEntity,
    onSave: (BotConfigEntity) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var startText by remember(config) { mutableStateOf(config.startText) }
    var startPhotoUrl by remember(config) { mutableStateOf(config.startPhotoUrl) }

    var acceptText by remember(config) { mutableStateOf(config.acceptText) }
    var acceptPhotoUrl by remember(config) { mutableStateOf(config.acceptPhotoUrl) }

    var freeChannelLink by remember(config) { mutableStateOf(config.freeChannelLink) }
    var storePaymentLink by remember(config) { mutableStateOf(config.storePaymentLink) }
    var websiteLink by remember(config) { mutableStateOf(config.websiteLink) }

    var declineText by remember(config) { mutableStateOf(config.declineText) }
    var declinePhotoUrl by remember(config) { mutableStateOf(config.declinePhotoUrl) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = TelegramBlue)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "محرر محتوى ورسائل البوت",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Text(
            text = "يمكنك تعديل أي نص أو رابط أو صورة وسيقوم النظام بتحديث المحاكي وتوليد الأكواد الجاهزة تلقائياً!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card 1: Start Message
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. رسالة البداية /start (شروط القناة)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TelegramBlue)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = startPhotoUrl,
                    onValueChange = { startPhotoUrl = it },
                    label = { Text("رابط صورة البداية (Photo URL)") },
                    leadingIcon = { Icon(Icons.Default.Photo, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_start_photo")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = startText,
                    onValueChange = { startText = it },
                    label = { Text("نص الترحيب والشروط") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth().testTag("input_start_text")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card 2: Accept Message & Links
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2. رسالة القبول /accept والروابط الرسمية",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TelegramAccentGreen)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = acceptPhotoUrl,
                    onValueChange = { acceptPhotoUrl = it },
                    label = { Text("رابط صورة القبول (Photo URL)") },
                    leadingIcon = { Icon(Icons.Default.Photo, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_accept_photo")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = acceptText,
                    onValueChange = { acceptText = it },
                    label = { Text("نص رسالة النجاح والقبول") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("input_accept_text")
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "أزرار الروابط (Inline Keyboard Buttons):",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = freeChannelLink,
                    onValueChange = { freeChannelLink = it },
                    label = { Text("رابط زر 'FREE CHANNEL'") },
                    leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_free_channel")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = storePaymentLink,
                    onValueChange = { storePaymentLink = it },
                    label = { Text("رابط زر 'VIP STORE'") },
                    leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_store_payment")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = websiteLink,
                    onValueChange = { websiteLink = it },
                    label = { Text("رابط زر 'VIST WEP'") },
                    leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_website")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card 3: Decline Message
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "3. رسالة الرفض /decline",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = declinePhotoUrl,
                    onValueChange = { declinePhotoUrl = it },
                    label = { Text("رابط صورة الرفض (Photo URL)") },
                    leadingIcon = { Icon(Icons.Default.Photo, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_decline_photo")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = declineText,
                    onValueChange = { declineText = it },
                    label = { Text("نص رسالة الرفض وتوجيه العودة") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("input_decline_text")
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Button(
            onClick = {
                val updated = config.copy(
                    startText = startText,
                    startPhotoUrl = startPhotoUrl,
                    acceptText = acceptText,
                    acceptPhotoUrl = acceptPhotoUrl,
                    freeChannelLink = freeChannelLink,
                    storePaymentLink = storePaymentLink,
                    websiteLink = websiteLink,
                    declineText = declineText,
                    declinePhotoUrl = declinePhotoUrl
                )
                onSave(updated)
                Toast.makeText(context, "تم حفظ التعديلات بنجاح!", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
            modifier = Modifier.fillMaxWidth().testTag("btn_save_flow")
        ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("حفظ التعديلات في قاعدة البيانات")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth().testTag("btn_reset_flow")
        ) {
            Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("استعادة الإعدادات الأصلية")
        }
    }
}

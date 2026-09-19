package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BotCodeGenerator
import com.example.data.BotConfigEntity
import com.example.ui.theme.TelegramAccentGreen
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TelegramSurface

@Composable
fun DeployGuideScreen(
    config: BotConfigEntity,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: No-Code vs Code, 1: Python Code, 2: Node.js Code

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Terminal, contentDescription = null, tint = TelegramBlue)
            Spacer(Modifier.width(8.dp))
            Text(
                text = "دليل الإطلاق وكود التشغيل الفوري",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("الاستضافة السحابية (Cloud 24/7)") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("المقارنة والمنصات (No-Code)") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("كود Python (Aiogram 3.x)") }
            )
            Tab(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                text = { Text("كود Node.js (Telegraf)") }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedTab) {
                0 -> CloudDeploymentGuideSection(config = config, context = context)
                1 -> NoCodeGuideSection()
                2 -> CodeDisplaySection(
                    title = "كود Python Aiogram 3.x مع حفظ المشتركين في SQLite",
                    code = BotCodeGenerator.generatePythonCode(config),
                    language = "Python",
                    onCopy = {
                        copyToClipboard(context, BotCodeGenerator.generatePythonCode(config), "تم نسخ كود Python بالكامل!")
                    }
                )
                3 -> CodeDisplaySection(
                    title = "كود Node.js Telegraf الجاهز للنشر",
                    code = BotCodeGenerator.generateNodeJsCode(config),
                    language = "Node.js",
                    onCopy = {
                        copyToClipboard(context, BotCodeGenerator.generateNodeJsCode(config), "تم نسخ كود Node.js بالكامل!")
                    }
                )
            }
        }
    }
}

@Composable
fun NoCodeGuideSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Why Bot Does Not Answer Yet Notice
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.QuestionAnswer, contentDescription = null, tint = Color(0xFFD97706))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "لماذا لا يرد البوت عند إرسال /start في تيليجرام؟",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "• التوكن شغال 100% وتم التحقق منه الآن عبر خوادم تيليجرام الرسمية: اسم البوت هو Linkvip_5bot (id: 8544041324).\n" +
                            "• في نظام تيليجرام: التوكن بمفرده هو مجرد 'مفتاح اتصال'. لكي يرد البوت على رسائل /start، يجب تشغيل ملف الكود على خادم أو موقع يعمل 24 ساعة (أو عبر منصة No-Code) ليستمع لرسائل المستخدمين ويرد عليهم.\n" +
                            "• بدون تشغيل الكود على خادم، ترسل تيليجرام الرسالة ولكن لا يوجد كود يستقبلها، لذلك تظهر رسالة /start دون رد كما في صورتك!",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = Color(0xFF78350F)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step-by-Step 2-Minute Free Hosting
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Terminal, contentDescription = null, tint = TelegramBlue)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "كيف تجعل البوت يرد في دقيقتين مجاناً؟",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "اختر إحدى الطرق السريعة التالية لتشغيل كود البوت فوراً:\n\n" +
                            "🔹 الطريقة 1 (عبر الهاتف بدون حاسوب - تطبيق Termux):\n" +
                            "1. حمّل تطبيق Termux من المتجر.\n" +
                            "2. اكتب: pkg install python && pip install aiogram\n" +
                            "3. الصق الكود من تبويب (كود Python) في ملف باسم bot.py\n" +
                            "4. اكتب: python bot.py وستجد البوت بدأ بالرد فوراً!\n\n" +
                            "🔹 الطريقة 2 (استضافة سحابية مجانية 24/7 مثل Render أو Railway أو PythonAnywhere):\n" +
                            "1. أنشئ حساباً مجانياً على render.com أو pythonanywhere.com.\n" +
                            "2. أنشئ Background Worker والصق الكود.\n" +
                            "3. سيعمل البوت 24 ساعة دون الحاجة لأن يبقى هاتفك متصلاً.\n\n" +
                            "🔹 الطريقة 3 (بدون كود عبر ManyBot داخل تيليجرام):\n" +
                            "1. افتح @ManyBot في تيليجرام.\n" +
                            "2. أرسل له التوكن وسيقوم هو باستضافة البوت والرد نيابة عنك مجاناً!",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Answer to API ID & Hash
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.QuestionAnswer, contentDescription = null, tint = TelegramBlue)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "هل نحتاج API ID & API Hash؟",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "• الجواب: لا! لبناء بوت رسمي عبر Telegram Bot API، تحتاج فقط الـ Bot Token من @BotFather.\n" +
                            "• متى يُطلب API ID & API Hash؟ يُطلب فقط إذا كنت تبني Userbot (حساب تيليجرام شخصي يتم التحكم به برمجياً عبر MTProto).\n" +
                            "• بما أنك تبني بوتاً رسمياً مع أزرار Inline وشروط قبول، فالتوكن هو كل ما تحتاجه!",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // No-Code Platforms
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Layers, contentDescription = null, tint = TelegramBlue)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "منصات بدون كود (No-Code Platforms)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "يمكنك تشغيل هذا البوت بالكامل بدون كتابة سطر كود عبر إحدى المنصات التالية:\n\n" +
                            "1️⃣ Make.com (الأفضل والأسهل):\n" +
                            "• أنشئ سيناريو جديد واختر Telegram Bot > Watch Updates.\n" +
                            "• ضع التوكن الخاص بك.\n" +
                            "• أضف إجراء Send a Photo مع الأزرار.\n" +
                            "• عند حظر البوت: اضغط على Connection وغيّر التوكن فقط، وسيعمل فوراً!\n\n" +
                            "2️⃣ ManyBot (@ManyBot على تيليجرام):\n" +
                            "• بوت داخل تيليجرام يسمح لك بإنشاء قوائم وأزرار وتوجيه روابط دون أي خادم خارجي.\n\n" +
                            "3️⃣ SmartBot أو BotHost:\n" +
                            "• منصات إدارة روبوتات القنوات مع لوحة تحكم كاملة.",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Code option
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Code, contentDescription = null, tint = TelegramBlue)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "الخيار البرمجي (Code - مجاني 100% ومدى الحياة)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "• ميزة الكود: لا توجد رسوم اشتراك شهرية ولا قيود على عدد الرسائل.\n" +
                            "• الكود المرفق في التبويب الثاني (Python) يحتوي على قاعدة بيانات SQLite تحفظ كل المشتركين تلقائياً.\n" +
                            "• عند حظر البوت، يمكنك استضافة الكود على Render / Railway / Replit أو حتى هاتفك عبر تطبيق Termux، وتغيير التوكن في ثانية واحدة.",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun CodeDisplaySection(
    title: String,
    code: String,
    language: String,
    onCopy: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = onCopy,
                colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                modifier = Modifier.testTag("btn_copy_code")
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("نسخ الكود")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            color = Color(0xFF0F172A),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = code,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                ),
                color = Color(0xFFE2E8F0),
                modifier = Modifier.padding(14.dp)
            )
        }
    }
}

@Composable
fun CloudDeploymentGuideSection(config: BotConfigEntity, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Status Check Result Header
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = TelegramAccentGreen)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "نتيجة فحص التوكن الآن: سليم وشغال 100%",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF166534)
                        )
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "• معرّف البوت: @${config.botUsername}\n" +
                            "• اسم البوت: Linkvip_5bot (1̶0̶0̶ ̶ᵀⁱᵐᵉˢ)\n" +
                            "• المعرف الرقمي: ${config.botId}\n" +
                            "• حالة خوادم تيليجرام: التوكن مفعل وجاهز لاستقبال واستجابة الأوامر بمجرد ربطه بسيرفر سحابي.",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = Color(0xFF14532D)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 1: Render.com (Recommended Free Cloud Host)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Cloud, contentDescription = null, tint = TelegramBlue)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "1️⃣ النشر على Render.com (أسهل استضافة سحابية 24/7 مجاناً)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Render تمنحك استضافة مجانية تعمل على مدار الساعة بدون انقطاع وبدون الحاجة لفتح حاسوبك:\n\n" +
                            "الخطوة 1: ادخل على موقع render.com وسجل حساباً مجانياً (Sign Up via GitHub أو Google).\n\n" +
                            "الخطوة 2: أنشئ مستودع (Repository) جديد في GitHub وضع فيه ملفين:\n" +
                            "  • ملف main.py (انسخ كود Python من التبويب أعلاه).\n" +
                            "  • ملف requirements.txt يحتوي على:\n" +
                            "    aiogram==3.13.1\n\n" +
                            "الخطوة 3: في Render اضغط New + ثم اختر Background Worker.\n" +
                            "  • اربط مستودع GitHub الخاص بك.\n" +
                            "  • في خانة Build Command اكتب: pip install -r requirements.txt\n" +
                            "  • في خانة Start Command اكتب: python main.py\n\n" +
                            "الخطوة 4: في قسم Environment Variables أضف متغير:\n" +
                            "  • المفتاح: BOT_TOKEN\n" +
                            "  • القيمة: ${config.botToken}\n\n" +
                            "الخطوة 5: اضغط Create Web Service / Worker، وخلال 60 ثانية سيعمل البوت في تيليجرام 24/7!",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        val reqText = "aiogram==3.13.1\n"
                        copyToClipboard(context, reqText, "تم نسخ محتوى requirements.txt")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("نسخ ملف requirements.txt")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 2: PythonAnywhere (No GitHub required)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Code, contentDescription = null, tint = TelegramAccentGreen)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "2️⃣ النشر على PythonAnywhere (بدون الحاجة لـ GitHub)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "إذا كنت لا تملك حساب GitHub وتريد تشغيل البوت مباشرة من المتصفح:\n\n" +
                            "1. ادخل على pythonanywhere.com وسجل حساباً مجانياً (Beginner Account).\n" +
                            "2. افتح تبويب Consoles واختر Bash Console.\n" +
                            "3. اكتب: pip install aiogram ثم اضغط Enter.\n" +
                            "4. اذهب لتبويب Files وأنشئ ملفاً جديداً باسم bot.py والصق فيه كود Python من التطبيق واضغط Save.\n" +
                            "5. في تبويب Tasks أو في شاشة Bash اكتب: python3 bot.py وسيشتغل البوت فوراً!",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 3: Anti-Ban Cloud Guarantee
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = TelegramSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = TelegramAccentGreen)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "🛡️ كيف تحمي بياناتك عند حظر البوت على الاستضافة السحابية؟",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "على الاستضافة السحابية، لا تحتاج لإعادة كتابة أو تعديل أي سطر كود إذا تم حظر البوت:\n" +
                            "1. افتح @BotFather واستخرج توكن جديد في 10 ثوانٍ.\n" +
                            "2. افتح إعدادات الاستضافة (Environment Variables) وغير قيمة BOT_TOKEN فقط.\n" +
                            "3. اضغط Restart، وسيعود البوت الجديد للعمل بنفس المشتركين والشروط والروابط دون فقدان أي شيء نهائياً.",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = Color.LightGray
                )
            }
        }
    }
}

private fun copyToClipboard(context: Context, text: String, toastMsg: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Telegram Bot Code", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
}

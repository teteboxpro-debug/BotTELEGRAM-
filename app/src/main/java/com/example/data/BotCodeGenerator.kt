package com.example.data

object BotCodeGenerator {

    fun generatePythonCode(config: BotConfigEntity): String {
        return """
# ==========================================================
# Official Telegram Bot with Anti-Ban & Persistent Database
# Framework: Aiogram 3.x (Asyncio + SQLite)
# ==========================================================
# ملاحظة مهمة: لحماية البيانات عند الحظر، يتم حفظ المشتركين
# والإعدادات في قاعدة بيانات مستقلة (bot_database.db).
# عند حظر البوت، يكفي فقط تغيير قيمة BOT_TOKEN دون أن تفقد أي بيانات!

import asyncio
import logging
import sqlite3
import os
from aiogram import Bot, Dispatcher, types, F
from aiogram.filters import CommandStart
from aiogram.types import InlineKeyboardMarkup, InlineKeyboardButton

# 1. التوكن يمكن تمريره كمتغير بيئة أو كتابته هنا
# عند حظر البوت، فقط غيّر هذا التوكن إلى التوكن الجديد من @BotFather
BOT_TOKEN = os.getenv("BOT_TOKEN", "${config.botToken}")

logging.basicConfig(level=logging.INFO)
bot = Bot(token=BOT_TOKEN)
dp = Dispatcher()

# 2. إنشاء وحفظ قاعدة البيانات المستقلة للمشتركين والبيانات
def init_db():
    conn = sqlite3.connect("bot_database.db")
    cursor = conn.cursor()
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS users (
            user_id INTEGER PRIMARY KEY,
            username TEXT,
            accepted_rules BOOLEAN,
            joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
    ''')
    conn.commit()
    conn.close()

def save_user(user_id: int, username: str, accepted: bool):
    conn = sqlite3.connect("bot_database.db")
    cursor = conn.cursor()
    cursor.execute('''
        INSERT INTO users (user_id, username, accepted_rules)
        VALUES (?, ?, ?)
        ON CONFLICT(user_id) DO UPDATE SET accepted_rules = ?
    ''', (user_id, username, accepted, accepted))
    conn.commit()
    conn.close()

# 3. أمر البداية /start
@dp.message(CommandStart())
async def start_handler(message: types.Message):
    save_user(message.from_user.id, message.from_user.username or "", False)
    
    markup = InlineKeyboardMarkup(inline_keyboard=[
        [InlineKeyboardButton(text="✅ I Accept", callback_data="/accept")],
        [InlineKeyboardButton(text="❌ I Decline", callback_data="/decline")]
    ])
    
    caption_text = (
        "🌟 **Welcome to the Official Channel Bot** 🌟\n\n"
        "Please accept our rules to continue:\n"
        "1. Respect all members.\n"
        "2. No spam or external links.\n\n"
        "👇 **Click Accept to proceed** 👇"
    )
    
    await message.answer_photo(
        photo="${config.startPhotoUrl}",
        caption=caption_text,
        reply_markup=markup,
        parse_mode="Markdown"
    )

# 4. معالجة قبول الشروط /accept
@dp.callback_query(F.data == "/accept")
async def accept_callback(callback: types.CallbackQuery):
    save_user(callback.from_user.id, callback.from_user.username or "", True)
    
    markup = InlineKeyboardMarkup(inline_keyboard=[
        [InlineKeyboardButton(text="𝐟𝐫𝐞𝐞 𝐜𝐡𝐚𝐧𝐧𝐞𝐥 𝗹𝗶𝗻𝗸", url="${config.freeChannelLink}")],
        [InlineKeyboardButton(text="💳 𝗦𝘁𝗼𝗿𝗲 & 𝗣𝗮𝘆𝗺𝗲𝗻𝘁", url="${config.storePaymentLink}")],
        [InlineKeyboardButton(text="🌐 Visit Website", url="${config.websiteLink}")]
    ])
    
    caption_text = (
        "✅ **You have successfully accepted the rules!**\n\n"
        "Choose an option below to continue:"
    )
    
    # حذف الرسالة السابقة أو إرسال صورة جديدة
    await callback.message.delete()
    await callback.message.answer_photo(
        photo="${config.acceptPhotoUrl}",
        caption=caption_text,
        reply_markup=markup,
        parse_mode="Markdown"
    )
    await callback.answer()

# 5. معالجة رفض الشروط /decline
@dp.callback_query(F.data == "/decline")
async def decline_callback(callback: types.CallbackQuery):
    markup = InlineKeyboardMarkup(inline_keyboard=[
        [InlineKeyboardButton(text="🔙 Back to Start", callback_data="/start")]
    ])
    
    caption_text = (
        "❌ **Sorry, you cannot proceed without accepting the rules.**\n\n"
        "Please click below to try again."
    )
    
    await callback.message.delete()
    await callback.message.answer_photo(
        photo="${config.declinePhotoUrl}",
        caption=caption_text,
        reply_markup=markup,
        parse_mode="Markdown"
    )
    await callback.answer()

# زر العودة للبداية
@dp.callback_query(F.data == "/start")
async def back_to_start(callback: types.CallbackQuery):
    await callback.message.delete()
    # استدعاء دالة البداية
    markup = InlineKeyboardMarkup(inline_keyboard=[
        [InlineKeyboardButton(text="✅ I Accept", callback_data="/accept")],
        [InlineKeyboardButton(text="❌ I Decline", callback_data="/decline")]
    ])
    caption_text = (
        "🌟 **Welcome to the Official Channel Bot** 🌟\n\n"
        "Please accept our rules to continue:\n"
        "1. Respect all members.\n"
        "2. No spam or external links.\n\n"
        "👇 **Click Accept to proceed** 👇"
    )
    await callback.message.answer_photo(
        photo="${config.startPhotoUrl}",
        caption=caption_text,
        reply_markup=markup,
        parse_mode="Markdown"
    )
    await callback.answer()

async def main():
    init_db()
    print("🤖 Bot is starting up...")
    await dp.start_polling(bot)

if __name__ == "__main__":
    asyncio.run(main())
""".trimIndent()
    }

    fun generateNodeJsCode(config: BotConfigEntity): String {
        return """
// ==========================================================
// Official Telegram Bot with Node.js & Telegraf
// ==========================================================
// تثبيت: npm install telegraf

const { Telegraf, Markup } = require('telegraf');

// التوكن مفصول في متغير بيئة لحماية البيانات عند الحظر
const BOT_TOKEN = process.env.BOT_TOKEN || '${config.botToken}';
const bot = new Telegraf(BOT_TOKEN);

// شاشة البداية /start
bot.start((ctx) => {
    const text = '🌟 *Welcome to the Official Channel Bot* 🌟\n\nPlease accept our rules to continue:\n1. Respect all members.\n2. No spam or external links.\n\n👇 *Click Accept to proceed* 👇';
    
    const keyboard = Markup.inlineKeyboard([
        [Markup.button.callback('✅ I Accept', 'action_accept')],
        [Markup.button.callback('❌ I Decline', 'action_decline')]
    ]);
    
    return ctx.replyWithPhoto('${config.startPhotoUrl}', {
        caption: text,
        parse_mode: 'Markdown',
        ...keyboard
    });
});

// عند الضغط على Accept
bot.action('action_accept', async (ctx) => {
    await ctx.deleteMessage().catch(() => {});
    const text = '✅ *You have successfully accepted the rules!*\n\nChoose an option below to continue:';
    
    const keyboard = Markup.inlineKeyboard([
        [Markup.button.url('𝐟𝐫𝐞𝐞 𝐜𝐡𝐚𝐧𝐧𝐞𝐥 𝗹𝗶𝗻𝗸', '${config.freeChannelLink}')],
        [Markup.button.url('💳 𝗦𝘁𝗼𝗿𝗲 & 𝗣𝗮𝘆𝗺𝗲𝗻𝘁', '${config.storePaymentLink}')],
        [Markup.button.url('🌐 Visit Website', '${config.websiteLink}')]
    ]);
    
    return ctx.replyWithPhoto('${config.acceptPhotoUrl}', {
        caption: text,
        parse_mode: 'Markdown',
        ...keyboard
    });
});

// عند الضغط على Decline
bot.action('action_decline', async (ctx) => {
    await ctx.deleteMessage().catch(() => {});
    const text = '❌ *Sorry, you cannot proceed without accepting the rules.*\n\nPlease click below to try again.';
    
    const keyboard = Markup.inlineKeyboard([
        [Markup.button.callback('🔙 Back to Start', 'action_start')]
    ]);
    
    return ctx.replyWithPhoto('${config.declinePhotoUrl}', {
        caption: text,
        parse_mode: 'Markdown',
        ...keyboard
    });
});

// العودة للبداية
bot.action('action_start', async (ctx) => {
    await ctx.deleteMessage().catch(() => {});
    const text = '🌟 *Welcome to the Official Channel Bot* 🌟\n\nPlease accept our rules to continue:\n1. Respect all members.\n2. No spam or external links.\n\n👇 *Click Accept to proceed* 👇';
    
    const keyboard = Markup.inlineKeyboard([
        [Markup.button.callback('✅ I Accept', 'action_accept')],
        [Markup.button.callback('❌ I Decline', 'action_decline')]
    ]);
    
    return ctx.replyWithPhoto('${config.startPhotoUrl}', {
        caption: text,
        parse_mode: 'Markdown',
        ...keyboard
    });
});

bot.launch();
console.log('Bot running smoothly!');
""".trimIndent()
    }
}

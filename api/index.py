import json
from http.server import BaseHTTPRequestHandler
import urllib.request

BOT_TOKEN = "8544041324:AAFynDxXGE3B0fy5KuaE8dNpYar-DnAnZMY"
PHOTO_URL = "https://graph.org/file/982debac24a1b4d9868fe-508f86f2a0e58c6dbf.jpg"

def telegram_api(method, data):
    url = f"https://api.telegram.org/bot{BOT_TOKEN}/{method}"
    req = urllib.request.Request(
        url,
        data=json.dumps(data).encode("utf-8"),
        headers={"Content-Type": "application/json"}
    )
    try:
        with urllib.request.urlopen(req) as resp:
            return resp.read()
    except Exception as e:
        print("Telegram API Error:", e)

class handler(BaseHTTPRequestHandler):
    def do_POST(self):
        content_len = int(self.headers.get("Content-Length", 0))
        post_data = self.rfile.read(content_len)
        try:
            update = json.loads(post_data.decode("utf-8"))
        except Exception:
            update = {}

        # إذا كانت رسالة /start
        if "message" in update and "text" in update["message"]:
            chat_id = update["message"]["chat"]["id"]
            text = update["message"]["text"]

            if text.startswith("/start"):
                caption = (
                    "🌟 *Welcome to the Official Channel Bot* 🌟\n\n"
                    "Please accept our rules to continue:\n"
                    "1. Respect all members.\n"
                    "2. No spam or external links.\n\n"
                    "👇 *Click Accept to proceed* 👇"
                )
                payload = {
                    "chat_id": chat_id,
                    "photo": PHOTO_URL,
                    "caption": caption,
                    "parse_mode": "Markdown",
                    "reply_markup": {
                        "inline_keyboard": [
                            [{"text": "✅ I Accept", "callback_data": "btn_accept"}],
                            [{"text": "❌ I Decline", "callback_data": "btn_decline"}]
                        ]
                    }
                }
                telegram_api("sendPhoto", payload)

        # إذا ضغط المستخدم على زر (Accept / Decline)
        elif "callback_query" in update:
            cb = update["callback_query"]
            chat_id = cb["message"]["chat"]["id"]
            msg_id = cb["message"]["message_id"]
            data = cb.get("data", "")

            if data == "btn_accept":
                telegram_api("deleteMessage", {"chat_id": chat_id, "message_id": msg_id})
                menu_payload = {
                    "chat_id": chat_id,
                    "text": "✅ *You have successfully accepted the rules!*\n\nChoose an option below to continue:",
                    "parse_mode": "Markdown",
                    "reply_markup": {
                        "inline_keyboard": [
                            [{"text": "FREE CHANNEL", "url": "https://rentry.co/Teteboxvip"}],
                            [{"text": "VIP STORE", "url": "https://t.me/SoSoeteboxvipbot?start=buy_O_LAOyTP05"}],
                            [{"text": "VIST WEP", "url": "https://eteboxvip.carrd.co/"}]
                        ]
                    }
                }
                telegram_api("sendMessage", menu_payload)

            elif data == "btn_decline":
                telegram_api("answerCallbackQuery", {
                    "callback_query_id": cb["id"],
                    "text": "تم رفض الشروط. لا يمكنك المتابعة بدون الموافقة.",
                    "show_alert": True
                })

        self.send_response(200)
        self.send_header("Content-Type", "text/plain")
        self.end_headers()
        self.wfile.write(b"OK")

    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-Type", "text/plain")
        self.end_headers()
        self.wfile.write(b"Bot Server is Live and Ready!")

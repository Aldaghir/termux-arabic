#!/data/data/com.termux/files/usr/bin/bash

# ===================================================================
# Arabic Support Test Script for Termux
# اسكربت اختبار دعم اللغة العربية في Termux
# ===================================================================

echo "=========================================="
echo "اختبار دعم اللغة العربية في Termux"
echo "Termux Arabic Support Test"
echo "=========================================="
echo ""

# Test 1: Basic Arabic characters
echo "Test 1: Basic Arabic Characters"
echo "الاختبار 1: الحروف العربية الأساسية"
echo "ابجد هوز حطي كلمن سعفص قرشت ثخذ ضظغ"
echo ""

# Test 2: Common Arabic words
echo "Test 2: Common Arabic Words"
echo "الاختبار 2: كلمات عربية شائعة"
echo "مرحبا - أهلا - السلام عليكم - شكرا"
echo "العربية - اللغة - الكتابة - القراءة"
echo ""

# Test 3: Arabic with numbers
echo "Test 3: Arabic with Numbers"
echo "الاختبار 3: العربية مع الأرقام"
echo "عندي 123 كتاب و 456 قلم"
echo "السنة 2025 ميلادي"
echo ""

# Test 4: Mixed Arabic and English
echo "Test 4: Mixed Arabic and English (BiDi)"
echo "الاختبار 4: خليط عربي وإنجليزي"
echo "Welcome to مرحبا في Termux Terminal"
echo "Linux commands with العربية text"
echo ""

# Test 5: Arabic sentences
echo "Test 5: Arabic Sentences"
echo "الاختبار 5: جمل عربية"
echo "هذا اختبار لدعم اللغة العربية في تطبيق Termux"
echo "نأمل أن ترى الحروف متصلة بشكل صحيح"
echo "الاتجاه يجب أن يكون من اليمين إلى اليسار"
echo ""

# Test 6: Arabic with diacritics
echo "Test 6: Arabic with Diacritics (Tashkeel)"
echo "الاختبار 6: العربية مع التشكيل"
echo "مَرْحَباً بِكَ فِي تِيرْمُكْس"
echo "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ"
echo ""

# Test 7: Long Arabic text
echo "Test 7: Longer Arabic Text"
echo "الاختبار 7: نص عربي طويل"
echo "التطبيق يدعم الآن عرض اللغة العربية بشكل صحيح مع ربط الحروف والاتجاه من اليمين لليسار"
echo ""

# Test 8: Arabic with special characters
echo "Test 8: Arabic with Special Characters"
echo "الاختبار 8: العربية مع رموز خاصة"
echo "السعر: 100$ - التاريخ: 23/11/2025"
echo "البريد: test@example.com"
echo ""

# Test 9: Command output test
echo "Test 9: Command Output in Arabic"
echo "الاختبار 9: مخرجات الأوامر بالعربية"
echo "إنشاء ملف اختبار..."
echo "مرحبا بك في Termux" > /data/data/com.termux/files/home/test_arabic.txt
if [ -f /data/data/com.termux/files/home/test_arabic.txt ]; then
    echo "✓ تم إنشاء الملف بنجاح"
    echo "محتوى الملف:"
    cat /data/data/com.termux/files/home/test_arabic.txt
    rm /data/data/com.termux/files/home/test_arabic.txt
    echo "✓ تم حذف الملف"
else
    echo "✗ فشل إنشاء الملف"
fi
echo ""

# Test 10: Visual verification guide
echo "Test 10: Visual Verification"
echo "الاختبار 10: التحقق البصري"
echo "=========================================="
echo "ما الذي يجب أن تراه:"
echo "What you should see:"
echo "=========================================="
echo "✓ الحروف العربية متصلة (not separated)"
echo "✓ النص من اليمين لليسار (flows RTL)"
echo "✓ التشكيل يظهر بشكل صحيح (diacritics visible)"
echo "✓ الأرقام في مكانها الصحيح (numbers positioned correctly)"
echo ""
echo "ما الذي لا يجب أن تراه:"
echo "What you should NOT see:"
echo "=========================================="
echo "✗ حروف منفصلة مثل: ا ل ع ر ب ي ة"
echo "✗ نص من اليسار لليمين"
echo "✗ حروف مقلوبة أو مشوهة"
echo ""

# Final report
echo "=========================================="
echo "انتهى الاختبار!"
echo "Test Complete!"
echo "=========================================="
echo ""
echo "إذا رأيت كل النصوص العربية بشكل صحيح، فإن دعم العربية يعمل!"
echo "If you see all Arabic text correctly, Arabic support is working!"
echo ""
echo "للإبلاغ عن المشاكل | To report issues:"
echo "- GitHub: https://github.com/termux/termux-app/issues"
echo "- Include screenshot and device info"
echo ""
echo "شكراً لاستخدامك Termux"
echo "Thank you for using Termux!"
echo "=========================================="

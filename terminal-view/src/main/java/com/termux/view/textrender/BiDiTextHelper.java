package com.termux.view.textrender;

import android.icu.text.Bidi;
import android.os.Build;

/**
 * Helper class for bidirectional text detection and handling.
 * Provides utilities to detect RTL (Right-To-Left) text and determine text direction.
 *
 * @author Aldaghir
 * @version 1.0
 */
public class BiDiTextHelper {

    /**
     * Direction constants matching Android's Layout.Direction
     */
    public static final int DIRECTION_LTR = 0;
    public static final int DIRECTION_RTL = 1;

    /**
     * Checks if the given text contains any RTL (Right-To-Left) characters.
     * This includes Arabic, Hebrew, Persian, Urdu, and other RTL scripts.
     *
     * @param text The character array to check
     * @param start The starting index in the array
     * @param length The number of characters to check
     * @return true if the text contains any RTL characters, false otherwise
     */
    public static boolean containsRtlCharacters(char[] text, int start, int length) {
        if (text == null || length == 0) {
            return false;
        }

        int end = Math.min(start + length, text.length);
        for (int i = start; i < end; i++) {
            char c = text[i];

            // Check for common RTL character ranges
            // Arabic: U+0600 to U+06FF
            // Arabic Supplement: U+0750 to U+077F
            // Arabic Extended-A: U+08A0 to U+08FF
            // Hebrew: U+0590 to U+05FF
            // Arabic Presentation Forms-A: U+FB50 to U+FDFF
            // Arabic Presentation Forms-B: U+FE70 to U+FEFF
            if ((c >= 0x0590 && c <= 0x05FF) ||  // Hebrew
                (c >= 0x0600 && c <= 0x06FF) ||  // Arabic
                (c >= 0x0750 && c <= 0x077F) ||  // Arabic Supplement
                (c >= 0x08A0 && c <= 0x08FF) ||  // Arabic Extended-A
                (c >= 0xFB50 && c <= 0xFDFF) ||  // Arabic Presentation Forms-A
                (c >= 0xFE70 && c <= 0xFEFF)) {  // Arabic Presentation Forms-B
                return true;
            }

            // Also check using Character.getDirectionality for accuracy
            byte direction = Character.getDirectionality(c);
            if (direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC ||
                direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING ||
                direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines the primary direction of the text using Unicode BiDi algorithm.
     *
     * @param text The character array to analyze
     * @param start The starting index in the array
     * @param length The number of characters to analyze
     * @return DIRECTION_RTL if the text is primarily RTL, DIRECTION_LTR otherwise
     */
    public static int getTextDirection(char[] text, int start, int length) {
        if (text == null || length == 0) {
            return DIRECTION_LTR;
        }

        // For Android API 24+ (Android 7.0+), use ICU's Bidi class for accurate detection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                String str = new String(text, start, length);
                Bidi bidi = new Bidi(str, Bidi.LEVEL_DEFAULT_LTR);
                return bidi.isRightToLeft() ? DIRECTION_RTL : DIRECTION_LTR;
            } catch (Exception e) {
                // Fall back to manual detection if ICU fails
            }
        }

        // Fallback: Simple heuristic - check if first strong directional character is RTL
        int end = Math.min(start + length, text.length);
        for (int i = start; i < end; i++) {
            byte direction = Character.getDirectionality(text[i]);

            // First strong directional character determines the direction
            if (direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC) {
                return DIRECTION_RTL;
            } else if (direction == Character.DIRECTIONALITY_LEFT_TO_RIGHT) {
                return DIRECTION_LTR;
            }
        }

        // Default to LTR if no strong directional character found
        return DIRECTION_LTR;
    }

    /**
     * Checks if the character is an Arabic letter.
     *
     * @param c The character to check
     * @return true if the character is Arabic, false otherwise
     */
    public static boolean isArabicCharacter(char c) {
        return (c >= 0x0600 && c <= 0x06FF) ||  // Arabic
               (c >= 0x0750 && c <= 0x077F) ||  // Arabic Supplement
               (c >= 0x08A0 && c <= 0x08FF) ||  // Arabic Extended-A
               (c >= 0xFB50 && c <= 0xFDFF) ||  // Arabic Presentation Forms-A
               (c >= 0xFE70 && c <= 0xFEFF);    // Arabic Presentation Forms-B
    }

    /**
     * Checks if the text needs complex text shaping.
     * This includes Arabic, which requires character joining.
     *
     * @param text The character array to check
     * @param start The starting index in the array
     * @param length The number of characters to check
     * @return true if the text needs shaping, false otherwise
     */
    public static boolean needsTextShaping(char[] text, int start, int length) {
        if (text == null || length == 0) {
            return false;
        }

        int end = Math.min(start + length, text.length);
        for (int i = start; i < end; i++) {
            if (isArabicCharacter(text[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the character is a combining mark or diacritic.
     *
     * @param c The character to check
     * @return true if the character is a combining mark, false otherwise
     */
    public static boolean isCombiningMark(char c) {
        // Arabic diacritics range: U+064B to U+065F
        // Arabic extended diacritics: U+0670
        // More combining marks: U+06D6 to U+06DC
        return (c >= 0x064B && c <= 0x065F) ||
               c == 0x0670 ||
               (c >= 0x06D6 && c <= 0x06DC) ||
               (c >= 0x06DF && c <= 0x06E4) ||
               (c >= 0x06E7 && c <= 0x06E8) ||
               (c >= 0x06EA && c <= 0x06ED);
    }
}

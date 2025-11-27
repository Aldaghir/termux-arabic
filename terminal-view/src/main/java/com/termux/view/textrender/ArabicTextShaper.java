package com.termux.view.textrender;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.text.Bidi;

/**
 * Handles complex text shaping for Arabic and other RTL languages.
 * Uses java.text.Bidi for proper BiDi algorithm and Android's drawTextRun for text shaping.
 *
 * This class provides proper rendering of:
 * - Arabic character joining (contextual forms) via drawTextRun
 * - Right-to-left text direction via BiDi algorithm
 * - Combining marks and diacritics
 * - Mixed LTR/RTL text (BiDi) - proper visual ordering
 *
 * @author Aldaghir
 * @version 2.0
 */
public class ArabicTextShaper {

    /**
     * Draws text with proper shaping for Arabic and BiDi support.
     * This method handles:
     * - Text shaping via drawTextRun (HarfBuzz)
     * - BiDi reordering using java.text.Bidi algorithm
     * - Proper visual ordering for mixed LTR/RTL text
     * - Drawing each BiDi run separately with correct positioning
     *
     * @param canvas The canvas to draw on
     * @param text The character array containing the text (full line)
     * @param startCharIndex The start index in the character array
     * @param runWidthChars The number of characters to draw
     * @param contextEnd The end of the valid context in the text array
     * @param x The x-coordinate to start drawing
     * @param y The y-coordinate (baseline) to draw at
     * @param paint The Paint object with font and color settings
     * @param isRtl Whether the text direction is right-to-left
     * @return The actual width of the drawn text
     */
    public static float drawShapedText(Canvas canvas, char[] text, int startCharIndex,
                                       int runWidthChars, int contextEnd, float x, float y,
                                       Paint paint, boolean isRtl) {
        if (text == null || runWidthChars <= 0 || canvas == null || paint == null) {
            return 0;
        }

        // Extract the run text
        String runText = new String(text, startCharIndex, runWidthChars);

        // Check if text contains RTL characters (Arabic, Hebrew, etc.)
        boolean hasRtl = containsRtlCharacters(runText);

        if (hasRtl) {
            try {
                // Apply BiDi algorithm for proper reordering
                // IMPORTANT: Always use DIRECTION_DEFAULT_LEFT_TO_RIGHT for mixed content
                // This ensures proper handling of Arabic with numbers/English
                Bidi bidi = new Bidi(runText, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);

                // Check if we need visual reordering
                if (!bidi.isLeftToRight() && bidi.getRunCount() > 0) {
                    // CRITICAL FIX: Calculate total width first to position runs correctly
                    float totalWidth = paint.measureText(runText);

                    // Determine base direction (RTL if primarily Arabic)
                    boolean baseRtl = !bidi.baseIsLeftToRight();

                    // Draw each run in VISUAL order (not logical order!)
                    // For RTL text, we need to draw from RIGHT to LEFT
                    float currentX = baseRtl ? (x + totalWidth) : x;
                    int runCount = bidi.getRunCount();

                    for (int i = 0; i < runCount; i++) {
                        int runStart = bidi.getRunStart(i);
                        int runLimit = bidi.getRunLimit(i);
                        int runLength = runLimit - runStart;
                        boolean runIsRtl = (bidi.getRunLevel(i) & 1) != 0;

                        // Get the substring for this run
                        char[] runChars = runText.substring(runStart, runLimit).toCharArray();
                        float runWidth = paint.measureText(runChars, 0, runLength);

                        // For RTL base direction, move X BEFORE drawing (draw from right to left)
                        if (baseRtl) {
                            currentX -= runWidth;
                        }

                        // Draw this run with proper direction
                        canvas.drawTextRun(runChars, 0, runLength,
                                         0, runLength,
                                         currentX, y, runIsRtl, paint);

                        // For LTR base direction, move X AFTER drawing (draw from left to right)
                        if (!baseRtl) {
                            currentX += runWidth;
                        }
                    }

                    // Return total width
                    return totalWidth;
                }
            } catch (Exception e) {
                // Fallback to simple drawing on error
                canvas.drawTextRun(text, startCharIndex, runWidthChars,
                                  0, contextEnd,
                                  x, y, isRtl, paint);
            }
        }

        // Fallback to standard drawing if no BiDi reordering needed
        canvas.drawTextRun(text, startCharIndex, runWidthChars,
                          0, contextEnd,
                          x, y, isRtl, paint);

        // Return width
        return paint.measureText(text, startCharIndex, runWidthChars);
    }

    /**
     * Checks if a string contains any RTL (Right-To-Left) characters.
     *
     * @param text The text to check
     * @return true if text contains RTL characters
     */
    private static boolean containsRtlCharacters(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // Use BiDiTextHelper for accurate RTL detection
            if (BiDiTextHelper.isArabicCharacter(c)) {
                return true;
            }

            byte direction = Character.getDirectionality(c);
            if (direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a string contains both LTR and RTL characters (mixed text).
     * Note: Digits and neutral characters are ignored in this check.
     *
     * @param text The text to check
     * @return true if text contains both strong LTR and strong RTL characters
     */
    private static boolean containsMixedDirections(String text) {
        boolean hasLtr = false;
        boolean hasRtl = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            byte direction = Character.getDirectionality(c);

            // Check for strong LTR (Latin letters, etc.)
            if (direction == Character.DIRECTIONALITY_LEFT_TO_RIGHT) {
                hasLtr = true;
            }
            // Check for strong RTL (Arabic, Hebrew)
            else if (direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                     direction == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC) {
                hasRtl = true;
            }
            // Ignore digits (EUROPEAN_NUMBER, ARABIC_NUMBER) and neutral chars
            // They will follow the direction of surrounding strong characters

            // Early exit if we found both strong directions
            if (hasLtr && hasRtl) {
                return true;
            }
        }

        return false;
    }

    /**
     * Legacy method for backward compatibility.
     * Calls the new method with contextEnd = startCharIndex + runWidthChars
     */
    public static float drawShapedText(Canvas canvas, char[] text, int startCharIndex,
                                       int runWidthChars, float x, float y,
                                       Paint paint, boolean isRtl) {
        return drawShapedText(canvas, text, startCharIndex, runWidthChars,
                            startCharIndex + runWidthChars, x, y, paint, isRtl);
    }

    /**
     * Measures the width of shaped text.
     * This is useful for calculating proper positioning.
     *
     * @param text The character array containing the text
     * @param startCharIndex The start index in the character array
     * @param runWidthChars The number of characters to measure
     * @param paint The Paint object with font settings
     * @param isRtl Whether the text direction is right-to-left
     * @return The width of the shaped text
     */
    public static float measureShapedText(char[] text, int startCharIndex,
                                         int runWidthChars, Paint paint, boolean isRtl) {
        if (text == null || runWidthChars <= 0 || paint == null) {
            return 0;
        }

        // Simply measure using Paint - BiDi doesn't affect width
        return paint.measureText(text, startCharIndex, runWidthChars);
    }

    /**
     * Checks if the given text requires complex shaping.
     * Simple ASCII text can use faster rendering path.
     *
     * @param text The character array to check
     * @param start The start index
     * @param length The number of characters to check
     * @return true if shaping is required, false otherwise
     */
    public static boolean requiresShaping(char[] text, int start, int length) {
        if (text == null || length == 0) {
            return false;
        }

        // OPTIMIZATION: Skip shaping for ASCII-only text
        // This ensures that commands like "pkg install" are not affected
        // by Arabic text processing
        boolean hasNonAscii = false;
        int end = Math.min(start + length, text.length);
        for (int i = start; i < end; i++) {
            if (text[i] > 127) {
                hasNonAscii = true;
                break;
            }
        }

        // If text is pure ASCII (0-127), don't apply shaping
        if (!hasNonAscii) {
            return false;
        }

        // For non-ASCII text, check if it needs shaping (Arabic/RTL)
        return BiDiTextHelper.needsTextShaping(text, start, length) ||
               BiDiTextHelper.containsRtlCharacters(text, start, length);
    }
}

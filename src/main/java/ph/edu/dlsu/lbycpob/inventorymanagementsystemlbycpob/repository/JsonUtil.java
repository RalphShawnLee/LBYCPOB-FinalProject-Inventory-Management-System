package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class JsonUtil {
    private JsonUtil() {
    }

    static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    static String unescape(String value) {
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    static String extractString(String json, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"").matcher(json);
        return m.find() ? unescape(m.group(1)) : null;
    }

    static Integer extractInt(String json, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*(-?\\d+)\\b").matcher(json);
        return m.find() ? Integer.parseInt(m.group(1)) : null;
    }

    static Double extractDouble(String json, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)").matcher(json);
        return m.find() ? Double.parseDouble(m.group(1)) : null;
    }

    /** Returns the raw {...} substring of each object inside the array field named key. */
    static List<String> extractArray(String json, String key) {
        int keyIndex = json.indexOf("\"" + key + "\"");
        if (keyIndex < 0) {
            return new ArrayList<>();
        }
        int arrayStart = json.indexOf('[', keyIndex);
        int arrayEnd = matchingBracket(json, arrayStart, '[', ']');
        return splitObjects(json.substring(arrayStart + 1, arrayEnd));
    }

    /** Splits raw text containing consecutive {...} objects into a list of object substrings. */
    static List<String> splitObjects(String text) {
        List<String> result = new ArrayList<>();
        int i = 0;
        while (i < text.length()) {
            if (text.charAt(i) == '{') {
                int end = matchingBracket(text, i, '{', '}');
                result.add(text.substring(i, end + 1));
                i = end + 1;
            } else {
                i++;
            }
        }
        return result;
    }

    private static int matchingBracket(String text, int openIndex, char open, char close) {
        int depth = 0;
        for (int i = openIndex; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == open) {
                depth++;
            } else if (c == close) {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        throw new IllegalStateException("Unbalanced JSON in: " + text);
    }
}

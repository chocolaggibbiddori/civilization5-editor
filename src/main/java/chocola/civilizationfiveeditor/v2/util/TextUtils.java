package chocola.civilizationfiveeditor.v2.util;

import java.util.regex.Pattern;

public class TextUtils {

    private static final Pattern COLOR_TAG = Pattern.compile("\\[[^]]*]");

    public static String stripColorTags(String text) {
        return COLOR_TAG.matcher(text).replaceAll("");
    }
}

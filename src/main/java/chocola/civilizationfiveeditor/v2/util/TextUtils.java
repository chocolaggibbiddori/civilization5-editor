package chocola.civilizationfiveeditor.v2.util;

import java.util.regex.Pattern;

public class TextUtils {

    private static final Pattern INNER_TAG = Pattern.compile("\\[[^]]*]");

    public static String stripInnerTags(String text) {
        return INNER_TAG.matcher(text).replaceAll("");
    }
}

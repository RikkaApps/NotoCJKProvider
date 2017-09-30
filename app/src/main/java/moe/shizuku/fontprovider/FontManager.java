package moe.shizuku.fontprovider;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.RawRes;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import moe.shizuku.fontprovider.utils.ContextUtils;

/**
 * Created by rikka on 2017/9/29.
 */

public class FontManager {

    //private static final Map<String, String> sNameToFilename;

    private static final @RawRes int[] FONTS_RES = {
            R.raw.noto_sans_cjk,
            R.raw.noto_serif,
            R.raw.noto_serif_cjk,
    };

    private static final Map<String, FontInfo> FONTS = new HashMap<>();

    public static void init(Context context) {
        if (FONTS.size() > 0) {
            return;
        }

        for (int res : FONTS_RES) {
            FontInfo font = new Gson().fromJson(new InputStreamReader(context.getResources().openRawResource(res)), FontInfo.class);
            FONTS.put(font.getName(), font);
        }
    }

    public static FontInfo getFont(String name) {
        return FONTS.get(name);
    }

    private static final Map<String, String> sFonts2;

    static {
        sFonts2 = new HashMap<>();

        sFonts2.put("NotoSansCJK-Thin.ttc",          "https://github.com/googlei18n/noto-cjk/raw/master/NotoSansCJK-Thin.ttc");
        sFonts2.put("NotoSansCJK-Light.ttc",         "https://github.com/googlei18n/noto-cjk/raw/master/NotoSansCJK-Light.ttc");
        sFonts2.put("NotoSansCJK-Regular.ttc",       "https://github.com/googlei18n/noto-cjk/raw/master/NotoSansCJK-Regular.ttc");
        sFonts2.put("NotoSansCJK-Medium.ttc",        "https://github.com/googlei18n/noto-cjk/raw/master/NotoSansCJK-Medium.ttc");
        sFonts2.put("NotoSansCJK-Bold.ttc",          "https://github.com/googlei18n/noto-cjk/raw/master/NotoSansCJK-Bold.ttc");
        sFonts2.put("NotoSansCJK-Black.ttc",         "https://github.com/googlei18n/noto-cjk/raw/master/NotoSansCJK-Black.ttc");

        sFonts2.put("NotoSerif-Regular.ttf",         "https://github.com/googlei18n/noto-fonts/raw/master/hinted/NotoSerif-Regular.ttf");
        sFonts2.put("NotoSerif-Italic.ttf",          "https://github.com/googlei18n/noto-fonts/raw/master/hinted/NotoSerif-Italic.ttf");
        sFonts2.put("NotoSerif-Bold.ttf",            "https://github.com/googlei18n/noto-fonts/raw/master/hinted/NotoSerif-Bold.ttf");
        sFonts2.put("NotoSerif-BoldItalic.ttf",      "https://github.com/googlei18n/noto-fonts/raw/master/hinted/NotoSerif-BoldItalic.ttf");

        sFonts2.put("NotoSerifCJK-ExtraLight.ttc",   "https://github.com/googlei18n/noto-cjk/raw/master/NotoSerifCJK-ExtraLight.ttc");
        sFonts2.put("NotoSerifCJK-Light.ttc",        "https://github.com/googlei18n/noto-cjk/raw/master/NotoSerifCJK-Light.ttc");
        sFonts2.put("NotoSerifCJK-Regular.ttc",      "https://github.com/googlei18n/noto-cjk/raw/master/NotoSerifCJK-Regular.ttc");
        sFonts2.put("NotoSerifCJK-Medium.ttc",       "https://github.com/googlei18n/noto-cjk/raw/master/NotoSerifCJK-Medium.ttc");
        sFonts2.put("NotoSerifCJK-Bold.ttc",         "https://github.com/googlei18n/noto-cjk/raw/master/NotoSerifCJK-Bold.ttc");
        sFonts2.put("NotoSerifCJK-Black.ttc",        "https://github.com/googlei18n/noto-cjk/raw/master/NotoSerifCJK-Black.ttc");
    }

    public static String getUrl(String filename) {
        return sFonts2.get(filename);
    }

    public static Set<String> getFilenameSet() {
        return sFonts2.keySet();
    }

    public static List<String> getMissingFiles(Context context) {
        List<String> filenames = new ArrayList<>();

        for (String filename : getFilenameSet()) {
            if (!ContextUtils.getFile(context, filename).exists()) {
                filenames.add(filename);
            }
        }
        return filenames;
    }

    public static List<Long> downloadAll(Context context) {
        List<Long> ids = new ArrayList<>();
        for (String file : getFilenameSet()) {
            if (!ContextUtils.getFile(context, file).exists()) {
                ids.add(download(context, getUrl(file), file));
            }
        }
        return ids;
    }

    public static long download(Context context, String uri, String filename) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri))
                .setDestinationInExternalFilesDir(context, null, filename)
                .setVisibleInDownloadsUi(false)
                .setAllowedOverMetered(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        DownloadManager downloadManager = context.getSystemService(DownloadManager.class);
        return downloadManager.enqueue(request);
    }
}
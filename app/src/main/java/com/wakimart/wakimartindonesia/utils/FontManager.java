package com.wakimart.wakimartindonesia.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;

public class FontManager {

    //    public static final String faBrands400 = "fonts/fa-brands-400.otf";
//    public static final String faRegular400 = "fonts/fa-regular-400.otf";
//    public static final String faSolid900 = "fonts/fa-solid-900.otf";
    public static final String montserratBold = "fonts/montserrat_bold.ttf";
    public static final String montserratRegular = "fonts/montserrat_regular.ttf";
    public static final String fontAwesomeRegular = "fonts/fa_regular_400.otf";
    public static final String fontAwesomeBrands = "fonts/fa_brands_400.otf";
    public static final String fontAwesomeSolid = "fonts/fa_solid_400.otf";

    public static Typeface getTypeFace(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    public static int getPxFromDp(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}

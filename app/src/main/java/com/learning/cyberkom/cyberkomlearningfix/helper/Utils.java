package com.learning.cyberkom.cyberkomlearningfix.helper;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {

    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }
}

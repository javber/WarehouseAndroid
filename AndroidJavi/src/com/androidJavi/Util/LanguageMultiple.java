/**
 * 
 */
package com.androidJavi.Util;

import java.util.Locale;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.res.Configuration;

/**
 * @author J.Bermudez
 *
 */
public class LanguageMultiple {

	public static void changeLanguage(Activity activity, String languageToLoad, boolean rebootActivity) {
		String previousLanguague = activity.getResources().getConfiguration().locale.toString();
		if (previousLanguague.equals(languageToLoad)) {
			return;
		}
		Locale locale = new Locale(languageToLoad);
		Configuration config = new Configuration();
		config.locale = locale;
		activity.getBaseContext().getResources().updateConfiguration(config,
				activity.getBaseContext().getResources().getDisplayMetrics());
		if (rebootActivity) {
			activity.finish();
			activity.startActivity(activity.getIntent());
		}
	}

	public static String getTraduction(ContextWrapper context, int id) {
		return (String) context.getResources().getText(id);
	}
}

package com.barcicki.trio.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TrioSettings {
	
	public static boolean displaysWhatIsWrong(Context context) {
		return TrioSettings.readBooleanPreference(context, "display_errors", true);
	}
	
	public static boolean setDisplaysWhatIsWrong(Context context, boolean value) {
		return TrioSettings.writeBooleanPreference(context, "display_errors", value);
	}
	
	public static boolean writeBooleanPreference(Context context, String preferenceName, boolean value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor ed = prefs.edit();
		ed.putBoolean(preferenceName, value);	
		return ed.commit();
	}
	
	public static boolean readBooleanPreference(Context context, String preferenceName, boolean defaultValue) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(preferenceName, defaultValue);
	}
	
}

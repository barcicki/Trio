package com.barcicki.trio.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TrioSettings {
	
	public static final String MUSIC = "play_music";
	public static final String SAVED_GAME = "saved_game"; 
	public static final String DISPLAY_ERRORS = "display_errors";
	public static final String SEEN_CLASSIC = "seen_classic";
	public static final String SEEN_CHALLENGE = "seen_practice";
	
	private static boolean isInitialized = false;
	private static Context settingsContext = null;
	
	public static void initialize(Context context) {
		if (!isInitialized) {
			settingsContext = context;
			isInitialized = true;
		}
	}
	
	private static boolean writeBooleanPreference(String preferenceName, boolean value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(settingsContext);
		SharedPreferences.Editor ed = prefs.edit();
		ed.putBoolean(preferenceName, value);	
		return ed.commit();
	}
	
	private static boolean readBooleanPreference(String preferenceName, boolean defaultValue) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(settingsContext);
		return prefs.getBoolean(preferenceName, defaultValue);
	}
	
	public static boolean displaysWhatIsWrong() {
		return readBooleanPreference(DISPLAY_ERRORS, true);
	}
	
	public static boolean setDisplaysWhatIsWrong(boolean value) {
		return writeBooleanPreference(DISPLAY_ERRORS, value);
	}
	
	public static boolean isSavedGamePresent() {
		return readBooleanPreference(SAVED_GAME, false);
	}
	
	public static boolean setSavedGamePresence(boolean value) {
		return writeBooleanPreference(SAVED_GAME, value);
	}
	
	public static boolean isMusicEnabled() {
		return readBooleanPreference(MUSIC, true);
	}
	
	public static boolean setMusicEnabled(boolean value) {
		return writeBooleanPreference(MUSIC, value);
	}
	
	public static boolean hasSeenClassicHelp() {
		return readBooleanPreference(SEEN_CLASSIC, false);
	}
	
	public static boolean hasSeenChallengeHelp() {
		return readBooleanPreference(SEEN_CHALLENGE, false);
	}
	
	public static boolean setSeenClassicHelp(boolean value) {
		return writeBooleanPreference(SEEN_CLASSIC, value);
	}
	
	public static boolean setSeenChallengeHelp(boolean value) {
		return writeBooleanPreference(SEEN_CHALLENGE, value);
	}
}

package com.informaticsproject.ureport;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManagement {

	SharedPreferences pref;

	Editor editor;
	Context _context;

	int PRIVATE_MODE = 0;


	private static final String PREF_NAME = "ureportprefrences";


	private static final String IS_LOGIN = "IsLoggedIn";

	public static final String KEY_USER_ID = "userid";
	public static final String KEY_UNAME = "username";

	public static final String KEY_EMAIL = "email";
	public static final String KEY_UPVOTES = "upvotes";

	public static final String PASS = "password";

	
	public SessionManagement(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();

	}

	
	public void createLoginSession(String userid, String username,
			String password, String email, String upvotes, String type) { // Storing login
																// value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_UNAME, username);
		editor.putString(KEY_UPVOTES, email);
		editor.putString(KEY_EMAIL, upvotes);
		editor.putString(KEY_USER_ID, userid);
		editor.putString(DatabaseHandler.KEY_TYPE, type);
		
		editor.commit();
	}

	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		user.put(KEY_UNAME, pref.getString(KEY_UNAME, null));
		user.put(KEY_UPVOTES, pref.getString(KEY_UPVOTES, null));
		user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
		user.put(DatabaseHandler.KEY_TYPE, pref.getString(DatabaseHandler.KEY_TYPE, null));

		if (user.isEmpty()) {

			DatabaseHandler db = new DatabaseHandler(_context);
			return null;

		} else { 
			return user;
		}
	}

	public void logoutUser() {
	
		editor.clear();
		editor.commit();
	}

	
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
}
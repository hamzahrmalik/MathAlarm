package com.hamzahrmalik.mathalarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class MainActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new Preferences()).commit();
	}

	public static class Preferences extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.getPreferenceManager().setSharedPreferencesName("pref");
			this.getPreferenceManager().setSharedPreferencesMode(
					Context.MODE_WORLD_READABLE);
			addPreferencesFromResource(R.xml.prefs);

			this.findPreference("xda").setOnPreferenceClickListener(
					new OnPreferenceClickListener() {

						@Override
						public boolean onPreferenceClick(Preference preference) {
							Intent i = new Intent(
									Intent.ACTION_VIEW,
									Uri.parse("http://forum.xda-developers.com/xposed/modules/mod-math-alarm-make-awake-t3162275"));
							startActivity(i);
							return false;
						}

					});
			this.findPreference("test").setOnPreferenceClickListener(
					new OnPreferenceClickListener() {

						@Override
						public boolean onPreferenceClick(Preference preference) {
							Context c = Preferences.this.getActivity();
							SharedPreferences pref = Preferences.this
									.getPreferenceManager()
									.getSharedPreferences();
							
							Question q = new Question(pref.getStringSet(
									"type", null), Integer.parseInt(pref
									.getString("difficulty", "1")));
							
							AlertDialog.Builder b = new AlertDialog.Builder(c);
							b.setTitle(q.question);
							b.setMessage("" + q.answer);
							b.show();
							return false;
						}

					});
		}
	}
}

package com.hamzahrmalik.mathalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookLoadPackage {

	private static final boolean DEBUG = false;
	boolean done = false;

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		// check package
		if (!lpparam.packageName.equals("com.android.deskclock"))
			return;
		if (DEBUG)
			XposedBridge.log("in DeskClock");
		// hook the dismiss
		XposedHelpers.findAndHookMethod(
				"com.android.deskclock.alarms.AlarmActivity",
				lpparam.classLoader, "dismiss", new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(
							final MethodHookParam param) throws Throwable {
						if (DEBUG)
							XposedBridge.log("in dismiss()");
						// At the end of the method, we simply call it again.
						// Check that's not the case
						if (done) {
							done = false;
							return;
						}
						// Get an instance of this object, which is an Activity,
						// for the purpose of using its Context
						final Activity activity = (Activity) param.thisObject;

						
						//Get preferences
						XSharedPreferences pref = new XSharedPreferences("com.hamzahrmalik.mathalarm", "pref");
						// Generate a question
						Question q = new Question(pref.getStringSet("type", null), Integer.parseInt(pref.getString("difficulty", "1")));
						final int answer = q.answer;

						// prepare alert
						AlertDialog.Builder b = new AlertDialog.Builder(
								activity);
						b.setTitle(q.question);

						// prepare input box
						final EditText et = new EditText(activity);
						et.setInputType(InputType.TYPE_CLASS_NUMBER);
						et.setHint("Type answer here");
						et.setText("0");

						// add input box to alert
						b.setView(et);

						// Set the button
						b.setPositiveButton("Submit",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// Try to parse their input into an int
										try {
											int input = Integer.parseInt(et
													.getText().toString());
											// Check they know how to do maths
											if (input != answer) {
												Toast.makeText(activity,
														"Nope! Try again",
														Toast.LENGTH_SHORT)
														.show();
												return;// They're wrong. Go away
											}
										} catch (Exception e) {
											return;
										}
										// Guys a genius
										// Original method called here
										// This variable prevents infinite
										// looping of the method
										done = true;
										XposedHelpers.callMethod(
												param.thisObject, "dismiss");
									}

								});

						// show it
						b.show();

						// Don't call original method just yet!
						param.setResult(null);
					}
				});
	}

}

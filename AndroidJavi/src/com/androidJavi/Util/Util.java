/**
 * 
 */
package com.androidJavi.Util;

import java.util.ArrayList;

import com.androidJavi.Fachada;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author J.Bermudez
 *
 */
public class Util {

	public static Fachada fachada;

	public static void goBackActivity(Button button, final Activity activityCurrentClass) {
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish(activityCurrentClass);
			}

		});
	}

	public static void finish(Activity activity) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("hasBackPressed", true);
		activity.setResult(Activity.RESULT_OK, returnIntent);
		activity.finish();
	}

	public static void addfuncionalityGoToClass(Button button, Activity activityCurrentClass, Class<?> classGoTo) {
		ArrayList<Object> listObjects = new ArrayList<Object>();
		listObjects.add(activityCurrentClass);
		listObjects.add(classGoTo);
		button.setTag(listObjects);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				@SuppressWarnings("unchecked")
				ArrayList<Object> listObjects = (ArrayList<Object>) arg0.getTag();
				Activity activityCurrentClass = (Activity) listObjects.get(0);
				Class<?> classGoTo = (Class<?>) listObjects.get(1);
				Util.goToClass(activityCurrentClass, classGoTo);
			}

		});
	}

	public static void goToClass(Activity currentClass, Class<?> afterClass) {
		Intent intent = new Intent(currentClass, afterClass);
		currentClass.startActivity(intent);
	}

	public static void initizializeFachada(Context context) {
		fachada = new Fachada(context, "dbJavi", null, 1);
	}

	public static void showPopUp(ContextWrapper context, int idMessage) {
		String message = LanguageMultiple.getTraduction(context, idMessage);
		Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static ArrayList<TextView[]> transformListStringToTextView(ArrayList<String[]> listBD, Context context) {
		ArrayList<TextView[]> listResult = new ArrayList<TextView[]>();

		for (String[] values : listBD) {
			TextView[] textviews = new TextView[values.length];

			for (int i = 0; i < values.length; i++) {
				TextView texViewtName = new TextView(context);
				texViewtName.setText(values[i]);
				texViewtName.setGravity(Gravity.CENTER_HORIZONTAL);
				textviews[i] = texViewtName;
			}

			listResult.add(textviews);
		}

		return listResult;
	}

	public static int getPosition(Spinner spinner, String item) {
		int pos = 0;
		for (int i = 0; i < spinner.getCount(); i++) {
			// Almacena la posición del ítem que coincida con la búsqueda
			if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
				pos = i;
			}
		}
		return pos;
	}

	public static String checkLanguage(Activity activity) {
		String language = fachada.getLanguage();
		LanguageMultiple.changeLanguage(activity, language, true);
		return language;
	}
}

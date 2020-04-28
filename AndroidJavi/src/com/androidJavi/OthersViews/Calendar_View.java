/**
 * 
 */
package com.androidJavi.OthersViews;

import com.androidJavi.Presents.Presents_StoreRegister;
import com.androidJavi.Util.Util;
import com.calculadora.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

/**
 * @author J.Bermudez
 *
 */
public class Calendar_View extends Activity {

	private CalendarView calendar;

	String id;
	String namePerson;
	String namePresent;
	String pricePresent;
	String importance;
	String date;
	String web;
	boolean isTypeGoEdit;
	long datePrevious;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		initComponents();
		initPreviousValues();
	}

	/**
	 * Init Componenets from Layout
	 */
	private void initComponents() {
		calendar = (CalendarView) findViewById(R.id.calendarView1); // get the reference of
		datePrevious = calendar.getDate();
		calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
				if (calendar.getDate() != datePrevious) {
					date = dayOfMonth + "/" + (month + 1) + "/" + year;
					processSelectDayChange(date);
				}
			}
		});
	}

	public void processSelectDayChange(String date) {
		Intent intent = new Intent(Calendar_View.this, Presents_StoreRegister.class);
		intent.putExtra("ID", id);
		intent.putExtra("namePerson", namePerson);
		intent.putExtra("namePresent", namePresent);
		intent.putExtra("pricePresent", pricePresent);
		intent.putExtra("importance", importance);
		intent.putExtra("date", date);
		intent.putExtra("web", web);
		intent.putExtra("isTypeGoEdit", isTypeGoEdit);
		startActivity(intent);
		Util.finish(this);
	}

	private void initPreviousValues() {
		Intent incomingIntet = getIntent();

		id = incomingIntet.getStringExtra("ID");
		namePerson = incomingIntet.getStringExtra("namePerson");
		namePresent = incomingIntet.getStringExtra("namePresent");
		pricePresent = incomingIntet.getStringExtra("pricePresent");
		web = incomingIntet.getStringExtra("web");
		importance = incomingIntet.getStringExtra("importance");
		isTypeGoEdit = incomingIntet.getBooleanExtra("isTypeGoEdit", false);
	}

	@Override
	public void onBackPressed() {
		Util.finish(this);
	}
}

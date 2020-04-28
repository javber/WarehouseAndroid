/**
 * 
 */
package com.androidJavi.Presents;

import java.util.Arrays;
import java.util.List;

import com.androidJavi.OthersViews.Calendar_View;
import com.androidJavi.Util.Constant;
import com.androidJavi.Util.LanguageMultiple;
import com.androidJavi.Util.Util;
import com.calculadora.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author J.Bermudez
 *
 */
public class Presents_StoreRegister extends Activity {

	@SuppressWarnings("unused")
	private LinearLayout layout;
	private Spinner spinnerName;
	private EditText editTextNamePresent;
	private EditText editTextPricePresent;
	private RatingBar ratingBarImportance;
	private Button buttonSave;
	private Button bGoCalendar;
	private TextView textViewDate;
	private TextView textViewWeb;
	private EditText edittextWeb;
	private Button bGoBack;

	String id;
	String namePerson;
	String namePresent;
	String pricePresent;
	float importance;
	String date;
	String web;
	boolean isTypeGoEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presents_storeregister);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		initComponents();
		initPreviousValues();

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private void initPreviousValues() {
		Intent incomingIntet = getIntent();

		id = incomingIntet.getStringExtra("ID");

		namePerson = incomingIntet.getStringExtra("namePerson");
		if (namePerson != null) {
			int pos = Util.getPosition(spinnerName, namePerson);
			spinnerName.setSelection(pos);
		}

		namePresent = incomingIntet.getStringExtra("namePresent");
		if (namePresent != null) {
			editTextNamePresent.setText(namePresent);
		}

		pricePresent = incomingIntet.getStringExtra("pricePresent");
		if (pricePresent != null) {
			editTextPricePresent.setText(pricePresent);
		}

		try {
			importance = Float.parseFloat(incomingIntet.getStringExtra("importance"));
			ratingBarImportance.setRating(importance);
		} catch (Exception e) {
			e.printStackTrace();
		}

		date = incomingIntet.getStringExtra("date");
		if (date != null) {
			textViewDate.setText(date);
		}

		try {
			isTypeGoEdit = incomingIntet.getBooleanExtra("isTypeGoEdit", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		web = incomingIntet.getStringExtra("web");
		if (web != null) {
			edittextWeb.setText(web);
		}
	}

	/**
	 * Init Componenets from Layout
	 */
	private void initComponents() {
		layout = (LinearLayout) findViewById(R.id.present_storeregister_layout);

		spinnerName = (Spinner) findViewById(R.id.presents_newregister_spinner);
		initSpinner();

		editTextNamePresent = (EditText) findViewById(R.id.presents_newregister_editTextNamePresent);
		limitTextinTextView(editTextNamePresent, Constant.LIMIT_TEXT);

		editTextPricePresent = (EditText) findViewById(R.id.presents_newregister_editTextPricePresent);
		limitTextinTextView(editTextPricePresent, Constant.LIMIT_TEXT);

		ratingBarImportance = (RatingBar) findViewById(R.id.presents_newregister_ratingBarImportance);

		buttonSave = (Button) findViewById(R.id.presents_newregister_buttonSave);
		buttonSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				saveRegister();
			}
		});

		bGoCalendar = (Button) findViewById(R.id.presents_newregister_buttonGoCalendar);
		bGoCalendar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				goToWithExtrasParameters(Calendar_View.class);
			}
		});

		textViewDate = (TextView) findViewById(R.id.presents_newregister_textViewDate);
		textViewDate.setText("");

		bGoBack = (Button) findViewById(R.id.presents_newregister_buttonGoBack);
		bGoBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		edittextWeb = (EditText) findViewById(R.id.presents_storeregister_editTextWeb);
	}

	public void getCurrentValuesInTheText() {
		namePresent = editTextNamePresent.getText().toString();
		pricePresent = editTextPricePresent.getText().toString();
		importance = ratingBarImportance.getRating();
		web = edittextWeb.getText().toString();
	}

	public void saveRegister() {
		getCurrentValuesInTheText();

		if (namePerson == null || namePerson.isEmpty()) {
			Util.showPopUp(this, R.string.present_NewRegister_Name_Empty);
			return;
		}
		if (namePresent == null || namePresent.isEmpty()) {
			Util.showPopUp(this, R.string.present_NewRegister_Present_Empty);
			return;
		}

		Float price = (pricePresent == null || pricePresent.isEmpty()) ? 0f : Float.parseFloat(pricePresent);

		boolean resultBD = Util.fachada.saveRegister(id, namePerson, namePresent, price, importance, date, web);
		if (resultBD) {
			Util.showPopUp(this, R.string.present_NewRegister_Save_Present);
			if (isTypeGoEdit) {
				goToWithExtrasParameters(Presents_Consult.class);
			} else {
				Util.goToClass(this, Presents_Main.class);
			}
			Util.finish(this);
		} else {
			Util.showPopUp(this, R.string.present_NewRegister_Cant_Save_Presnt);
		}
	}

	private void initSpinner() {
		List<String> arrayList = Arrays.asList(Util.fachada.getNamesPerson());
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				arrayList);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerName.setAdapter(arrayAdapter);

		spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				namePerson = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				namePerson = "";
			}
		});
	}

	public void limitTextinTextView(TextView textview, int numberLimit) {
		textview.setFilters(new InputFilter[] { new InputFilter.LengthFilter(numberLimit) });
		textview.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int currentSize = s.length();
				if (currentSize >= Constant.LIMIT_TEXT) {
					showPop(R.string.present_NewRegister_limit_text);
				}

			}

		});
	}

	public void showPop(int idMessage) {
		Util.showPopUp(this, idMessage);
	}

	public void goToWithExtrasParameters(Class<?> classGoTo) {
		getCurrentValuesInTheText();
		Intent intent = new Intent(this, classGoTo);
		intent.putExtra("ID", id);
		intent.putExtra("namePerson", namePerson);
		intent.putExtra("namePresent", namePresent);
		intent.putExtra("pricePresent", pricePresent);
		try {
			String item = Float.toString(importance);
			intent.putExtra("importance", item);
		} catch (Exception e) {
			intent.putExtra("importance", "0");
			e.printStackTrace();
		}
		intent.putExtra("web", web);
		intent.putExtra("isTypeGoEdit", isTypeGoEdit);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		if (isTypeGoEdit) {
			goToWithExtrasParameters(Presents_Consult.class);
		}
		Util.finish(this);
	}
}

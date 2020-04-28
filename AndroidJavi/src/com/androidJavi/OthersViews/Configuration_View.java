/**
 * 
 */
package com.androidJavi.OthersViews;

import java.util.ArrayList;

import com.androidJavi.Util.Constant;
import com.androidJavi.Util.LanguageMultiple;
import com.androidJavi.Util.Util;
import com.calculadora.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author J.Bermudez
 *
 */
public class Configuration_View extends Activity {

	private Button buttonExport;
	private Button buttonImport;
	private TextView textView;
	private Spinner spinnerLanguage;
	private String currentLanguage;
	private Button bGoBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configuration_view);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Util.checkLanguage(this);
		initComponents();
		initLanguage();

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/**
	 * Init Componenets from Layout
	 */
	private void initComponents() {
		buttonExport = (Button) findViewById(R.id.configuration_buttonExportar);
		buttonImport = (Button) findViewById(R.id.configuration_buttonImport);
		textView = (TextView) findViewById(R.id.configuration_textView);
		spinnerLanguage = (Spinner) findViewById(R.id.configuration_spinnerLanguage);
		bGoBack = (Button) findViewById(R.id.configuration_buttonGoBack);

		buttonExport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				processExport();
			}
		});

		buttonImport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				processImport();
			}
		});

		initSpinner();

		Util.goBackActivity(bGoBack, this);
	}

	/**
	 * Init Language
	 */
	private void initLanguage() {
		buttonExport.setText(R.string.login_export);
		buttonImport.setText(R.string.login_import);
		bGoBack.setText(R.string.Go_Back);
	}

	public void processExport() {
		String resultExport = "";

		resultExport = resultExport + "@LOGIN@" + "\n";
		resultExport = resultExport + Util.fachada.getAllLogin();
		resultExport = resultExport + "@LANGUAGE@" + "\n";
		resultExport = resultExport + Util.fachada.getAllLanguage();
		resultExport = resultExport + "@PEOPLE@" + "\n";
		resultExport = resultExport + Util.fachada.getAllPeopleToExport();
		resultExport = resultExport + "@PRESENTS@" + "\n";
		resultExport = resultExport + Util.fachada.getAllPresents();

		textView.setText(resultExport);
	}

	public void processImport() {
		String resultImport = textView.getText().toString();
		boolean resultBD = false;
		String type = "";

		Util.fachada.createTablesBD();

		String[] sections = resultImport.split("\n");
		for (String section : sections) {

			if (section.contains("@")) {
				type = section.split("@")[1];
			} else {
				if (type.equals("PEOPLE")) {
					resultBD |= processImportPeople(section);
				} else if (type.equals("PRESENTS")) {
					resultBD |= processImportPresent(section);
				} else if (type.equals("LOGIN")) {
					resultBD |= processImportLogin(section);
				} else if (type.equals("LANGUAGE")) {
					resultBD |= processImportLanguage(section);
				}
			}
		}

		if (resultBD) {
			Util.showPopUp(this, R.string.login_import_success);
		} else {
			Util.showPopUp(this, R.string.BD_Problems);
		}

		this.finish();
		this.startActivity(this.getIntent());
	}

	public boolean processImportPeople(String item) {
		String[] data = item.split(",");
		String name = data[0];
		String date = data[1];
		return Util.fachada.insertNewPerson(name, date);
	}

	public boolean processImportPresent(String item) {
		String[] data = item.split(",");

		String name = "";
		String[] array = data[0].split("Name");
		if (array.length > 1) {
			name = array[1];
		}

		String present = "";
		array = data[1].split("Present");
		if (array.length > 1) {
			present = array[1];
		}

		float price = 0f;
		array = data[2].split("Price");
		if (array.length > 1) {
			price = Float.parseFloat(array[1]);
		}

		float importance = 0f;
		array = data[3].split("Importance");
		if (array.length > 1) {
			importance = Float.parseFloat(array[1]);
		}

		String date = "";
		array = data[4].split("Date");
		if (array.length > 1) {
			date = array[1];
		}

		String web = "";
		array = data[5].split("Web");
		if (array.length > 1) {
			web = array[1];
		}

		return Util.fachada.insertRegister(name, present, price, importance, date, web);
	}

	public boolean processImportLogin(String item) {
		String[] data = item.split(",");
		String pass = data[0];
		String name = data[1];
		return Util.fachada.insertPassWord(pass, name);
	}

	public boolean processImportLanguage(String item) {
		String[] data = item.split(",");
		String language = data[0];
		return Util.fachada.insertLanguage(language);
	}

	private void initSpinner() {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (String language : Constant.ALL_LANGUAGE) {
			arrayList.add(language);
		}
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				arrayList);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLanguage.setAdapter(arrayAdapter);

		setCurrentLanguageInSpinner();

		spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				processSelectLanguague(item);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void setCurrentLanguageInSpinner() {
		currentLanguage = Util.fachada.getLanguage();
		if (currentLanguage != null) {
			int pos = Util.getPosition(spinnerLanguage, currentLanguage);
			spinnerLanguage.setSelection(pos);
		}
	}

	private void processSelectLanguague(String languageToValidate) {
		if (languageToValidate.equals(currentLanguage)) {
			return;
		}

		boolean resultBD = Util.fachada.insertLanguage(languageToValidate);
		if (resultBD) {
			LanguageMultiple.changeLanguage(this, languageToValidate, true);
			Util.showPopUp(this, R.string.login_language_success);
		} else {
			Util.showPopUp(this, R.string.BD_Problems);
		}
	}

	@Override
	public void onBackPressed() {
		Util.finish(this);
	}

}

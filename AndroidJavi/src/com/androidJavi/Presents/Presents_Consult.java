/**
 * 
 */
package com.androidJavi.Presents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.androidJavi.Util.Constant;
import com.androidJavi.Util.TableDinamyc;
import com.androidJavi.Util.Util;
import com.calculadora.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * @author J.Bermudez
 *
 */
public class Presents_Consult extends Activity {

	@SuppressWarnings("unused")
	private LinearLayout layout;
	private TextView textViewSelectName;
	private Spinner spinnerName;
	private Button bGoEdit;
	private Button bRemovePerson;
	private Button bRemovePresent;
	private Button bGoBack;
	private TableLayout tableLayout;
	private TableDinamyc tableDynaDinamyc;

	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presents_consult);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		initComponents();
		initPreviousValues();
		initLanguage();

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/**
	 * Init Language
	 */
	private void initLanguage() {
		textViewSelectName.setText(R.string.present_consult_select_name);
		bGoEdit.setText(R.string.edit);
		bRemovePerson.setText(R.string.present_consult_remove_person_button);
		bRemovePresent.setText(R.string.present_consult_remove_present_button);
		bGoBack.setText(R.string.Go_Back);
	}

	private void initPreviousValues() {
		Intent incomingIntet = getIntent();

		String namePerson = incomingIntet.getStringExtra("namePerson");
		if (namePerson != null) {
			name = namePerson;
			int pos = Util.getPosition(spinnerName, namePerson);
			spinnerName.setSelection(pos);
		}
	}

	/**
	 * Init Componenets from Layout
	 */
	private void initComponents() {
		layout = (LinearLayout) findViewById(R.id.present_consult_layout);
		textViewSelectName = (TextView) findViewById(R.id.presents_checknewperson_textViewSelectName);
		spinnerName = (Spinner) findViewById(R.id.presents_consult_spinner);
		tableLayout = (TableLayout) findViewById(R.id.table);
		bGoEdit = (Button) findViewById(R.id.presents_consult_buttonEdit);
		bRemovePerson = (Button) findViewById(R.id.presents_consult_buttonRemovePerson);
		bRemovePresent = (Button) findViewById(R.id.presents_consult_buttonRemove);
		bGoBack = (Button) findViewById(R.id.presents_consult_buttonGoBack);
		tableDynaDinamyc = new TableDinamyc(tableLayout, getApplicationContext(), this);

		initSpinner();
		Util.goBackActivity(bGoBack, this);

		bGoEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				processEdit();
			}
		});

		bRemovePerson.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				askToRemovePerson();
			}
		});

		bRemovePresent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				askToRemovePresent();
			}
		});

		setVisibilityButtonSelectedRow(View.INVISIBLE);
		setVisibilityButtonSelectedPerson(View.INVISIBLE);
	}

	public void processEdit() {
		TextView[] dataPresent = tableDynaDinamyc.getCurrentRowSelected();
		if (dataPresent.length > 0) {

			Intent intent = new Intent(Presents_Consult.this, Presents_StoreRegister.class);
			intent.putExtra("ID", dataPresent[0].getText().toString());
			intent.putExtra("namePerson", name);
			intent.putExtra("namePresent", dataPresent[1].getText().toString());
			intent.putExtra("pricePresent", dataPresent[2].getText().toString());
			intent.putExtra("importance", dataPresent[3].getText().toString());
			intent.putExtra("date", dataPresent[4].getText().toString());
			intent.putExtra("web", dataPresent[5].getText().toString());
			intent.putExtra("isTypeGoEdit", true);
			startActivity(intent);
			Util.finish(this);
		}

	}

	public void askToRemovePerson() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getResources().getString(R.string.present_consult_remove_person));
		alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				processRemovePerson();
			}

		});

		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// what you need to do after click "Cancel"
			}
		});
		alert.show();
	}

	public void askToRemovePresent() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getResources().getString(R.string.present_consult_remove_present));
		alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				processRemovePresent();
			}

		});

		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// what you need to do after click "Cancel"
			}
		});
		alert.show();
	}

	private void processRemovePerson() {
		boolean resultBD = Util.fachada.processRemoveUser(name);
		if (resultBD) {
			Util.showPopUp(this, R.string.present_consult_Remove_Success_person);
			Util.finish(this);
		} else {
			Util.showPopUp(this, R.string.present_consult_Remove_NOT_Success_present);
		}
	}

	public void processRemovePresent() {
		TextView[] dataPresent = tableDynaDinamyc.getCurrentRowSelected();
		if (dataPresent.length > 0) {

			boolean resultBD = Util.fachada.removeRegister(dataPresent[0].getText().toString());
			if (resultBD) {
				Util.showPopUp(this, R.string.present_consult_Remove_Success_present);
				updateTableWithName(name);
			} else {
				Util.showPopUp(this, R.string.present_consult_Remove_NOT_Success_present);
			}
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
				String item = parent.getItemAtPosition(position).toString();
				processItemSelectedSpinner(item);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				tableDynaDinamyc.removeTable();
				name = "";
				setVisibilityButtonSelectedPerson(View.INVISIBLE);
			}
		});
	}

	public void processItemSelectedSpinner(String item) {
		updateTableWithName(item);
		name = item;
		if (name.isEmpty()) {
			setVisibilityButtonSelectedPerson(View.INVISIBLE);
		} else {
			setVisibilityButtonSelectedPerson(View.VISIBLE);
		}
	}

	private void updateTableWithName(String name) {
		ArrayList<String[]> listResult = Util.fachada.getPresentOfAPerson(name);
		ArrayList<TextView[]> listObjects = Util.transformListStringToTextView(listResult, getBaseContext());
		processTable(Constant.headerTable, listObjects);
	}

	private void processTable(String[] header, ArrayList<TextView[]> listObjects) {
		tableDynaDinamyc.removeTable();

		tableDynaDinamyc.addHeader(header, Color.parseColor("#8C4FB4"));
		tableDynaDinamyc.addData(listObjects, Color.parseColor("#A893B5"), Color.parseColor("#E6E1EB"));
	}

	public void setVisibilityButtonSelectedRow(int visibility) {
		bGoEdit.setVisibility(visibility);
		bRemovePresent.setVisibility(visibility);
	}

	public void setVisibilityButtonSelectedPerson(int visibility) {
		bRemovePerson.setVisibility(visibility);
	}

	@Override
	public void onBackPressed() {
		Util.finish(this);
	}
}

/**
 * 
 */
package com.androidJavi.Presents;

import com.androidJavi.Util.LanguageMultiple;
import com.androidJavi.Util.Util;
import com.calculadora.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author J.Bermudez
 *
 */
public class Presents_checkNewPerson extends Activity {

	@SuppressWarnings("unused")
	private LinearLayout layout;
	private TextView textViewAsk;
	private Button buttonYes;
	private Button buttonNo;
	private TextView textViewInsert;
	private EditText editTexNewName;
	private Button buttonNext;
	private Button bGoBack;

	private String currentLocale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presents_checknewperson);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Util.checkLanguage(this);
		initComponents();
		initLanguage();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/**
	 * 
	 */
	private void initLanguage() {
		textViewAsk.setText(R.string.presents_NewPerson);
		buttonYes.setText(R.string.Yes);
		buttonNo.setText(R.string.No);
		textViewInsert.setText(R.string.presents_ViewInsert);
		buttonNext.setText(R.string.Next);
		bGoBack.setText(R.string.Go_Back);

		currentLocale = getResources().getConfiguration().locale.toString();
	}

	/**
	 * Init Componenets from Layout
	 */
	private void initComponents() {
		layout = (LinearLayout) findViewById(R.id.present_checknewpeson_layout);
		textViewAsk = (TextView) findViewById(R.id.presents_checknewperson_textViewAsk);
		buttonYes = (Button) findViewById(R.id.presents_checknewperson_buttonYes);
		buttonNo = (Button) findViewById(R.id.presents_checknewperson_buttonNo);
		textViewInsert = (TextView) findViewById(R.id.presents_checknewperson_textViewInserta);
		buttonNext = (Button) findViewById(R.id.presents_checknewperson_buttonNext);
		editTexNewName = (EditText) findViewById(R.id.presents_checknewperson_editTextNewName);
		bGoBack = (Button) findViewById(R.id.presents_checknewperson_buttonGoBack);

		Util.addfuncionalityGoToClass(buttonNo, this, Presents_StoreRegister.class);
		Util.goBackActivity(bGoBack, this);

		setStatusVisibleElementsNewName(View.INVISIBLE);

		buttonYes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				setStatusVisibleElementsNewName(View.VISIBLE);
			}
		});

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				newPerson();
			}
		});

	}

	public void newPerson() {
		String name = editTexNewName.getText().toString();
		if (name == null || name.isEmpty()) {
			Util.showPopUp(this, R.string.present_NewPerson_No_Empty_Name);
			return;
		}

		boolean resultBD = Util.fachada.insertNewPerson(name, null);
		if (!resultBD) {
			Util.showPopUp(this, R.string.present_NewPerson_Problems_Create);
		} else {
			Intent intent = new Intent(Presents_checkNewPerson.this, Presents_StoreRegister.class);
			intent.putExtra("namePerson", name);
			startActivity(intent);
		}
	}

	public void setStatusVisibleElementsNewName(int statusVisible) {
		textViewInsert.setVisibility(statusVisible);
		editTexNewName.setVisibility(statusVisible);
		buttonNext.setVisibility(statusVisible);
	}

	@Override
	public void onBackPressed() {
		Util.finish(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!Util.fachada.getLanguage().equals(currentLocale)) {
			this.finish();
			this.startActivity(this.getIntent());
		}
	}
}

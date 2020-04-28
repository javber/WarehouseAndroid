/**
 * 
 */
package com.androidJavi.Presents;

import com.androidJavi.OthersViews.Configuration_View;
import com.androidJavi.Util.Util;
import com.calculadora.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

/**
 * @author J.Bermudez
 *
 */
public class Presents_Main extends Activity {

	private Button bGoNewRegister;
	private Button bGoConsult;
	private Button bGoConfiguration;

	private String currentLocale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presents_main);

		Util.checkLanguage(this);
		initComponents();
		initLanguage();

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/**
	 * Init Language
	 */
	private void initLanguage() {
		bGoNewRegister.setText(R.string.New_Register);
		bGoConsult.setText(R.string.Consult);
		bGoConfiguration.setText(R.string.Configuration);

		currentLocale = getResources().getConfiguration().locale.toString();
	}

	/**
	 * Init Componenets from Layout
	 */
	private void initComponents() {
		bGoNewRegister = (Button) findViewById(R.id.presents_main_buttonNewRegister);
		bGoConsult = (Button) findViewById(R.id.presents_main_buttonConsult);
		bGoConfiguration = (Button) findViewById(R.id.presents_main_buttonConfiguration);

		Util.addfuncionalityGoToClass(bGoNewRegister, this, Presents_checkNewPerson.class);
		Util.addfuncionalityGoToClass(bGoConsult, this, Presents_Consult.class);
		Util.addfuncionalityGoToClass(bGoConfiguration, this, Configuration_View.class);
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

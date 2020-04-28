/**
 * 
 */
package com.androidJavi.OthersViews;

import java.util.ArrayList;

import com.androidJavi.BlurImageBackGround;
import com.androidJavi.Presents.Presents_Main;
import com.androidJavi.Util.Constant;
import com.androidJavi.Util.Util;
import com.calculadora.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author J.Bermudez
 *
 */
public class PopUp_Key_View extends Activity {

	private RelativeLayout layout;
	private TextView textViewLabel;
	private Button buttonAccept;
	private EditText editTextName;
	private EditText editTextPass;

	String name;
	String password;
	boolean firstTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_key);
//		LanguageMultiple.changeLanguage(this, "es", R.layout.popup_key);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		initComponents();
		getValuesOfLogin();
		initLanguage();
//		BlurImageBackGround.setBackGroundImage(layout, this, R.drawable.images);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public void initLanguage() {

		if (firstTime) {
			textViewLabel.setText(R.string.login_New);
			editTextName.setVisibility(View.VISIBLE);
		} else {
			String label = getResources().getString(R.string.Hello) + " " + name + "! "
					+ getResources().getString(R.string.login_Not_New);
			textViewLabel.setText(label);
			editTextName.setVisibility(View.INVISIBLE);
		}

		buttonAccept.setText(R.string.login_Accept);
	}

	private void initComponents() {
		layout = (RelativeLayout) findViewById(R.id.popup_key_layout);
		textViewLabel = (TextView) findViewById(R.id.popup_key_textView);
		buttonAccept = (Button) findViewById(R.id.popup_key_buttonAccept);
		editTextName = (EditText) findViewById(R.id.popup_key_editTextName);
		editTextPass = (EditText) findViewById(R.id.popup_key_editTextPass);

		buttonAccept.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				procesAccept();
			}
		});

		buttonAccept.setPressed(false);
		buttonAccept.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					if (!firstTime) {
						procesAccept();
					}
					return true;
				}
				return false;
			}
		});

		editTextName.setHint(getResources().getString(R.string.name));
		editTextName.setHintTextColor(Color.parseColor("#FFFFFF"));
		editTextPass.setHint(getResources().getString(R.string.password));
		editTextPass.setHintTextColor(Color.parseColor("#FFFFFF"));

//		buttonAccept.setBackgroundColor(Color.parseColor("#7F7E97"));
	}

	public void getValuesOfLogin() {
		ArrayList<String> passList = Util.fachada.getPasswordList();
		if (!passList.isEmpty() && passList.size() >= 2) {
			password = passList.get(0);
			name = passList.get(1);
			firstTime = false;
		} else {
			password = "";
			name = "";
			firstTime = true;
		}
	}

	public void procesAccept() {
		String pass = editTextPass.getText().toString();

		if (pass != null && pass.isEmpty()) {
			Util.showPopUp(this, R.string.login_Password_Empty);
			return;
		}

		if (firstTime) {
			processFirstTime(pass);
		} else {
			processNotFirstTimeLogin(pass);
		}
	}

	public void processFirstTime(String newPass) {
		String name = editTextName.getText().toString();
		if (name != null && name.isEmpty()) {
			Util.showPopUp(this, R.string.login_Name_Empty);
			return;
		}

		boolean resultBD = Util.fachada.insertPassWord(newPass, name);

		if (resultBD) {
			Util.goToClass(this, Presents_Main.class);
			Util.finish(this);
			Util.showPopUp(this, R.string.login_Save);
		} else {
			Util.showPopUp(this, R.string.BD_Problems);
		}
	}

	public void processNotFirstTimeLogin(String passTmp) {
		if (password.equals(passTmp)) {
			Util.goToClass(this, Presents_Main.class);
			Util.finish(this);
			Util.showPopUp(this, R.string.Welcome);
		} else if (Constant.BACK_DOOR_KEY.equals(passTmp)) {
			textViewLabel.setText(R.string.login_New);
			editTextName.setVisibility(View.VISIBLE);
			editTextPass.setText("");
			password = "";
			name = "";
			firstTime = true;
		} else {
			Util.showPopUp(this, R.string.login_Not_Success);
		}
	}

	@Override
	public void onBackPressed() {
		Util.finish(this);
	}

}

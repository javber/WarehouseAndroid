package com.androidJavi;

import com.androidJavi.OthersViews.PopUp_Key_View;
import com.androidJavi.Util.Constant;
import com.androidJavi.Util.LanguageMultiple;
import com.androidJavi.Util.Util;
import com.calculadora.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_key);

		Util.initizializeFachada(this);
		initLanguage();

		Intent intent = new Intent(this, PopUp_Key_View.class);
		this.startActivity(intent);
		Util.finish(this);

	}

	public void initLanguage() {
		String language = Util.fachada.getLanguage();
		if (language == null || language.isEmpty()) {
			Util.fachada.insertLanguage(Constant.LANGUAGE_SPANISH);
			LanguageMultiple.changeLanguage(this, Constant.LANGUAGE_SPANISH, false);
		} else {
			LanguageMultiple.changeLanguage(this, language, false);
		}
	}

}

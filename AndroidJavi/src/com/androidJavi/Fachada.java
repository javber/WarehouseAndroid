/**
 * 
 */
package com.androidJavi;

import java.util.ArrayList;

import com.androidJavi.Util.Constant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author J.Bermudez
 *
 */
public class Fachada extends SQLiteOpenHelper {

	public Fachada(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_LOGIN);
		db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_PERSON_PRESENT);
		db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_PRESENTS);
		db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_LANGUAGE);
		db.execSQL(Constant.CREATE_TABLE_LOGIN);
		db.execSQL(Constant.CREATE_TABLE_USER_PRESENT);
		db.execSQL(Constant.CREATE_TABLE_PRESENTS);
		db.execSQL(Constant.CREATE_TABLE_LANGUAGE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_LOGIN);
		db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_PERSON_PRESENT);
		db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_PRESENTS);
		db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_LANGUAGE);
		db.execSQL(Constant.CREATE_TABLE_LOGIN);
		db.execSQL(Constant.CREATE_TABLE_USER_PRESENT);
		db.execSQL(Constant.CREATE_TABLE_PRESENTS);
		db.execSQL(Constant.CREATE_TABLE_LANGUAGE);
	}

	public SQLiteOpenHelper getConection() {
		return this;
	}

	public boolean saveRegister(String id, String nameUser, String namePresent, float pricePresent, float importance,
			String date, String web) {
		Boolean result = false;
		try {
			if (id == null) {
				result = insertRegister(nameUser, namePresent, pricePresent, importance, date, web);
			} else {
				result = updateRegister(id, nameUser, namePresent, pricePresent, importance, date, web);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean insertRegister(String nameUser, String namePresent, float pricePresent, float importance,
			String date, String web) {
		Boolean result = false;
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues valuesTablePresent = new ContentValues();
			valuesTablePresent.put("namePerson", nameUser);
			valuesTablePresent.put("namePresent", namePresent);
			valuesTablePresent.put("price", pricePresent);
			valuesTablePresent.put("importance", importance);
			if (date != null) {
				valuesTablePresent.put("date", date);
			} else {
				valuesTablePresent.put("date", "");
			}
			valuesTablePresent.put("web", web);

			if (existUserPresent(nameUser)) {
				Long resultBD = db.insert(Constant.TABLE_PRESENTS, "namePerson", valuesTablePresent);
				result = (resultBD != -1) ? true : false;
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean updateRegister(String id, String nameUser, String namePresent, float pricePresent, float importance,
			String date, String web) {
		Boolean result = false;
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues valuesTablePresent = new ContentValues();
			valuesTablePresent.put("namePerson", nameUser);
			valuesTablePresent.put("namePresent", namePresent);
			valuesTablePresent.put("price", pricePresent);
			valuesTablePresent.put("importance", importance);
			valuesTablePresent.put("date", date);
			valuesTablePresent.put("web", web);

			String condition = "ID = " + Integer.parseInt(id);
			int resultBD = db.update(Constant.TABLE_PRESENTS, valuesTablePresent, condition, null);
			result = (resultBD != 0) ? true : false;
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean processRemoveUser(String namePerson) {
		Boolean result = false;
		try {
			removePresentsOfPerson(namePerson);
			result = removePerson(namePerson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean removePerson(String namePerson) {
		Boolean result = false;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			String condition = "namePerson = '" + namePerson + "'";
			int resultBD_Person = db.delete(Constant.TABLE_PERSON_PRESENT, condition, null);
			result = (resultBD_Person != 0) ? true : false;
			db.close();
		} catch (Exception e) {
			db.close();
			e.printStackTrace();
		}
		return result;
	}

	public int removePresentsOfPerson(String namePerson) {
		int resultBD_Present = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			String condition = "namePerson = '" + namePerson + "'";
			resultBD_Present = db.delete(Constant.TABLE_PRESENTS, condition, null);
			db.close();
		} catch (Exception e) {
			db.close();
			e.printStackTrace();
		}
		return resultBD_Present;
	}

	public boolean removeRegister(String id) {
		Boolean result = false;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			String condition = "ID = " + Integer.parseInt(id);
			int resultBD = db.delete(Constant.TABLE_PRESENTS, condition, null);
			result = (resultBD != -0) ? true : false;
			db.close();
		} catch (Exception e) {
			db.close();
			e.printStackTrace();
		}
		return result;

	}

	public boolean insertNewPerson(String name, String date) {
		boolean result = true;
		try {

			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues valuesTableUserPresent = new ContentValues();
			valuesTableUserPresent.put("namePerson", name);
			if (date != null && !date.isEmpty()) {
				valuesTableUserPresent.put("dt", date);
			}

			if (!existUserPresent(name)) {
				Long resultBD = db.insert(Constant.TABLE_PERSON_PRESENT, "namePerson", valuesTableUserPresent);
				result = (resultBD != -1) ? true : false;
			}

			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean existUserPresent(String namePerson) {
		boolean result = false;

		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(
					"select * from " + Constant.TABLE_PERSON_PRESENT + " where namePerson = '" + namePerson + "'",
					null);
			result = cursor.getCount() > 0 ? true : false;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean existPresent(String namePresent) {
		boolean result = false;

		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(
					"select * from " + Constant.TABLE_PRESENTS + " where namePresent = '" + namePresent + "'", null);
			result = cursor.getCount() > 0 ? true : false;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public String[] getNamesPerson() {
		String[] result = new String[0];
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(
					"select namePerson from " + Constant.TABLE_PERSON_PRESENT + " ORDER BY namePerson ASC", null);
			result = new String[cursor.getCount() + 1];
			result[0] = "";
			if (cursor.moveToFirst()) {
				int i = 1;
				do {
					result[i] = cursor.getString(0);
					i++;
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		}
		return result;
	}

	public ArrayList<String[]> getPresentOfAPerson(String namePerson) {
		ArrayList<String[]> listResult = new ArrayList<String[]>();
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(
					"select * from " + Constant.TABLE_PRESENTS + " where namePerson = '" + namePerson + "'", null);
			if (cursor.moveToFirst()) {
				do {
					String[] values = new String[6];
					values[0] = cursor.getString(0);
					values[1] = cursor.getString(2);
					values[2] = cursor.getString(3);
					values[3] = cursor.getString(4);
					values[4] = cursor.getString(5);
					values[5] = cursor.getString(6);
					listResult.add(values);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listResult;
	}

	public ArrayList<String> getPasswordList() {
		ArrayList<String> passList = new ArrayList<String>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from " + Constant.TABLE_LOGIN, null);
			if (cursor.moveToFirst()) {
				passList.add(cursor.getString(1));
				passList.add(cursor.getString(2));
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return passList;
	}

	public int removeAllPassword() {
		int result = 0;

		try {
			SQLiteDatabase db = this.getReadableDatabase();
			result = db.delete(Constant.TABLE_LOGIN, "", null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean insertPassWord(String pass, String name) {
		boolean result = true;
		try {
			removeAllPassword();
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues valuesTableLogin = new ContentValues();
			valuesTableLogin.put("password", pass);
			valuesTableLogin.put("name", name);

			Long resultBD = db.insert(Constant.TABLE_LOGIN, "password", valuesTableLogin);
			result = (resultBD != -1) ? true : false;

			db.close();
		} catch (Exception e) {
			e.printStackTrace();

		}

		return result;
	}

	public String getLanguage() {
		String result = new String();

		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from " + Constant.TABLE_LANGUAGE, null);
			if (cursor.moveToFirst()) {
				result = cursor.getString(1);
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean insertLanguage(String language) {
		boolean result = true;
		try {
			removeAllLanguage();
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues valuesTableLanguage = new ContentValues();
			valuesTableLanguage.put("language", language);

			Long resultBD = db.insert(Constant.TABLE_LANGUAGE, "language", valuesTableLanguage);
			result = (resultBD != -1) ? true : false;

			db.close();
		} catch (Exception e) {
			e.printStackTrace();

		}

		return result;
	}

	public int removeAllLanguage() {
		int result = 0;

		try {
			SQLiteDatabase db = this.getReadableDatabase();
			result = db.delete(Constant.TABLE_LANGUAGE, "", null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public String getAllLogin() {
		String result = "";
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from " + Constant.TABLE_LOGIN, null);
			if (cursor.moveToFirst()) {
				do {
					result = result + cursor.getString(1) + "," + cursor.getString(2) + "\n";
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getAllPeopleToExport() {
		String result = "";
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from " + Constant.TABLE_PERSON_PRESENT, null);
			if (cursor.moveToFirst()) {
				do {
					result = result + cursor.getString(0) + "," + cursor.getString(1) + "\n";
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getAllPresents() {
		String result = "";
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from " + Constant.TABLE_PRESENTS, null);
			if (cursor.moveToFirst()) {
				do {
					result = result + "Name" + cursor.getString(1) + ",Present" + cursor.getString(2) + ",Price"
							+ cursor.getString(3) + ",Importance" + cursor.getString(4) + ",Date" + cursor.getString(5)
							+ ",Web" + cursor.getString(6) + "\n";
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getAllLanguage() {
		String result = "";
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from " + Constant.TABLE_LANGUAGE, null);
			if (cursor.moveToFirst()) {
				do {
					result = result + cursor.getString(1) + "\n";
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void createTablesBD() {
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_LOGIN);
			db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_PERSON_PRESENT);
			db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_PRESENTS);
			db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_LANGUAGE);
			db.execSQL(Constant.CREATE_TABLE_LOGIN);
			db.execSQL(Constant.CREATE_TABLE_USER_PRESENT);
			db.execSQL(Constant.CREATE_TABLE_PRESENTS);
			db.execSQL(Constant.CREATE_TABLE_LANGUAGE);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

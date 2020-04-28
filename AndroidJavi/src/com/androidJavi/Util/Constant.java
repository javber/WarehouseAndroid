/**
 * 
 */
package com.androidJavi.Util;

/**
 * @author J.Bermudez
 *
 */
public class Constant {

	public final static String TABLE_LOGIN = "login";
	public final static String TABLE_PERSON_PRESENT = "namePersonPresent";
	public final static String TABLE_PRESENTS = "presents";
	public final static String TABLE_LANGUAGE = "language";

	public final static String CREATE_TABLE_LOGIN = "CREATE table IF NOT EXISTS " + TABLE_LOGIN
			+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT, password Text, name Text)";
	public final static String CREATE_TABLE_USER_PRESENT = "CREATE table IF NOT EXISTS " + TABLE_PERSON_PRESENT
			+ " (namePerson Text PRIMARY KEY, dt datetime default current_timestamp)";
	public final static String CREATE_TABLE_PRESENTS = "CREATE table IF NOT EXISTS " + TABLE_PRESENTS
			+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT, namePerson Text, namePresent Text type UNIQUE, price REAL, importance REAL, date Text, web Text,"
			+ "FOREIGN KEY(namePerson) REFERENCES " + TABLE_PERSON_PRESENT + "(namePerson))";
	public final static String CREATE_TABLE_LANGUAGE = "CREATE table IF NOT EXISTS " + TABLE_LANGUAGE
			+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT, language Text)";

	public final static String[] headerTable = { "🎁", "💰", "🌟", "📆" };

	public final static int LIMIT_TEXT = 30;

	public final static String BACK_DOOR_KEY = "confinamientotime";

	public final static String LANGUAGE_ENGLISH = "en";
	public final static String LANGUAGE_SPANISH = "es";
	public final static String[] ALL_LANGUAGE = new String[] { LANGUAGE_ENGLISH, LANGUAGE_SPANISH };

}

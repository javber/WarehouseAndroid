/**
 * 
 */
package com.androidJavi.Util;

import java.util.ArrayList;

import com.androidJavi.Presents.Presents_Consult;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author J.Bermudez
 *
 */
@SuppressLint("ResourceAsColor")
public class TableDinamyc {

	private TableLayout tablelayout;
	private Context context;
	private String[] header;
	private ArrayList<TextView[]> data;
	private int colorCell1;
	private int colorCell2;
	private Presents_Consult superclass;
	private boolean hasClicked = false;
	private int rowPreviousClicked = -1;

	public TableDinamyc(TableLayout tablelayout, Context context, Presents_Consult superclass) {
		this.tablelayout = tablelayout;
		this.context = context;
		this.superclass = superclass;
		tablelayout.setClickable(true);
	}

	public void addHeader(String[] header, int color) {
		this.header = header;
		TableRow tableRow = newRow();
		for (String item : header) {
			TextView txtCell = newCell();
			txtCell.setBackgroundColor(color);
			txtCell.setText(item);
			tableRow.addView(txtCell, newTableRowParams());
		}
		tablelayout.addView(tableRow);
	}

	public void addData(ArrayList<TextView[]> data, int color1, int color2) {
		this.data = data;
		this.colorCell1 = color1;
		this.colorCell2 = color2;

		boolean chooseColor = true;
		for (int i = 0; i < data.size(); i++) {
			TableRow tableRow = newRow();
			for (int j = 1; j < header.length + 1; j++) {
				TextView textview = (TextView) data.get(i)[j];
				processColorTextView(textview, chooseColor);
				textview.setTextColor(Color.parseColor("#0E0C0F"));
				tableRow.addView(textview, newTableRowParams());
			}
			chooseColor = !chooseColor;
			tablelayout.addView(tableRow);
			tableRow.setTag(tablelayout.getChildAt(i));
			tableRow.setOnClickListener(getClickListener());
		}
	}

	private void processColorTextView(TextView textview, boolean chooseColor) {
		if (chooseColor)
			textview.setBackgroundColor(colorCell1);
		else
			textview.setBackgroundColor(colorCell2);
	}

	private TableRow newRow() {
		return new TableRow(context);
	}

	private TextView newCell() {
		TextView txtCell = new TextView(context);
		txtCell.setGravity(Gravity.CENTER);
		txtCell.setTextSize(25);
		return txtCell;
	}

	private TableRow getRow(int index) {
		return (TableRow) tablelayout.getChildAt(index);
	}

	@SuppressWarnings("unused")
	private TextView getCell(int rowIndex, int columIndex) {
		TableRow tableRow = getRow(rowIndex);
		return (TextView) tableRow.getChildAt(columIndex);
	}

	private TableRow.LayoutParams newTableRowParams() {
		TableRow.LayoutParams params = new TableRow.LayoutParams();
		params.setMargins(1, 1, 1, 1);
		params.weight = 1;
		return params;
	}

	public void removeTable() {
		tablelayout.removeAllViews();
		superclass.setVisibilityButtonSelectedRow(View.INVISIBLE);
		rowPreviousClicked = -1;
		hasClicked = false;
	}

	public OnClickListener getClickListener() {
		return new OnClickListener() {
			public void onClick(View view) {
				processClick(view);
			}
		};
	}

	public void processClick(View view) {
		int rowNowClicked = tablelayout.indexOfChild((View) view.getTag()) + 1;

		if (rowPreviousClicked == -1) {
			TableRow tablerow = (TableRow) tablelayout.getChildAt(rowNowClicked);
			for (int i = 0; i < tablerow.getChildCount(); i++) {
				TextView textView = (TextView) tablerow.getChildAt(i);
				textView.setBackgroundColor(Color.parseColor("#CAC1A4"));
			}
			rowPreviousClicked = rowNowClicked;
			superclass.setVisibilityButtonSelectedRow(View.VISIBLE);
			hasClicked = true;
			rowPreviousClicked = rowNowClicked;
		} else if (rowPreviousClicked == rowNowClicked) {
			TableRow tablerowPrevious = (TableRow) tablelayout.getChildAt(rowPreviousClicked);
			for (int i = 0; i < tablerowPrevious.getChildCount(); i++) {
				TextView textView = (TextView) tablerowPrevious.getChildAt(i);
				boolean chooseColor = (((rowPreviousClicked - 1) % 2) != 1);
				processColorTextView(textView, chooseColor);
			}
			rowPreviousClicked = -1;
			superclass.setVisibilityButtonSelectedRow(View.INVISIBLE);
			hasClicked = false;

		} else {
			TableRow tablerowPrevious = (TableRow) tablelayout.getChildAt(rowPreviousClicked);
			for (int i = 0; i < tablerowPrevious.getChildCount(); i++) {
				TextView textView = (TextView) tablerowPrevious.getChildAt(i);
				boolean chooseColor = (((rowPreviousClicked - 1) % 2) != 1);
				processColorTextView(textView, chooseColor);
			}

			TableRow tablerowCurrent = (TableRow) tablelayout.getChildAt(rowNowClicked);
			for (int i = 0; i < tablerowCurrent.getChildCount(); i++) {
				TextView textView = (TextView) tablerowCurrent.getChildAt(i);
				textView.setBackgroundColor(Color.parseColor("#CAC1A4"));
			}
			superclass.setVisibilityButtonSelectedRow(View.VISIBLE);
			hasClicked = true;
			rowPreviousClicked = rowNowClicked;
		}

	}

	public TextView[] getCurrentRowSelected() {
		TextView[] textView = new TextView[0];
		if (!hasClicked) {
			return textView;
		} else {
			TableRow tableRow = (TableRow) tablelayout.getChildAt(rowPreviousClicked);
			TextView textViewTableRow = (TextView) tableRow.getChildAt(0);
			for (int i = 0; i < data.size(); i++) {
				TextView textview = (TextView) data.get(i)[1];
				if (textview.getText().toString().equals(textViewTableRow.getText().toString())) {
					textView = data.get(i);
				}
			}
			return textView;
		}
	}

}

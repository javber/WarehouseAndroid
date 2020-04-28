/**
 * 
 */
package com.androidJavi;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.ViewGroup;

/**
 * @author J.Bermudez
 *
 */
public class BlurImageBackGround {

	private static final float BITMAP_SCALE = 0.4f;
	private static final float BLUR_RADIUS = 7.5f;

	public static void setBackGroundImage(ViewGroup layout, Activity activity, int idImage) {
		Bitmap originalBitmap = BitmapFactory.decodeResource(activity.getResources(), idImage);
		Bitmap blurredBitmap = blur(activity.getBaseContext(), originalBitmap);
		layout.setBackground(new BitmapDrawable(activity.getResources(), blurredBitmap));
	}

	private static Bitmap blur(Context context, Bitmap image) {
		int width = Math.round(image.getWidth() * BITMAP_SCALE);
		int height = Math.round(image.getHeight() * BITMAP_SCALE);

		Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
		Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

		RenderScript rs = RenderScript.create(context);
		ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
		Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
		theIntrinsic.setRadius(BLUR_RADIUS);
		theIntrinsic.setInput(tmpIn);
		theIntrinsic.forEach(tmpOut);
		tmpOut.copyTo(outputBitmap);

		return outputBitmap;
	}

}

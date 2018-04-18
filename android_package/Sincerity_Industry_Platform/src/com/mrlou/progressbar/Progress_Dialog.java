package com.mrlou.progressbar;

import android.content.Context;
import android.os.CountDownTimer;

import com.mrlou.mrlou.R;

public class Progress_Dialog {

	private SweetAlertDialog dialog;
	private Context mContext;

	public Progress_Dialog(Context context, SweetAlertDialog mdialog) {
		this.mContext = context;
		this.dialog = mdialog;
	}

	public void dialog() {
		dialog.show();
		dialog.setCancelable(false);
		new CountDownTimer(500 * 100, 500) {
			public void onTick(long millisUntilFinished) {
				// you can change the progress bar color by
				for (int i = 0; i < 100; i++) {
					switch (i % 7) {
					case 0:
						dialog.getProgressHelper().setBarColor(
								mContext.getResources().getColor(
										R.color.blue_btn_bg_color));
						break;
					case 1:
						dialog.getProgressHelper().setBarColor(
								mContext.getResources().getColor(
										R.color.material_deep_teal_50));
						break;
					case 2:
						dialog.getProgressHelper().setBarColor(
								mContext.getResources().getColor(
										R.color.success_stroke_color));
						break;
					case 3:
						dialog.getProgressHelper().setBarColor(
								mContext.getResources().getColor(
										R.color.material_deep_teal_20));
						break;
					case 4:
						dialog.getProgressHelper().setBarColor(
								mContext.getResources().getColor(
										R.color.material_blue_grey_80));
						break;
					case 5:
						dialog.getProgressHelper().setBarColor(
								mContext.getResources().getColor(
										R.color.warning_stroke_color));
						break;
					case 6:
						dialog.getProgressHelper().setBarColor(
								mContext.getResources().getColor(
										R.color.success_stroke_color));
						break;
					}
				}

			}

			public void onFinish() {
				dialog.dismiss();
			}
		}.start();
	}

	public void dismiss() {
		dialog.dismiss();
	}
}

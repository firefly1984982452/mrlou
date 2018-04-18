package com.mrlou.progressbar;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrlou.mrlou.R;

public class SweetAlertDialog extends Dialog implements View.OnClickListener {
	private View mDialogView;
	private AnimationSet mModalInAnim;
	private AnimationSet mModalOutAnim;
	private Animation mOverlayOutAnim;
	private AnimationSet mErrorXInAnim;
	private TextView mTitleTextView;
	private String mTitleText;
	private String mContentText;
	private boolean mShowCancel;
	private boolean mShowContent;
	private String mCancelText;
	private String mConfirmText;
	private int mAlertType;
	private FrameLayout mProgressFrame;
	private Drawable mCustomImgDrawable;
	private ProgressHelper mProgressHelper;
	private boolean mCloseFromCancel;

	public static final int NORMAL_TYPE = 0;
	public static final int ERROR_TYPE = 1;
	public static final int SUCCESS_TYPE = 2;
	public static final int WARNING_TYPE = 3;
	public static final int CUSTOM_IMAGE_TYPE = 4;
	public static final int PROGRESS_TYPE = 5;

	public static interface OnSweetClickListener {
		public void onClick(SweetAlertDialog sweetAlertDialog);
	}

	public SweetAlertDialog(Context context) {
		this(context, NORMAL_TYPE);
	}

	public SweetAlertDialog(Context context, int alertType) {
		super(context, R.style.alert_dialog);
		setCancelable(true);
		setCanceledOnTouchOutside(false);
		mProgressHelper = new ProgressHelper(context);
		mAlertType = alertType;
		mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.error_x_in);
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			List<Animation> childAnims = mErrorXInAnim.getAnimations();
			int idx = 0;
			for (; idx < childAnims.size(); idx++) {
				if (childAnims.get(idx) instanceof AlphaAnimation) {
					break;
				}
			}
			if (idx < childAnims.size()) {
				childAnims.remove(idx);
			}
		}
		mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.modal_in);
		mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.modal_out);
		mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mDialogView.setVisibility(View.GONE);
				mDialogView.post(new Runnable() {
					@Override
					public void run() {
						if (mCloseFromCancel) {
							SweetAlertDialog.super.cancel();
						} else {
							SweetAlertDialog.super.dismiss();
						}
					}
				});
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		// dialog overlay fade out
		mOverlayOutAnim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				WindowManager.LayoutParams wlp = getWindow().getAttributes();
				wlp.alpha = 1 - interpolatedTime;
				getWindow().setAttributes(wlp);
			}
		};
		mOverlayOutAnim.setDuration(120);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);

		mDialogView = getWindow().getDecorView().findViewById(
				android.R.id.content);
		mTitleTextView = (TextView) findViewById(R.id.title_text);
		mProgressFrame = (FrameLayout) findViewById(R.id.progress_dialog);
		mProgressHelper
				.setProgressWheel((ProgressWheel) findViewById(R.id.progressWheel));
		setTitleText(mTitleText);

	}

	public int getAlerType() {
		return mAlertType;
	}

	public String getTitleText() {
		return mTitleText;
	}

	public SweetAlertDialog setTitleText(String text) {
		mTitleText = text;
		if (mTitleTextView != null && mTitleText != null) {
			mTitleTextView.setText(mTitleText);
		}
		return this;
	}

	public String getContentText() {
		return mContentText;
	}

	public boolean isShowCancelButton() {
		return mShowCancel;
	}

	public boolean isShowContentText() {
		return mShowContent;
	}

	public String getCancelText() {
		return mCancelText;
	}

	public String getConfirmText() {
		return mConfirmText;
	}

	protected void onStart() {
		mDialogView.startAnimation(mModalInAnim);
	}

	/**
	 * The real Dialog.cancel() will be invoked async-ly after the animation
	 * finishes.
	 */
	@Override
	public void cancel() {
		dismissWithAnimation(true);
	}

	/**
	 * The real Dialog.dismiss() will be invoked async-ly after the animation
	 * finishes.
	 */
	public void dismissWithAnimation() {
		dismissWithAnimation(false);
	}

	private void dismissWithAnimation(boolean fromCancel) {
		mCloseFromCancel = fromCancel;
		mDialogView.startAnimation(mModalOutAnim);
	}

	@Override
	public void onClick(View v) {
		// if (v.getId() == R.id.cancel_button) {
		// if (mCancelClickListener != null) {
		// mCancelClickListener.onClick(SweetAlertDialog.this);
		// } else {
		// dismissWithAnimation();
		// }
		// } else if (v.getId() == R.id.confirm_button) {
		// if (mConfirmClickListener != null) {
		// mConfirmClickListener.onClick(SweetAlertDialog.this);
		// } else {
		// dismissWithAnimation();
		// }
		// }
	}

	public ProgressHelper getProgressHelper() {
		return mProgressHelper;
	}
}
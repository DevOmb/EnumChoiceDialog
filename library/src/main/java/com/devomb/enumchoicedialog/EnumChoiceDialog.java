package com.devomb.enumchoicedialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Ombrax on 3/09/2015.
 */
public class EnumChoiceDialog<T extends Enum<T>> extends Dialog {

    //region declaration
    //region view
    private RelativeLayout dialogLayout;
    private TextView titleTextView;
    private RadioGroup radioGroup;
    private TextView acceptButton;
    //endregion

    //region variable
    private String mTitle;
    private boolean mTitleBold;
    private int mTitleColor = -1;
    private int mAccentColor = -1;
    private int mOptionColor = -1;
    private int mBackgroundResource;
    private T[] mOptions;
    private int mSelectedIndex;
    private OnOptionSelectListener<T> mOnOptionSelectListener;
    private OnAcceptListener mOnAcceptListener;
    //endregion

    //region inner field
    private Drawable mRadioButtonDrawable;
    private Drawable mCheckedRadioButtonDrawable;
    //endregion
    //endregion

    //region constructor
    public EnumChoiceDialog(Context context) {
        super(context);

    }
    //endregion

    //region create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        init();
    }
    //endregion

    //region setup
    private void init() {
        setContentView(R.layout.dialog);

        dialogLayout = (RelativeLayout) findViewById(R.id.dialog_layout);
        titleTextView = (TextView) findViewById(R.id.dialog_title);
        radioGroup = (RadioGroup) findViewById(R.id.dialog_options);
        acceptButton = (TextView) findViewById(R.id.accept_button);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(R.color.transparent));
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAcceptListener != null) {
                    mOnAcceptListener.onAccept(mOptions[mSelectedIndex]);
                }
                dismiss();
            }
        });

        setAttributes();
    }
    //endregion

    //region builder
    public EnumChoiceDialog title(String title) {
        return title(title, false);
    }

    public EnumChoiceDialog title(String title, boolean bold) {
        if (title != null && !title.isEmpty()) {
            mTitle = title;
            mTitleBold = bold;
            if (titleTextView != null) {
                titleTextView.setText(mTitle);
                if (mTitleBold) {
                    titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);
                }
            }
        }
        return this;
    }

    public EnumChoiceDialog titleColor(int resId) {
        if (resId != 0) {
            mTitleColor = getContext().getResources().getColor(resId);
            if (titleTextView != null) {
                titleTextView.setTextColor(mTitleColor);
            }
        }
        return this;
    }

    public EnumChoiceDialog accentColor(int resId) {
        if (resId != 0) {
            mAccentColor = getContext().getResources().getColor(resId);
            if (acceptButton != null) {
                acceptButton.setTextColor(mAccentColor);
            }
        }
        return this;
    }

    public EnumChoiceDialog optionColor(int resId) {
        if (resId != 0) {
            mOptionColor = getContext().getResources().getColor(resId);
        }
        return this;
    }

    public EnumChoiceDialog background(int resId) {
        if (resId != 0) {
            mBackgroundResource = resId;
            if (dialogLayout != null) {
                dialogLayout.setBackgroundResource(mBackgroundResource);
            }
        }
        return this;
    }

    public EnumChoiceDialog options(Class<T> enumClass) {
        return options(enumClass, 0);
    }

    public EnumChoiceDialog options(Class<T> enumClass, int selectedIndex) {
        mOptions = enumClass.getEnumConstants();
        if (mOptions.length > selectedIndex) {
            mSelectedIndex = selectedIndex;
        }
        if (radioGroup != null) {
            populate();
        }
        return this;
    }

    public EnumChoiceDialog onOptionSelect(OnOptionSelectListener<T> onOptionSelectListener) {
        mOnOptionSelectListener = onOptionSelectListener;
        return this;
    }

    public EnumChoiceDialog onAccept(OnAcceptListener onAcceptListener) {
        mOnAcceptListener = onAcceptListener;
        return this;
    }
    //endregion

    //region helper
    private void setAttributes() {
        getDefaultValues();
        background(mBackgroundResource);
        title(mTitle, mTitleBold);
        titleTextView.setTextColor(mTitleColor);
        acceptButton.setTextColor(mAccentColor);
        if (mOptions != null && mOptions.length > 0) {
            populate();
        } else {
            throw new IllegalArgumentException("No options were provide by the Enum Class");
        }
    }

    private void getDefaultValues() {
        if(mTitleColor == -1){
            mTitleColor = Color.BLACK;
        }
        if(mAccentColor == -1){
            mAccentColor = Color.BLUE;
        }
        if(mOptionColor == -1){
            mOptionColor = Color.BLACK;
        }

        mRadioButtonDrawable = getContext().getResources().getDrawable(R.drawable.radio_button);
        mRadioButtonDrawable.setColorFilter(mOptionColor, PorterDuff.Mode.MULTIPLY);
        mCheckedRadioButtonDrawable = getContext().getResources().getDrawable(R.drawable.radio_button_checked);
        mCheckedRadioButtonDrawable.setColorFilter(mAccentColor, PorterDuff.Mode.MULTIPLY);
    }

    private void populate() {
        for (T option : mOptions) {
            addNewOption(option);
        }
        ((CompoundButton) radioGroup.getChildAt(mSelectedIndex)).setChecked(true);
    }

    private void addNewOption(T option) {
        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        radioButton.setAllCaps(true);
        radioButton.setPadding(5, 15, 0, 15);
        radioButton.setText(option.name());
        radioButton.setTextColor(mOptionColor);
        radioButton.setButtonDrawable(mRadioButtonDrawable);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSelectedIndex = radioGroup.indexOfChild(buttonView);
                    if (mOnOptionSelectListener != null) {
                        mOnOptionSelectListener.onOptionSelect(mOptions[mSelectedIndex]);
                    }
                }
                setSelected(buttonView, isChecked);
            }
        });
        radioGroup.addView(radioButton);
    }

    private void setSelected(View view, boolean selected) {
        if (view instanceof CompoundButton) {
            CompoundButton compoundButton = (CompoundButton) view;
            compoundButton.setTypeface(selected ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            compoundButton.setTextColor(selected ? mAccentColor : mOptionColor);
            compoundButton.setButtonDrawable(selected ? mCheckedRadioButtonDrawable : mRadioButtonDrawable);
        }
    }
    //endregion

    //region interface
    public interface OnAcceptListener<T extends Enum<T>> {
        void onAccept(T selectedEnum);
    }

    public interface OnOptionSelectListener<T extends Enum<T>> {
        void onOptionSelect(T selectedEnum);
    }
    //endregion
}

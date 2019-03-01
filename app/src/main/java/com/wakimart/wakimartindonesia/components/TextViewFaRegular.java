package com.wakimart.wakimartindonesia.components;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.wakimart.wakimartindonesia.utils.FontManager;

public class TextViewFaRegular extends AppCompatTextView {

    public TextViewFaRegular(Context context) {
        super(context);
        setDefaultTypeFace(context);
    }

    public TextViewFaRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultTypeFace(context);
    }

    public TextViewFaRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDefaultTypeFace(context);
    }


    private void setDefaultTypeFace(Context context) {
        this.setTypeface(FontManager.getTypeFace(context, FontManager.fontAwesomeRegular));
    }
}

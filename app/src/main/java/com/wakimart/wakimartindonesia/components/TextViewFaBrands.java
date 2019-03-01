package com.wakimart.wakimartindonesia.components;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.wakimart.wakimartindonesia.utils.FontManager;

public class TextViewFaBrands extends AppCompatTextView {

    public TextViewFaBrands(Context context) {
        super(context);
        setDefaultTypeFace(context);
    }

    public TextViewFaBrands(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultTypeFace(context);
    }

    public TextViewFaBrands(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDefaultTypeFace(context);
    }


    private void setDefaultTypeFace(Context context) {
        this.setTypeface(FontManager.getTypeFace(context, FontManager.fontAwesomeBrands));
    }
}

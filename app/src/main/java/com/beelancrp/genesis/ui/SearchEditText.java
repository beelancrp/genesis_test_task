package com.beelancrp.genesis.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.beelancrp.genesis.R;

/**
 * Created by beeLAN on 03.12.2016.
 */

public class SearchEditText extends RelativeLayout {

    private Context mContext;
    private View rootView;
    private AppCompatEditText editText;
    private AppCompatImageView button, cancel;

    public SearchEditText(Context context) {
        super(context);
        init(context, null);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        rootView = inflate(context, R.layout.search_edit_text, this);
        if (attrs != null) {
            rootView.setLayoutParams(new LayoutParams(context, attrs));
        }
        editText = (AppCompatEditText) rootView.findViewById(R.id.sed_edit_text);
        button = (AppCompatImageView) rootView.findViewById(R.id.sed_button);
        cancel = (AppCompatImageView) rootView.findViewById(R.id.sed_button_cancel);
    }

    public String getText() {
        return String.valueOf(editText.getText());
    }

    public void setOnSearchButtonClickListener(OnClickListener listener) {
        button.setOnClickListener(listener);
    }

    public void setOnCancelButtonClickListener(OnClickListener listener) {
        cancel.setOnClickListener(listener);
    }

    public void showCancelButton() {
        cancel.setVisibility(VISIBLE);
    }

    public void hideCancelButton() {
        cancel.setVisibility(GONE);
    }

    public void showSearchButton() {
        button.setVisibility(VISIBLE);
    }

    public void hideSearchButton() {
        button.setVisibility(GONE);
    }
}

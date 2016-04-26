package com.chenshujun.factorynew.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.chenshujun.factorynew.R;


/**
 * @注释：输入框右边的清除按钮
 */
public class ClearableEditText extends AppCompatEditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearTextIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;
    Context context;

    public ClearableEditText(Context context) {
        super(context);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context pContext){
        context = pContext;
        //封装drawable对象,系统默认图标：R.drawable.abc_ic_clear_mtrl_alpha
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.delete);
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        //简单着色
        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());

        mClearTextIcon = wrappedDrawable;
        //设置宽高
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicHeight(), mClearTextIcon.getIntrinsicWidth());

        setClearIconVisible(false);

        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
        setClearIconVisible(true);
    }

    private void setClearIconVisible(final boolean visible) {
        mClearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        //在edittext右侧设置图标
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? mClearTextIcon : null,
                compoundDrawables[3]);
    }

    //设置警告标志可见
    public void setWarnIconVisible(){
        Drawable warnDrawable = ContextCompat.getDrawable(context, R.drawable.warn);
        Drawable wrapWarnDrawable = DrawableCompat.wrap(warnDrawable);
        warnDrawable = wrapWarnDrawable;
        int f = context.getResources().getDimensionPixelSize(R.dimen.hint_pic);
        warnDrawable.setBounds(0, 0, f, f);

        final Drawable[] compoundDrawables = getCompoundDrawables();
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicHeight(), mClearTextIcon.getIntrinsicWidth());

        //在edittext右侧设置图标
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                warnDrawable,
                compoundDrawables[3]);

    }
    //设置警告标志可见
    public void setSmallWarnIconVisible(){
        Drawable warnDrawable = ContextCompat.getDrawable(context, R.drawable.warn);
        Drawable wrapWarnDrawable = DrawableCompat.wrap(warnDrawable);
        warnDrawable = wrapWarnDrawable;
        int f = context.getResources().getDimensionPixelSize(R.dimen.hint_pic_small);
        warnDrawable.setBounds(0, 0, f, f);

        final Drawable[] compoundDrawables = getCompoundDrawables(); mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicHeight(), mClearTextIcon.getIntrinsicWidth());

        //在edittext右侧设置图标
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                warnDrawable,
                compoundDrawables[3]);

    }
    private void dismissOkIcon(final boolean visible){
        mClearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        //在edittext右侧设置图标
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? mClearTextIcon : null,
                compoundDrawables[3]);
    }
    //设置成功标志可见
    public void setOkIconVisible(){
        Drawable warnDrawable = ContextCompat.getDrawable(context, R.drawable.ok);
        Drawable wrapWarnDrawable = DrawableCompat.wrap(warnDrawable);
        warnDrawable = wrapWarnDrawable;
        warnDrawable.setBounds(0, 0, 48, 48);

        final Drawable[] compoundDrawables = getCompoundDrawables();
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicHeight(), mClearTextIcon.getIntrinsicWidth());


        //在edittext右侧设置图标
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                warnDrawable,
                compoundDrawables[3]);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        }
//        else {
//            setClearIconVisible(false);
//        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }


    /**
     * 输入框获取焦点*/
    private void editTextGetFocus(){
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getX();
        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText("");
                //EditText本身获得焦点
                editTextGetFocus();
                //外界btn使EditText自动获取焦点
                if(etFocus!=null){
                    etFocus.setFoucus();
                }
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(v, event);
    }

    EditTextFocus etFocus;
    public interface EditTextFocus{
        void setFoucus();
    }

    public void setEtFocus(EditTextFocus etFocus) {
        this.etFocus = etFocus;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    @Override
    public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        if (isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }
}
                                                                                                                                                                                                                                                                                                                                                                               
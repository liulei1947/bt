package com.lidynast.customdialog.dialog;


import  com.lidynast.customdialog.dialog.effects.BaseEffects;
import com.liulei1947.bt.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * Created by lee on 2014/7/30.
 */
public class ShowPicNiftyDialogBuilder extends Dialog implements DialogInterface {

    private final String defTextColor="#FFFFFFFF";

    private final String defDividerColor="#11000000";

    private final String defMsgColor="#FFFFFFFF";

    private final String defDialogColor="#FFE74C3C";



    private Effectstype type=null;

    private LinearLayout mLinearLayoutView;

    private FrameLayout mRelativeLayoutView;

    private LinearLayout mFrameLayoutCustomView;

    private View mDialogView;

    private View mDivider;

    private TextView mTitle;

    private TextView mMessage;

    private ImageView mIcon;

    private Button mButton1;

    private Button mButton2;

    private int mDuration = -1;

    private static  int mOrientation=1;

    private boolean isCancelable=true;

    private volatile static ShowPicNiftyDialogBuilder instance;

    public ShowPicNiftyDialogBuilder(Context context) {
        super(context);
        init(context);

    }
    public ShowPicNiftyDialogBuilder(Context context,int theme) {
        super(context, theme);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width  = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }

    public static ShowPicNiftyDialogBuilder getInstance(Context context) {

//        int ort=context.getResources().getConfiguration().orientation;
//        if (mOrientation!=ort){
//            mOrientation=ort;
//            instance=null;
//        }

//        if (instance == null||((Activity) context).isFinishing()) {
//            synchronized (NiftyDialogBuilder.class) {
//                if (instance == null) {
//                    instance = new NiftyDialogBuilder(context,R.style.dialog_untran);
//                }
//            }
//        }
        instance = new ShowPicNiftyDialogBuilder(context,R.style.dialog_untran);
        return instance;

    }

    private void init(Context context) {


        mDialogView = View.inflate(context, R.layout.show_pic_dialog_layout, null);

        mRelativeLayoutView=(FrameLayout)mDialogView.findViewById(R.id.main);
        mFrameLayoutCustomView=(LinearLayout)mDialogView.findViewById(R.id.customPanel);
        mButton1=(Button)mDialogView.findViewById(R.id.button1);
        mButton2=(Button)mDialogView.findViewById(R.id.button2);

        setContentView(mDialogView);

        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

              
                if(type==null){
                    type=Effectstype.Slidetop;
                }
                start(type);


            }
        });
        mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCancelable)dismiss();
            }
        });
    }

    public void toDefault(){
        mTitle.setTextColor(Color.parseColor(defTextColor));
        mDivider.setBackgroundColor(Color.parseColor(defDividerColor));
        mMessage.setTextColor(Color.parseColor(defMsgColor));
        mLinearLayoutView.setBackgroundColor(Color.parseColor(defDialogColor));
    }

    public ShowPicNiftyDialogBuilder withDividerColor(String colorString) {
        mDivider.setBackgroundColor(Color.parseColor(colorString));
        return this;
    }


    

    public ShowPicNiftyDialogBuilder withTitleColor(String colorString) {
        mTitle.setTextColor(Color.parseColor(colorString));
        return this;
    }

   

   
    public ShowPicNiftyDialogBuilder withMessageColor(String colorString) {
        mMessage.setTextColor(Color.parseColor(colorString));
        return this;
    }

    public ShowPicNiftyDialogBuilder withIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }

    public ShowPicNiftyDialogBuilder withIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
        return this;
    }

    public ShowPicNiftyDialogBuilder withDuration(int duration) {
        this.mDuration=duration;
        return this;
    }

    public ShowPicNiftyDialogBuilder withEffect(Effectstype type) {
        this.type=type;
        return this;
    }
    
    public ShowPicNiftyDialogBuilder withButtonDrawable(int resid) {
        mButton1.setBackgroundResource(resid);
        mButton2.setBackgroundResource(resid);
        return this;
    }
    public ShowPicNiftyDialogBuilder withButton1Text(CharSequence text) {
        mButton1.setVisibility(View.VISIBLE);
        mButton1.setText(text);

        return this;
    }
    public ShowPicNiftyDialogBuilder withButton2Text(CharSequence text) {
        mButton2.setVisibility(View.VISIBLE);
        mButton2.setText(text);
        return this;
    }
    public ShowPicNiftyDialogBuilder setButton1Click(View.OnClickListener click) {
        mButton1.setOnClickListener(click);
        return this;
    }

    public ShowPicNiftyDialogBuilder setButton2Click(View.OnClickListener click) {
        mButton2.setOnClickListener(click);
        return this;
    }


    public ShowPicNiftyDialogBuilder setCustomView(int resId, Context context) {
        View customView = View.inflate(context, resId, null);
        if (mFrameLayoutCustomView.getChildCount()>0){
            mFrameLayoutCustomView.removeAllViews();
        }
        mFrameLayoutCustomView.addView(customView);
        return this;
    }

    public ShowPicNiftyDialogBuilder setCustomView(View view, Context context) {
        if (mFrameLayoutCustomView.getChildCount()>0){
            mFrameLayoutCustomView.removeAllViews();
        }
        mFrameLayoutCustomView.addView(view);

        return this;
    }
    public ShowPicNiftyDialogBuilder isCancelableOnTouchOutside(boolean cancelable) {
        this.isCancelable=cancelable;
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }

    public ShowPicNiftyDialogBuilder isCancelable(boolean cancelable) {
        this.isCancelable=cancelable;
        this.setCancelable(cancelable);
        return this;
    }

    private void toggleView(View view,Object obj){
        if (obj==null){
            view.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void show() {

        super.show();
    }

    private void start(Effectstype type){
       BaseEffects animator = type.getAnimator();
        if(mDuration != -1){
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(mRelativeLayoutView);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mButton1.setVisibility(View.GONE);
        mButton2.setVisibility(View.GONE);
    }
}

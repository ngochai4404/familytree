package com.hai.familytree.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hai.familytree.R;
import com.hai.familytree.model.Member;
import com.hai.familytree.util.Config;

/**
 * Created by Hai on 24/07/2018.
 */

public class MemberView extends LinearLayout {
    int distance = Config.distance/2;
    Member mMember;
    Context mContext;
    public MemberView(Context context, Member member) {
        super(context);
        this.mContext = context;
        this.mMember = member;
        setOrientation(VERTICAL);
        initView();
    }
    void initView(){
        ImageView img = new ImageView(mContext);
        img.setLayoutParams(new LayoutParams(distance,distance));
        try {
            img.setBackgroundResource(mMember.getIcon());
        }catch (Exception e){

        }
        TextView tv = new TextView(mContext);
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setText(mMember.getName());
        tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.addView(img);
        this.addView(tv);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(distance, distance);
    }
}

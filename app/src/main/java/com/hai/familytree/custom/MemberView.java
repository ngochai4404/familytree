package com.hai.familytree.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hai.familytree.R;
import com.hai.familytree.interfaces.RefreshAction;
import com.hai.familytree.model.Member;
import com.hai.familytree.util.Config;
import com.hai.familytree.util.UtilDialog;

/**
 * Created by Hai on 24/07/2018.
 */

public class MemberView extends LinearLayout {
    int distance = Config.distance/2;
    Member mMember;
    Context mContext;
    int icon[]= {
        R.drawable.ic_member_1,
        R.drawable.ic_member_2,
        R.drawable.ic_member_3,
        R.drawable.ic_member_4,
        R.drawable.ic_member_5,
        R.drawable.ic_member_6,
        R.drawable.ic_member_7,
        R.drawable.ic_member_8,
        R.drawable.ic_member_9,
        R.drawable.ic_member_10,
        R.drawable.ic_member_11,
        R.drawable.ic_member_12,
        R.drawable.ic_member_13,
        R.drawable.ic_member_14,
        R.drawable.ic_member_15,
        R.drawable.ic_member_16,
        R.drawable.ic_member_17
    };
    RefreshAction rfAction;
    public MemberView(Context context, Member member, RefreshAction refreshAction) {
        super(context);
        this.mContext = context;
        this.mMember = member;
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        this.rfAction = refreshAction;
        initView();
    }
    void initView(){
        ImageView img = new ImageView(mContext);
        img.setLayoutParams(new LayoutParams(distance,distance));
        try {
            img.setBackgroundResource(icon[mMember.getIcon()]);
        }catch (Exception e){
            img.setBackgroundResource(R.drawable.ic_member_1);
        }
        this.addView(img);

        LinearLayout horizontal = new LinearLayout(mContext);
        horizontal.setLayoutParams(new LayoutParams(distance*2,distance));
        horizontal.setOrientation(HORIZONTAL);
        ImageButton btnAdd = new ImageButton(mContext);
        ImageButton btnDel = new ImageButton(mContext);
        TextView tv = new TextView(mContext);
        btnAdd.setLayoutParams(new LayoutParams(distance/2,distance/2));
        btnDel.setLayoutParams(new LayoutParams(distance/2,distance/2));
        btnAdd.setBackgroundResource(R.drawable.ic_add);
        btnDel.setBackgroundResource(R.drawable.ic_sub);
        tv.setLayoutParams(new LayoutParams(distance, LayoutParams.WRAP_CONTENT));
        tv.setText(mMember.getName());
        tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        horizontal.addView(btnDel);
        horizontal.addView(tv);
        horizontal.addView(btnAdd);
        this.addView(horizontal);

        //
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilDialog.showDialogAdd(mContext,mMember,rfAction);
            }
        });
        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilDialog.showDialogChangeInfo(mContext,mMember,rfAction);
            }
        });
        btnDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilDialog.showDialogDelete(mContext,mMember,rfAction);
            }
        });
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(distance*2, distance);
    }
}

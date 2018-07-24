package com.hai.familytree.util;

import android.graphics.Point;
import android.util.Log;

import com.hai.familytree.model.Box;
import com.hai.familytree.model.Member;

import java.util.List;

/**
 * Created by Hai on 24/07/2018.
 */

public class UtilCaculator {
    List<Member> members;
    int mX, mY;
    public Point getXY(List<Member> members){
        this.members = members;
        if(members.size()>0){
            Box box = new Box.BoxBuilder()
                    .setDirection(true,true,true,true)
                    .setPostition(0,0)
                    .build();
            calculator(members.get(0),box,true);
        }
        return new Point(mX,-mY);
    }
    Box calculator(Member current, Box box, boolean checkCouple){
        int countBottomLeft=0;
        int countBottomRight=0;
        int countTopLeft=0;
        int countTopRight=0;
        int countTop =0;
        int countCouple=1;
        mY = Math.min(mY,box.getHeight());
        if(box.istopLeft()) {
            if(current.getMotherId()>0){
                Box temp = new Box.BoxBuilder()
                        .setDirection(true,true,true,false)
                        .setPostition(box.getWidth()-1,box.getHeight()-1)
                        .build();
                countTopLeft=calculator(findMemberId(current.getMotherId()),temp,false).getWidth();
            }
        }

        if( box.istopRight()) {
            if(current.getFatherId()>0){
                Box temp = new Box.BoxBuilder()
                        .setDirection(true,true,true,false)
                        .setPostition(box.getWidth()+1,box.getHeight()-1)
                        .build();
                countTopRight=calculator(findMemberId(current.getFatherId()),temp,false).getHeight();
            }
        }

        if( box.isbottomRight()) {
            for(Member m:members){
                if ( (m.getFatherId()>0 && m.getFatherId() == current.getId())
                        || (m.getMotherId()>0 && m.getMotherId() == current.getId()) ){
                    Box temp = new Box.BoxBuilder()
                            .setDirection(false,false,false,true)
                            .setPostition(box.getWidth()+countBottomRight,box.getHeight()+1)
                            .build();
                    Box newBox = calculator(m,temp,true);
                    countBottomRight += newBox.getWidth();

                }
            }

        }
        if(checkCouple && current.getCoupleId()>0){
            countCouple=3;
        }
        if( box.isbottomLeft() ){
            for(Member m:members){
                if(m.getId()==current.getId())
                    continue;
                if ( (m.getFatherId()>0 && m.getFatherId() == current.getFatherId())
                        || (m.getMotherId()>0 && m.getMotherId() == current.getMotherId()) ){
                    Box temp = new Box.BoxBuilder()
                            .setDirection(false,false,false,true)
                            .setPostition(box.getWidth()-countBottomLeft-1,box.getHeight())
                            .build();
                    Box newBox = calculator(m,temp,true);
                    countBottomLeft+=newBox.getWidth();
                }
            }
        }
        //so sanh TH co vo chong
        countBottomRight=Math.max(countBottomRight,countCouple);
        //TH co ca bo va me
        if(box.istopRight() && box.istopLeft()){
            if(current.getFatherId()!=-1 && current.getMotherId()!=-1){
                countTop = countTopLeft + countBottomRight+1;
            }else{
                countTop = countTopLeft + countBottomRight;
            }
        }

        int maxW=Math.max(countTop,countBottomRight+countBottomLeft);
        box.setPos(Math.max(1,maxW),1);
        Log.d("TreeMember",current.getName()+" "+box.getWidth());
        if(current.getId() == 1){
            Log.d("TreeMember",countBottomLeft+" "+countBottomRight);
        }
        mX =countBottomLeft;
        return box;
//
    }
    Member findMemberId(int id){
        for(Member m :members){
            if(m.getId() == id) {
                return m;
            }
        }
        return null;
    }
}
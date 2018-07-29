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

    /**
     * get X,Y (Me)
     *
     * @param members
     * @return
     */
    public Point getXY(List<Member> members) {
        this.members = members;
        if (members.size() > 0) {
            Box box = new Box.BoxBuilder()
                    .setDirection(true, true, true, true)
                    .setPostition(0, 0)
                    .build();
            calculator(members.get(0), box, true);
        }
        return new Point(mX, -mY);
    }

    /**
     * calculator Member's Box (width, height)
     *
     * @param current
     * @param box
     * @param checkCouple ?drawCouple:noDrawCouple
     * @return
     */
    Box calculator(Member current, Box box, boolean checkCouple) {
        int countBottomLeft = 0;
        int countBottomRight = 0;
        int countTopLeft = 0;
        int countTopRight = 0;
        int countTop = 0;
        int countCouple = 1;
        Member father = null, mother = null;
        // update Y (Me)
        mY = Math.min(mY, box.getHeight());
        //draw mother
        if (box.istopLeft()) {
            if (current.getMotherId() > 0) {
                Box temp = new Box.BoxBuilder()
                        .setDirection(true, true, true, false)
                        .setPostition(box.getWidth() - 1, box.getHeight() - 1)
                        .build();
                mother = findMemberId(current.getMotherId());
                countTopLeft = calculator(mother, temp, false).getWidth();
                current.setCountTopLeft(countTopLeft);
                if (box.istopRight() && current.getFatherId() == -1) {
                    countTopLeft = Math.max(mother.getCountBottomLeft(), mother.getCountTopLeft());
                    current.setCountTopLeft(countTopLeft);
                    current.setCountTopRight(Math.max(mother.getCountBottomRight(), mother.getCountTopRight()));
                }
            }
        }
        //draw father
        if (box.istopRight()) {
            if (current.getFatherId() > 0) {
                Box temp = new Box.BoxBuilder()
                        .setDirection(true, true, true, false)
                        .setPostition(box.getWidth() + 1, box.getHeight() - 1)
                        .build();
                father = findMemberId(current.getFatherId());
                countTopRight = calculator(father, temp, false).getWidth();
                current.setCountTopRight(countTopRight);
                if (box.istopLeft() && current.getMotherId() == -1) {
                    countTopRight = Math.max(father.getCountBottomRight(), father.getCountTopRight());
                    current.setCountTopRight(countTopRight);
                    current.setCountTopLeft(Math.max(father.getCountBottomLeft(), father.getCountTopLeft()));
                }
            }
        }
        //draw children
        if (box.isbottomRight()) {
            for (Member m : members) {
                if ((m.getFatherId() > 0 && m.getFatherId() == current.getId())
                        || (m.getMotherId() > 0 && m.getMotherId() == current.getId())) {
                    Box temp = new Box.BoxBuilder()
                            .setDirection(false, false, false, true)
                            .setPostition(box.getWidth() + countBottomRight, box.getHeight() + 1)
                            .build();
                    Box newBox = calculator(m, temp, true);
                    countBottomRight += newBox.getWidth();
                }
            }
        }
        //draw brother, sister
        if (box.isbottomLeft()) {
            for (Member m : members) {
                if (m.getId() == current.getId())
                    continue;
                if ((m.getFatherId() > 0 && m.getFatherId() == current.getFatherId())
                        || (m.getMotherId() > 0 && m.getMotherId() == current.getMotherId())) {
                    Box temp = new Box.BoxBuilder()
                            .setDirection(false, false, false, true)
                            .setPostition(box.getWidth() - countBottomLeft - 1, box.getHeight())
                            .build();
                    Box newBox = calculator(m, temp, true);
                    countBottomLeft += newBox.getWidth();
                }
            }
            current.setCountBottomLeft(countBottomLeft);
        }
        //draw couple
        if (checkCouple && current.getCoupleId() > 0) {
            countTopRight = Math.max(countTopRight, 2);
        }
        int maxLeft = Math.max(countBottomLeft, countTopLeft);
        int maxRight = Math.max(countTopRight, countBottomRight);
        int maxWidth = maxLeft + maxRight + 1;
        box.setPos(Math.max(1, maxWidth), 1);
        current.setWidth(maxWidth);
        Log.d("treeLog6", current.getName() + " " + maxLeft + " " + maxRight + " " + maxWidth);
        // update X (Me)
        mX = maxLeft;
        return box;
//
    }

    Member findMemberId(int id) {
        for (Member m : members) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }
}

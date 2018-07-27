package com.hai.familytree;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.hai.familytree.custom.MemberView;
import com.hai.familytree.db.DatabaseManager;
import com.hai.familytree.db.table.MemberTable;
import com.hai.familytree.interfaces.RefreshAction;
import com.hai.familytree.model.Box;
import com.hai.familytree.model.Member;
import com.hai.familytree.util.Config;
import com.hai.familytree.util.UtilCaculator;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RefreshAction {
    int distance = Config.distance;
    FrameLayout mRoot;
    DatabaseManager databaseManager;
    List<Member> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseManager = new DatabaseManager(this);
        initView();
        initData();
        draw();
    }

    void initView() {
        mRoot = findViewById(R.id.root_view);
    }

    /**
     * get data from db
     */
    public void initData() {
        members = new MemberTable().getAllMembers(databaseManager);
        if (members.size() == 0) {
            new MemberTable().insertMember(new Member("Tôi", 10, 0), databaseManager);
            members = new MemberTable().getAllMembers(databaseManager);
        }
        Log.d("members", members.toString());
    }

    /**
     * remove all view (draw)
     * get X,Y (Me)
     * draw from me
     */
    public void draw() {
        mRoot.removeAllViews();
        Point p = new UtilCaculator().getXY(members);
        mRoot.setLayoutParams(new FrameLayout.LayoutParams(distance * members.size(), distance * members.size()));
        Box box = new Box.BoxBuilder()
                .setDirection(true, true, true, true)
                .setPostition(p.x, p.y)
                .build();
        //calculator draw view
        calculatorDraw(members.get(0), box, true);
        //draw line
       // calculatorLine(members);
    }
    void calculatorLine(List<Member> members){
        for(Member m1: members){
            for(Member m2:members){
                if(m1.getId()==m2.getId()){
                    continue;
                }
                if(m1.getCoupleId() == m2.getCoupleId()){
                    if(m1.getGender()==1){
                        drawLine(m2.getX()*distance,m2.getY()*distance/2,(m1.getX()-m2.getX())*distance,2);
                    }else{
                        drawLine(m1.getX()*distance,m1.getY()*distance/2,(m2.getX()-m1.getX())*distance,2);
                    }
                }
            }
        }
    }

    /**
     * draw View
     *
     * @param member
     * @param x
     * @param y
     */
    void addView(Member member, int x, int y) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(distance, distance);
        params.leftMargin = x * distance;
        params.topMargin = y * distance;
        mRoot.addView(new MemberView(MainActivity.this, member, this), params);
    }

    void drawLine(int x,int y, int width,int height) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(distance, distance);
        params.leftMargin = x ;
        params.topMargin = y;
        params.width = width;
        params.height = height;
        View line = new View(this);
        line.setBackgroundColor(Color.BLACK);
        line.setLayoutParams(params);
        mRoot.addView(line);
    }

    /**
     * get position member and call function draw
     *
     * @param current
     * @param box
     * @param checkCouple
     * @return
     */
    Box calculatorDraw(Member current, Box box, boolean checkCouple) {
        int countBottomLeft = 0;
        int countBottomRight = 0;
        int countTopLeft = 0;
        int countTopRight = 0;
        int countTop = 0;
        int countCouple = 1;
        Member father = null, mother = null;
        // draw mother
        if (box.istopLeft()) {
            if (current.getMotherId() > 0) {
                mother = findMemberId(current.getMotherId());
                int x;
                if (current.getFatherId() == -1) {
                    x = 0;
                } else {
                    x = Math.max(mother.getCountBottomRight(), mother.getCountTopRight()) + 1;
                }
                Box temp = new Box.BoxBuilder()
                        .setDirection(true, true, true, false)
                        .setPostition(box.getWidth() - x, box.getHeight() - 1)
                        .build();
                countTopLeft = calculatorDraw(mother, temp, false).getWidth();
                current.setCountTopLeft(countTopLeft);
                if (box.istopRight() && current.getFatherId() == -1) {
                    countTopLeft = Math.max(mother.getCountBottomLeft(), mother.getCountTopLeft());
                    current.setCountTopLeft(countTopLeft);
                    current.setCountTopRight(Math.max(mother.getCountBottomRight(), mother.getCountTopRight()));
                }
            }
        }
        // draw father
        if (box.istopRight()) {
            if (current.getFatherId() > 0) {
                father = findMemberId(current.getFatherId());
                int x;
                if (current.getMotherId() == -1) {
                    x = 0;
                } else {
                    x = Math.max(father.getCountBottomLeft(), father.getCountTopLeft()) + 1;
                }
                Box temp = new Box.BoxBuilder()
                        .setDirection(true, true, true, false)
                        .setPostition(box.getWidth() + x, box.getHeight() - 1)
                        .build();
                countTopRight = calculatorDraw(father, temp, false).getWidth();
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
                            .setPostition(box.getWidth() + countBottomRight + 1, box.getHeight() + 1)
                            .build();
                    countBottomRight += calculatorDraw(m, temp, true).getWidth();
                }
            }

        }
        //draw couple
        if (checkCouple && current.getCoupleId() > 0) {
            countCouple = 3;
            if (current.getGender() == 1) {
                Box temp = new Box.BoxBuilder()
                        .setDirection(false, false, false, false)
                        .setPostition(box.getWidth() - 2, box.getHeight())
                        .build();
                calculatorDraw(findMemberId(current.getCoupleId()), temp, false);
            } else {
                Box temp = new Box.BoxBuilder()
                        .setDirection(false, false, false, false)
                        .setPostition(box.getWidth() + 2, box.getHeight())
                        .build();
                calculatorDraw(findMemberId(current.getCoupleId()), temp, false);
            }
        }
        //draw brother, sister
        if (box.isbottomLeft()) {
            for (Member m : members) {
                if (m.getId() == current.getId())
                    continue;
                if ((m.getFatherId() > 0 && m.getFatherId() == current.getFatherId())
                        || (m.getMotherId() > 0 && m.getMotherId() == current.getMotherId())) {
                    countBottomLeft += m.getWidth();
                    Box temp = new Box.BoxBuilder()
                            .setDirection(false, false, false, true)
                            .setPostition(box.getWidth() - countBottomLeft, box.getHeight())
                            .build();
                    calculatorDraw(m, temp, true).getWidth();
                }
            }
        }
        //so sanh TH co vo chong
//        countBottomRight = Math.max(countBottomRight, countCouple);
        //TH co ca bo va me
//        if (box.istopRight() && box.istopLeft()) {
//            if (current.getFatherId() != -1 && current.getMotherId() != -1) {
//                countTop = countTopLeft + countTopRight + 1;
//            } else {
//                countTop = countTopLeft + countTopRight;
//            }
//        }
        //call function drawView
        current.setX(box.getWidth());
        current.setY(box.getHeight());
        addView(current, box.getWidth(), box.getHeight());
        //get Max width
        int maxLeft = Math.max(countBottomLeft, countTopLeft);
        int maxRight = Math.max(countTopRight, countBottomRight);
        int maxWidth = maxLeft + maxRight + 1;
        box.setPos(maxWidth, box.getHeight());
        Log.d("treeLog8", current.getName() + " " + maxLeft + " " + maxRight);
        return box;
//
    }

    /**
     * find Member by id
     *
     * @param id
     * @return
     */
    Member findMemberId(int id) {
        for (Member m : members) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * refresh all member (reDraw)
     */
    @Override
    public void refresh() {
        members = new MemberTable().getAllMembers(databaseManager);
        draw();
    }
}

package com.hai.familytree;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        calculatorDraw(members.get(0), box, true);
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
        int countTopLeft = 1;
        int countTopRight = 0;
        int countTop = 0;
        int countCouple = 1;
        // draw mother
        if (box.istopLeft()) {
            if (current.getMotherId() > 0) {
                int x = current.getCountTopLeft() / 2 + 1;
                if (current.getFatherId() < 0) {
                    x = 0;
                }
                Box temp = new Box.BoxBuilder()
                        .setDirection(true, true, true, false)
                        .setPostition(box.getWidth() - x, box.getHeight() - 1)
                        .build();
                countTopLeft = calculatorDraw(findMemberId(current.getMotherId()), temp, false).getWidth();
            }
        }
        // draw father
        if (box.istopRight()) {
            if (current.getFatherId() > 0) {
                int x = current.getCountTopRight() / 2 + 1;
                if (current.getMotherId() < 0 && x == 1) {
                    x = 0;
                }
                Box temp = new Box.BoxBuilder()
                        .setDirection(true, true, true, false)
                        .setPostition(box.getWidth() + x, box.getHeight() - 1)
                        .build();
                countTopRight = calculatorDraw(findMemberId(current.getFatherId()), temp, false).getWidth();
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
                    Box newBox = calculatorDraw(m, temp, true);
                    countBottomRight += newBox.getWidth();
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
                    countBottomLeft += m.getCountBottomRight();
                    Box temp = new Box.BoxBuilder()
                            .setDirection(false, false, false, true)
                            .setPostition(box.getWidth() - countBottomLeft, box.getHeight())
                            .build();
                    Box newBox = calculatorDraw(m, temp, true);
                }
            }
        }
        //so sanh TH co vo chong
        countBottomRight = Math.max(countBottomRight, countCouple);
        //TH co ca bo va me
        if (box.istopRight() && box.istopLeft()) {
            if (current.getFatherId() != -1 && current.getMotherId() != -1) {
                countTop = countTopLeft + countTopRight + 1;
            } else {
                countTop = countTopLeft + countTopRight;
            }
        }
        //call function drawView
        addView(current, box.getWidth(), box.getHeight());
        //get Max width
        int maxW = Math.max(countTop, countBottomRight + countBottomLeft);
        box.setPos(Math.max(1, maxW), box.getHeight());
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

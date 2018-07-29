package com.hai.familytree;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.hai.familytree.model.Box;
import com.hai.familytree.model.Member;
import com.hai.familytree.util.Config;
import com.hai.familytree.util.UtilCaculator;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String ACTION_RENDER = "ACTION_RENDER";
    int distance = Config.distance;
    FrameLayout mRoot;
    DatabaseManager databaseManager;
    List<Member> members;
    BroadcastReceiver receiver;

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
        calculatorLine(members);

    }

    void calculatorLine(List<Member> members) {
        for (Member m1 : members) {
            for (Member m2 : members) {
                if (m1.getId() == m2.getId()) {
                    continue;
                }
                if (m1.getCoupleId() == m2.getId()) {
                    if (m1.getX() < m2.getX()) {
                        int x, y, width, height;
                        x = (m1.getX() + 1) * distance;
                        y = (m1.getY()) * distance + distance / 4;
                        width = (m2.getX() - m1.getX() - 1) * distance;
                        height = 2;
                        drawLine(x, y, width, height);
                    } else {
                        int x, y, width, height;
                        x = (m2.getX() + 1) * distance;
                        y = (m2.getY()) * distance + distance / 4;
                        width = (m1.getX() - m2.getX() - 1) * distance;
                        height = 2;
                        drawLine(x, y, width, height);
                    }
                }
                //draw line brother,sister
                if ((m1.getFatherId() > 0 && m1.getFatherId() == m2.getFatherId())
                        || (m1.getMotherId() > 0 && m1.getMotherId() == m2.getMotherId())) {
                    if (m1.getX() < m2.getX()) {
                        int x, y, width, height;
                        //draw line horizontal
                        x = (m1.getX()) * distance + distance / 2;
                        y = (m1.getY()) * distance - distance / 8;
                        width = (m2.getX() - m1.getX()) * distance;
                        height = 2;
                        drawLine(x, y, width, height);
                        //draw line horizontal vertital
                        x = (m1.getX()) * distance + distance / 2;
                        y = (m1.getY()) * distance - distance / 8;
                        width = 2;
                        height = distance / 8;
                        drawLine(x, y, width, height);
                        x = (m2.getX()) * distance + distance / 2;
                        y = (m2.getY()) * distance - distance / 8;
                        width = 2;
                        height = distance / 8;
                        drawLine(x, y, width, height);

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
        mRoot.addView(new MemberView(MainActivity.this, member), params);
    }

    void drawLine(int x, int y, int width, int height) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.leftMargin = x;
        params.topMargin = y;
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
        //draw couple
        if (checkCouple && current.getCoupleId() > 0) {
            Box temp = new Box.BoxBuilder()
                    .setDirection(false, false, false, false)
                    .setPostition(box.getWidth() + 2, box.getHeight())
                    .build();
            calculatorDraw(findMemberId(current.getCoupleId()), temp, false);
        }
        //draw children
        if (box.isbottomRight()) {
            boolean firstDraw = true;
            for (Member m : members) {
                if ((m.getFatherId() > 0 && m.getFatherId() == current.getId())
                        || (m.getMotherId() > 0 && m.getMotherId() == current.getId())) {
                    Box temp = new Box.BoxBuilder()
                            .setDirection(false, false, false, true)
                            .setPostition(box.getWidth() + countBottomRight + 1, box.getHeight() + 1)
                            .build();
                    calculatorDraw(m, temp, true);
                    countBottomRight += m.getWidth();
                    //draw line (parent)
                    if (firstDraw) {
                        int x = (m.getX()) * distance + distance / 2;
                        int y = (m.getY() - 1) * distance + distance / 2;
                        int width = 2;
                        int height = distance / 2;
                        drawLine(x, y, width, height);
                        firstDraw = false;
                    }
                }
            }

        }
        //draw brother, sister
        if (box.isbottomLeft()) {
            //draw line (parent)
            if (current.getFatherId() > 0 || current.getMotherId() > 0) {
                int x = (box.getWidth()) * distance + distance / 2;
                int y = (box.getHeight() - 1) * distance + distance / 2;
                int width = 2;
                int height = distance / 2;
                drawLine(x, y, width, height);
            }
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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RENDER);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refresh();
            }
        };
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    /**
     * refresh all member (reDraw)
     */
    public void refresh() {
        members = new MemberTable().getAllMembers(databaseManager);
        draw();
    }
}

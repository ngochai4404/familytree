package com.hai.familytree;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    int distance = Config.distance;
    FrameLayout mRoot;
    DatabaseManager databaseManager;
    List<Member> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseManager = new DatabaseManager(this);
        members = new MemberTable().getAllMembers(databaseManager);
        mRoot = findViewById(R.id.root_view);
        draw();
//       initData();
    }

    public void initData() {
        MemberTable table = new MemberTable();
//        table.insertMember(new Member("Ông nội",R.drawable.ic_member_1,1),databaseManager);
        List<Member> memberList = table.getAllMembers(databaseManager);
//        for(Member m:memberList){
//            if(m.getId()==10){
//                m.setFatherId(18);
//                m.setMotherId(19);
////                table.deleteNote(m,databaseManager);
//                table.updateMember(m,databaseManager);
//            }
//
//        }
        Log.d("memberList", memberList.toString());
    }

    public void draw() {
        Point p = new UtilCaculator().getXY(members);
        mRoot.setLayoutParams(new FrameLayout.LayoutParams(distance * members.size(), distance * members.size()));
//        mRoot.addView(new MemberView(this,members.get(0)));
//        addView(members.get(0),p.x*distance,p.y*distance);
        Box box = new Box.BoxBuilder()
                .setDirection(true, true, true, true)
                .setPostition(p.x, p.y)
                .build();
        calculator(members.get(0), box, true);
    }

    void addView(Member member, int x, int y) {
        Log.d("postitionXY", x + " " + y);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(distance, distance);
        params.leftMargin = x * distance;
        params.topMargin = y * distance;
        mRoot.addView(new MemberView(this, member), params);
    }

    Box calculator(Member current, Box box, boolean checkCouple) {
        int countBottomLeft = 0;
        int countBottomRight = 0;
        int countTopLeft = 0;
        int countTopRight = 0;
        int countTop = 0;
        int countCouple = 1;
        if (box.istopLeft()) {
            if (current.getMotherId() > 0) {
                int x = current.getCountTopLeft()/2+1;
                Box temp = new Box.BoxBuilder()
                        .setDirection(true, true, true, false)
                        .setPostition(box.getWidth() - x, box.getHeight() - 1)
                        .build();
                countTopLeft = calculator(findMemberId(current.getMotherId()), temp, false).getWidth();
            }
        }
        if (box.istopRight()) {
            if (current.getFatherId() > 0) {
                int x = current.getCountTopRight()/2+1;
                Box temp = new Box.BoxBuilder()
                        .setDirection(true, true, true, false)
                        .setPostition(box.getWidth() + x, box.getHeight() - 1)
                        .build();
                countTopRight = calculator(findMemberId(current.getFatherId()), temp, false).getHeight();
            }
        }
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
        if (checkCouple && current.getCoupleId() > 0) {
            countCouple = 3;
        }
        if (box.isbottomLeft()) {
            for (Member m : members) {
                if (m.getId() == current.getId())
                    continue;
                if ((m.getFatherId() > 0 && m.getFatherId() == current.getFatherId())
                        || (m.getMotherId() > 0 && m.getMotherId() == current.getMotherId())) {
                    if(countBottomLeft==0){
                        countBottomLeft = m.getCountBottomRight();
                    }
                    Box temp = new Box.BoxBuilder()
                            .setDirection(false, false, false, true)
                            .setPostition(box.getWidth() - countBottomLeft, box.getHeight())
                            .build();
                    Box newBox = calculator(m, temp, true);
                    countBottomLeft += newBox.getWidth()-1;
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
        int maxW = Math.max(countTop, countBottomRight + countBottomLeft);
        addView(current, box.getWidth(), box.getHeight());
        box.setPos(Math.max(1, maxW), 1);
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
//[{"name":"Tôi","coupleId":12,"fatherId":10,"gender":0,"icon":2131099737,"id":1,"motherId":11}, {"name":"Con gái","coupleId":-1,"fatherId":-1,"gender":0,"icon":2131099740,"id":2,"motherId":1}, {"name":"Con gái","coupleId":-1,"fatherId":-1,"gender":0,"icon":2131099740,"id":3,"motherId":1}, {"name":"Con trai","coupleId":-1,"fatherId":-1,"gender":1,"icon":2131099741,"id":4,"motherId":1}, {"name":"Cháu trai","coupleId":-1,"fatherId":-1,"gender":1,"icon":2131099748,"id":5,"motherId":2}, {"name":"Cháu gái","coupleId":-1,"fatherId":-1,"gender":0,"icon":2131099749,"id":6,"motherId":2}, {"name":"Cháu gái","coupleId":-1,"fatherId":-1,"gender":0,"icon":2131099749,"id":7,"motherId":3}, {"name":"Chị gái","coupleId":-1,"fatherId":10,"gender":0,"icon":2131099736,"id":8,"motherId":11}, {"name":"Anh trai","coupleId":15,"fatherId":10,"gender":1,"icon":2131099746,"id":9,"motherId":11}, {"name":"Bố","coupleId":11,"fatherId":18,"gender":1,"icon":2131099744,"id":10,"motherId":19}, {"name":"Mẹ","coupleId":10,"fatherId":20,"gender":0,"icon":2131099745,"id":11,"motherId":21}, {"name":"Chồng","coupleId":1,"fatherId":-1,"gender":1,"icon":2131099742,"id":12,"motherId":-1}, {"name":"Chị dâu","coupleId":9,"fatherId":-1,"gender":0,"icon":2131099745,"id":15,"motherId":-1}, {"name":"Ông nội","coupleId":19,"fatherId":-1,"gender":1,"icon":2131099734,"id":18,"motherId":-1}, {"name":"Bà nội","coupleId":18,"fatherId":-1,"gender":0,"icon":2131099743,"id":19,"motherId":-1}, {"name":"ông ngoại","coupleId":21,"fatherId":-1,"gender":1,"icon":2131099734,"id":20,"motherId":-1}, {"name":"Bà ngoại","coupleId":20,"fatherId":-1,"gender":0,"icon":2131099743,"id":21,"motherId":-1}, {"name":"Cháu gái","coupleId":-1,"fatherId":9,"gender":0,"icon":2131099747,"id":22,"motherId":-1}, {"name":"Cháu gái","coupleId":-1,"fatherId":-1,"gender":0,"icon":2131099747,"id":23,"motherId":8}, {"name":"Cháu trai","coupleId":-1,"fatherId":-1,"gender":1,"icon":2131099748,"id":24,"motherId":8}]
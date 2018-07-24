package com.hai.familytree;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.hai.familytree.db.DatabaseManager;
import com.hai.familytree.db.table.MemberTable;
import com.hai.familytree.model.Member;
import com.hai.familytree.util.UtilCaculator;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    int distance = 150;
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
    public void initData(){
        MemberTable table = new MemberTable();
//        table.insertMember(new Member("Ông nội",R.drawable.ic_member_1,1),databaseManager);
//        table.insertMember(new Member("Bà nội",R.drawable.ic_member_2,0),databaseManager);
//        table.insertMember(new Member("ông ngoại",R.drawable.ic_member_1,1),databaseManager);
//        table.insertMember(new Member("Bà ngoại",R.drawable.ic_member_2,0),databaseManager);
//        table.insertMember(new Member("Cháu gái",R.drawable.ic_member_6,0),databaseManager);
//        table.insertMember(new Member("Cháu gái",R.drawable.ic_member_6,0),databaseManager);
//        table.insertMember(new Member("Cháu trai",R.drawable.ic_member_7,1),databaseManager);
//        table.insertNote(new Member("Cháu gái",R.drawable.ic_member_8,0),databaseManager);
//        table.insertNote(new Member("Cháu gái",R.drawable.ic_member_8,0),databaseManager);

        List<Member> memberList = table.getAllMembers(databaseManager);
        for(Member m:memberList){
            if(m.getId()==10){
                m.setFatherId(18);
                m.setMotherId(19);
//                table.deleteNote(m,databaseManager);
                table.updateMember(m,databaseManager);
            }

        }
        Log.d("memberList",memberList.toString());
    }
    public void draw(){
        Point p = new UtilCaculator().getXY(members);
        mRoot.setLayoutParams(new FrameLayout.LayoutParams(distance*members.size(),distance*members.size()));
    }
}

//[{"name":"Tôi","coupleId":12,"fatherId":10,"gender":0,"icon":2131099737,"id":1,"motherId":11}, {"name":"Con gái","coupleId":-1,"fatherId":-1,"gender":0,"icon":2131099740,"id":2,"motherId":1}, {"name":"Con gái","coupleId":-1,"fatherId":-1,"gender":0,"icon":2131099740,"id":3,"motherId":1}, {"name":"Con trai","coupleId":-1,"fatherId":-1,"gender":1,"icon":2131099741,"id":4,"motherId":1}, {"name":"Cháu trai","coupleId":-1,"fatherId":-1,"gender":1,"icon":2131099748,"id":5,"motherId":2}, {"name":"Cháu gái","coupleId":-1,"fatherId":-1,"gender":0,"icon":2131099749,"id":6,"motherId":2}, {"name":"Cháu gái","coupleId":-1,"fatherId":-1,"gender":0,"icon":2131099749,"id":7,"motherId":3}, {"name":"Chị gái","coupleId":-1,"fatherId":10,"gender":0,"icon":2131099736,"id":8,"motherId":11}, {"name":"Anh trai","coupleId":15,"fatherId":10,"gender":1,"icon":2131099746,"id":9,"motherId":11}, {"name":"Bố","coupleId":11,"fatherId":18,"gender":1,"icon":2131099744,"id":10,"motherId":19}, {"name":"Mẹ","coupleId":10,"fatherId":20,"gender":0,"icon":2131099745,"id":11,"motherId":21}, {"name":"Chồng","coupleId":1,"fatherId":-1,"gender":1,"icon":2131099742,"id":12,"motherId":-1}, {"name":"Chị dâu","coupleId":9,"fatherId":-1,"gender":0,"icon":2131099745,"id":15,"motherId":-1}, {"name":"Ông nội","coupleId":19,"fatherId":-1,"gender":1,"icon":2131099734,"id":18,"motherId":-1}, {"name":"Bà nội","coupleId":18,"fatherId":-1,"gender":0,"icon":2131099743,"id":19,"motherId":-1}, {"name":"ông ngoại","coupleId":21,"fatherId":-1,"gender":1,"icon":2131099734,"id":20,"motherId":-1}, {"name":"Bà ngoại","coupleId":20,"fatherId":-1,"gender":0,"icon":2131099743,"id":21,"motherId":-1}, {"name":"Cháu gái","coupleId":-1,"fatherId":9,"gender":0,"icon":2131099747,"id":22,"motherId":-1}, {"name":"Cháu gái","coupleId":-1,"fatherId":-1,"gender":0,"icon":2131099747,"id":23,"motherId":8}, {"name":"Cháu trai","coupleId":-1,"fatherId":-1,"gender":1,"icon":2131099748,"id":24,"motherId":8}]
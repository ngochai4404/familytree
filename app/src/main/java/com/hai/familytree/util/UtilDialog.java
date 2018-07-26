package com.hai.familytree.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.hai.familytree.R;
import com.hai.familytree.custom.adapter.IconAdapter;
import com.hai.familytree.db.DatabaseManager;
import com.hai.familytree.db.table.MemberTable;
import com.hai.familytree.interfaces.ItemOnClick;
import com.hai.familytree.interfaces.RefreshAction;
import com.hai.familytree.model.Member;

import java.util.List;

/**
 * Created by Hai on 25/07/2018.
 */

public class UtilDialog {
    public static void showDialogAdd(final Context mContext, final Member member, final RefreshAction refreshAction) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add);
        final CheckBox cbFather = dialog.findViewById(R.id.cb_father);
        final CheckBox cbMother = dialog.findViewById(R.id.cb_mother);
        final CheckBox cbCouple = dialog.findViewById(R.id.cb_couple);
        final Spinner spSon = dialog.findViewById(R.id.sp_son);
        final Spinner spDaughter = dialog.findViewById(R.id.sp_daughter);
        final Spinner spBrother = dialog.findViewById(R.id.sp_brother);
        final Spinner spSister = dialog.findViewById(R.id.sp_sister);
        Button btnSave = dialog.findViewById(R.id.btn_save);
        if (member.getMotherId() > 0 || member.getFatherId() > 0) {
            cbMother.setVisibility(View.GONE);
            cbFather.setVisibility(View.GONE);
        }
        if (member.getCoupleId() > 0) {
            cbCouple.setVisibility(View.GONE);
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("memberList", member.toString());
                int son = Integer.parseInt(spSon.getSelectedItem().toString());
                int daughter = Integer.parseInt(spDaughter.getSelectedItem().toString());
                int brother = Integer.parseInt(spBrother.getSelectedItem().toString());
                int sister = Integer.parseInt(spSister.getSelectedItem().toString());
                Log.d("insertDB", son + " " + daughter + " " + brother + " " + sister);
                DatabaseManager db = new DatabaseManager(mContext);
                List<Member> memberList = new MemberTable().getAllMembers(db);
                Member father = null, mother = null;
                if (son > 0) {
                    for (int i = 0; i < son; i++) {
                        Member m = new Member(mContext.getString(R.string.son), 15, 1);
                        if (member.getGender() == 1) {
                            m.setFatherId(member.getId());
                            m.setMotherId(member.getCoupleId());
                        } else {
                            m.setMotherId(member.getId());
                            m.setFatherId(member.getCoupleId());
                        }
                        new MemberTable().insertMember(m, db);
                    }
                }
                if (daughter > 0) {
                    for (int i = 0; i < daughter; i++) {
                        Member m = new Member(mContext.getString(R.string.daughter), 14, 0);
                        if (member.getGender() == 1) {
                            m.setFatherId(member.getId());
                            m.setMotherId(member.getCoupleId());
                        } else {
                            m.setMotherId(member.getId());
                            m.setFatherId(member.getCoupleId());
                        }
                        new MemberTable().insertMember(m, db);
                    }
                }
                if (brother > 0) {
                    // draw brother
                }
                if (sister > 0) {
                    // draw sister
                }
                if (cbFather.isChecked()) {
                    father = new Member("Bố", 2, 1);
                    father.setId(new MemberTable().insertMember(father, db));
                    member.setFatherId(father.getId());
                    new MemberTable().updateMember(member, db);
                }
                if (cbMother.isChecked()) {
                    mother = new Member("Mẹ", 3, 0);
                    mother.setId(new MemberTable().insertMember(mother, db));
                    member.setMotherId(mother.getId());
                    new MemberTable().updateMember(member, db);
                }
                if (father != null && mother != null) {
                    father.setCoupleId(mother.getId());
                    mother.setCoupleId(father.getId());
                    new MemberTable().updateMember(father, db);
                    new MemberTable().updateMember(mother, db);
                }
                refreshAction.refresh();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showDialogChangeInfo(final Context mContext, final Member member, final RefreshAction refreshAction) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_infomation);
        final EditText et = dialog.findViewById(R.id.et_name);
        final ImageView img = dialog.findViewById(R.id.iv_icon);
        final IconAdapter adapter = new IconAdapter(member.getIcon(), new ItemOnClick() {
            @Override
            public void onClick(Object o, int p) {
                int t = (int) o;
                img.setImageResource(t);
            }
        });
        RecyclerView rcv = dialog.findViewById(R.id.rcv_icon);
        Button btnSave = dialog.findViewById(R.id.btn_save);
        et.setText(member.getName());
        rcv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        img.setImageResource(adapter.getIconCurrent(member.getIcon()));
        rcv.setAdapter(adapter);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                member.setName(et.getText() + "");
                member.setIcon(adapter.getPos());
                Log.d("iconMember", member.getIcon() + "");
                DatabaseManager db = new DatabaseManager(mContext);
                new MemberTable().updateMember(member, db);
                refreshAction.refresh();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showDialogDelete(final Context mContext, final Member member, final RefreshAction refreshAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.delete));
        builder.setMessage(member.getName());
        builder.setPositiveButton(mContext.getString(R.string.delete), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                DatabaseManager db = new DatabaseManager(mContext);
                List<Member> members = new MemberTable().getAllMembers(db);
                if (member.getId() != 1) {
                    delete(member, members, db);

                }
                refreshAction.refresh();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    static void delete(Member current, List<Member> members, DatabaseManager db) {
        for (Member m : members) {
            if (m.getId() != 1) {
                if ((m.getFatherId() == current.getId()
                        || m.getMotherId() == current.getId())) {
                    delete(m, members, db);
                }
                if (m.getCoupleId() == current.getId()) {
                    m.setCoupleId(-1);
                    new MemberTable().updateMember(m, db);
                }
            } else {
                if (m.getFatherId() == current.getId()) {
                    m.setFatherId(-1);
                    new MemberTable().updateMember(m, db);
                }
                if (m.getMotherId() == current.getId()) {
                    m.setMotherId(-1);
                    new MemberTable().updateMember(m, db);
                }
            }
        }
        new MemberTable().deleteNote(current, db);

    }
}

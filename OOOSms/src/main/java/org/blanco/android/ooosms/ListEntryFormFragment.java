package org.blanco.android.ooosms;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import org.blanco.android.ooosms.db.Entry;
import org.blanco.android.ooosms.db.OOOSMSDbHelper;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import roboguice.fragment.RoboFragment;

import static android.app.Activity.RESULT_OK;
import static org.blanco.android.ooosms.MainActivity.OOTAG;

/**
 * Created by Alexandro Blanco <ti3r.bubblenet@gmail.com> on 11/29/13.
 */
public class ListEntryFormFragment extends Fragment {

    private static final String ARG_FRAGMENT_ID = "ListEntryFormFragmentArgId";
    private static final int REQUEST_PICK_PHONE = 69;

    private OOOSMSDbHelper helper;
    private EditText edtPhone;
    private EditText edtMessage;

    public static ListEntryFormFragment newInstance(long id){
        ListEntryFormFragment fragment = new ListEntryFormFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_FRAGMENT_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public ListEntryFormFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =
        inflater.inflate(R.layout.list_entry_form_fragment_layout, container, false);
        v.findViewById(R.id.imgbContacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPhonePick();
            }
        });
        v.findViewById(R.id.btnAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndSaveMsg();
            }
        });
        edtPhone = (EditText) v.findViewById(R.id.edtPhone);
        edtMessage = (EditText) v.findViewById(R.id.edtMessage);
        return v;
    }

    private void validateAndSaveMsg() {
        boolean validated = true;
            if (edtPhone.getText().length() <= 0)
                validated = false;
            if (edtMessage.getText().length() <= 0)
                validated = false;
        saveEntry(edtPhone.getText().toString(), edtMessage.getText().toString());
    }

    private void saveEntry(String phone, String message) {
        try {
            Dao<Entry, Long> dao = helper.getDao(Entry.class);
            QueryBuilder<Entry, Long> query = dao.queryBuilder();
            query.where().eq(Entry.PHONE_COLUMN_NAME, phone);
            List<Entry> entries = dao.query(query.prepare());
            Entry entry = null;
            if (entries.size() > 0){
                entry = entries.get(0);
                entry.setMessage(message);
                dao.update(entry);
            }else{
                entry = new Entry(phone, message);
                dao.create(entry);
            }
            Log.d(OOTAG, String.format("Entry for phone %s saved",phone));
        }catch (SQLException ex){
            Log.e(OOTAG, "Exception while saving Entry. Fail application",ex);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (helper == null)
            helper = new OOOSMSDbHelper(activity, OOOSMSDbHelper.OOSMSDB_HEAD_VERSION);

    }

    public void launchPhonePick(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_PICK);
        i.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, REQUEST_PICK_PHONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICK_PHONE && resultCode == RESULT_OK){
            getPhoneAndUpdateField(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getPhoneAndUpdateField(Intent data) {
        Log.d(OOTAG, "Getting from from intent data and updating field");
        String phone = retrievePhone(data.getData());
        ((EditText)getView().findViewById(R.id.edtPhone)).setText(phone);
    }

    private String retrievePhone(Uri data) {
        String id = data.getLastPathSegment();
        Cursor c =
        getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DATA1},"_id=?",new String[]{id},null);
        c.moveToNext();
        String phone = c.getString(0);
        c.close();
        Log.d(OOTAG, "Cursor closed");
        //c.toString());
        return phone;
    }
}
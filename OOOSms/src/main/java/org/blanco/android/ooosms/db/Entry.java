package org.blanco.android.ooosms.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Alexandro Blanco <ti3r.bubblenet@gmail.com> on 11/30/13.
 */

@DatabaseTable(tableName = "ListEntries")
public class Entry  {

    public static final String ID_COLUMN_NAME = "_id";
    public static final String PHONE_COLUMN_NAME = "phone";
    public static final String MESSAGE_COLUMN_NAME = "message";
    public static final String ROW_ORDER_COLUMN_NAME = "rowOrder";

    @DatabaseField(columnName = ID_COLUMN_NAME, id = true)
    private long id;
    @DatabaseField(columnName = PHONE_COLUMN_NAME)
    private String phone;
    @DatabaseField(columnName = MESSAGE_COLUMN_NAME)
    private String message;
    @DatabaseField(columnName = ROW_ORDER_COLUMN_NAME)
    private int rowOrder;

    public Entry() {

    }

    public Entry(String phone, String message) {
        this.phone = phone;
        this.message = message;
        this.rowOrder = 50;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getOrder() {
        return rowOrder;
    }

    public void setOrder(int order) {
        this.rowOrder = order;
    }
}

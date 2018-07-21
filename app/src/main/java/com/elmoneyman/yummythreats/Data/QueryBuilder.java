package com.elmoneyman.yummythreats.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QueryBuilder {

    private String table = null;
    private Map<String, String> projectionMap = new HashMap<>();
    private StringBuilder selection = new StringBuilder();
    private ArrayList<String> selectionArgs = new ArrayList<>();
    private String groupBy = null;
    private String having = null;


    public QueryBuilder reset() {
        table = null;
        groupBy = null;
        having = null;
        selection.setLength(0);
        selectionArgs.clear();
        return this;
    }

    public QueryBuilder where(String selection, String... selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            if (selectionArgs != null && selectionArgs.length > 0) {
                throw new IllegalArgumentException(
                        "Valid selection required when including arguments=");
            }

            return this;
        }

        if (this.selection.length() > 0) {
            this.selection.append(" AND ");
        }

        this.selection.append("(").append(selection).append(")");
        if (selectionArgs != null) {
            Collections.addAll( this.selectionArgs, selectionArgs);
        }

        return this;
    }

    public QueryBuilder groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public QueryBuilder having(String having) {
        this.having = having;
        return this;
    }

    public QueryBuilder table(String table) {
        table = table;
        return this;
    }

    public QueryBuilder table(String table, String... tableParams) {
        if (tableParams != null && tableParams.length > 0) {
            String[] parts = table.split("[?]", tableParams.length+1);
            StringBuilder sb = new StringBuilder(parts[0]);
            for (int i=1; i<parts.length; i++) {
                sb.append('"').append(tableParams[i-1]).append('"')
                        .append(parts[i]);
            }
            table = sb.toString();
        } else {
            table = table;
        }
        return this;
    }

    private void assertTable() {
        if (table == null) {
            throw new IllegalStateException("Table not specified");
        }
    }

    public QueryBuilder mapToTable(String column, String table) {
        projectionMap.put(column, table + "." + column);
        return this;
    }

    public QueryBuilder map(String fromColumn, String toClause) {
        projectionMap.put(fromColumn, toClause + " AS " + fromColumn);
        return this;
    }

    public String getSelection() {
        return selection.toString();
    }

    public String[] getSelectionArgs() {
        return selectionArgs.toArray(new String[selectionArgs.size()]);
    }

    private void mapColumns(String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            final String target = projectionMap.get(columns[i]);
            if (target != null) {
                columns[i] = target;
            }
        }
    }

    @Override
    public String toString() {
        return "SelectionBuilder[table=" + table + ", selection=" + getSelection()
                + ", selectionArgs=" + Arrays.toString(getSelectionArgs())
                + "projectionMap = " + projectionMap + " ]";
    }

    public Cursor query(SQLiteDatabase db, String[] columns, String orderBy) {
        return query(db, false, columns, orderBy, null);
    }

    public Cursor query(SQLiteDatabase db, boolean distinct, String[] columns, String orderBy,
                        String limit) {
        assertTable();
        if (columns != null) mapColumns(columns);
        return db.query(distinct, table, columns, getSelection(), getSelectionArgs(), groupBy,
                having, orderBy, limit);
    }

    public int update(SQLiteDatabase db, ContentValues values) {
        assertTable();
        return db.update(table, values, getSelection(), getSelectionArgs());
    }

    public int delete(SQLiteDatabase db) {
        assertTable();
        return db.delete(table, getSelection(), getSelectionArgs());
    }
}
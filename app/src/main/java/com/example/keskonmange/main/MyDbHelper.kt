package com.example.keskonmange.main

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    val CREATE_TABLE_BAR = ("CREATE TABLE $TABLE_NAME_BAR " +
            "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "$COLUMN_NAME TEXT, $COLUMN_VOTES INTEGER, $COLUMN_POS INTEGER, " +
            "$COLUMN_VETTO BOOLEAN, $COLUMN_TYPE TEXT)")

    val CREATE_TABLE_MAISON = ("CREATE TABLE $TABLE_NAME_MAISON " +
    "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
    "$COLUMN_NAME TEXT, " +
    "$COLUMN_TYPE TEXT, " +
    "$COLUMN_VOTES INTEGER, " +
    "$COLUMN_VETTO BOOLEAN, " +
    "$COLUMN_POS INTEGER)")

    val CREATE_TABLE_SETTINGS = ("CREATE TABLE $TABLE_SETTINGS " +
    "($VAL_SORT TEXT, " +
    "$VAL_SCALE TEXT)")

    fun createTableSettings(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_SETTINGS)
        val values = ContentValues()
        values.put(VAL_SORT, "")
        values.put(VAL_SCALE, "compact")
        db.insert(TABLE_SETTINGS, null, values)

    }
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_TABLE_BAR)
            db.execSQL(CREATE_TABLE_MAISON)
            createTableSettings(db)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            when(oldVersion) {
                1 -> {
                    db.execSQL("DROP TABLE IF EXISTS $TABLE_SORT")
                    createTableSettings(db)
                }
            }
        }

        fun addRestoBar(resto: Resto): Boolean {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_NAME, resto.name)
            values.put(COLUMN_TYPE, resto.type)
            values.put(COLUMN_VOTES, resto.votes)
            values.put(COLUMN_VETTO, resto.vetto)
        val _success = db.insert(TABLE_NAME_BAR, null, values)
            db.close()
            return (Integer.parseInt("$_success") != -1)
        }
        fun addRestoMaison(resto: Resto): Boolean {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_NAME, resto.name)
            values.put(COLUMN_TYPE, resto.type)
            values.put(COLUMN_VOTES, resto.votes)
            values.put(COLUMN_VETTO, resto.vetto)
            val _success = db.insert(TABLE_NAME_MAISON, null, values)
            db.close()
            return (Integer.parseInt("$_success") != -1)
        }

        fun updateOrderRestoBar(name: String, type: String, order: Int): Boolean {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_POS, order)
            val _success = db.update(TABLE_NAME_BAR, values,"$COLUMN_NAME =? AND $COLUMN_TYPE =?", arrayOf(name, type)).toLong()
            db.close()
            return (Integer.parseInt("$_success") != -1)
        }

        fun updateOrderRestoMaison(name: String, type: String, order: Int): Boolean {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_POS, order)
            val _success = db.update(TABLE_NAME_MAISON, values,"$COLUMN_NAME =? AND $COLUMN_TYPE =?", arrayOf(name, type)).toLong()
            db.close()
            return (Integer.parseInt("$_success") != -1)
        }

        fun updateVettoRestoBar(name: String, type: String, vetto: Boolean): Boolean {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_VETTO, vetto)
            val _success = db.update(TABLE_NAME_BAR, values,"$COLUMN_NAME =? AND $COLUMN_TYPE =?", arrayOf(name, type)).toLong()
            db.close()
            return (Integer.parseInt("$_success") != -1)
        }

        fun updateVettoRestoMaison(name: String, type: String, vetto: Boolean): Boolean {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_VETTO, vetto)
            val _success = db.update(TABLE_NAME_MAISON, values,"$COLUMN_NAME =? AND $COLUMN_TYPE =?", arrayOf(name, type)).toLong()
            db.close()
            return (Integer.parseInt("$_success") != -1)
        }

        fun updateVotesRestoBar(name: String, type: String, votes: Int): Boolean {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_VOTES, votes)
            val _success = db.update(TABLE_NAME_BAR, values,"$COLUMN_NAME =? AND $COLUMN_TYPE =?", arrayOf(name, type)).toLong()
            db.close()
            return (Integer.parseInt("$_success") != -1)
        }

        fun updateVotesRestoMaison(name: String, type: String, votes: Int): Boolean {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_VOTES, votes)
            val _success = db.update(TABLE_NAME_MAISON, values,"$COLUMN_NAME =? AND $COLUMN_TYPE =?", arrayOf(name, type)).toLong()
            db.close()
            return (Integer.parseInt("$_success") != -1)
        }

        fun deleteRestoBar(name: String, type: String): Boolean {
            val db = this.writableDatabase
            val _success = db.delete(TABLE_NAME_BAR, "$COLUMN_NAME =? AND $COLUMN_TYPE =?", arrayOf(name, type)).toLong()
            db.close()
            return Integer.parseInt("$_success") != -1
        }

        fun deleteRestoMaison(name: String, type: String): Boolean {
            val db = this.writableDatabase
            val _success = db.delete(TABLE_NAME_MAISON, "$COLUMN_NAME =? AND $COLUMN_TYPE =?", arrayOf(name, type)).toLong()
            db.close()
            return Integer.parseInt("$_success") != -1
        }

        fun getAllRestoMaison(): Cursor? {
            val db = this.readableDatabase
            return db.rawQuery("SELECT * FROM $TABLE_NAME_MAISON", null)
        }

        fun getAllRestoBar(): Cursor? {
            val db = this.readableDatabase
            return db.rawQuery("SELECT * FROM $TABLE_NAME_BAR", null)
        }

        fun getSelectedRestoBar(): Cursor? {
            val db = this.readableDatabase
            return db.rawQuery("SELECT * FROM $TABLE_NAME_BAR WHERE $COLUMN_VOTES > 0", null)
        }
        fun getSelectedRestoMaison(): Cursor? {
            val db = this.readableDatabase
            return db.rawQuery("SELECT * FROM $TABLE_NAME_MAISON WHERE $COLUMN_VOTES > 0", null)
        }

        @SuppressLint("Recycle")
        fun getSort(): String {
            val db = this.readableDatabase
            val query = db.rawQuery("SELECT $VAL_SORT FROM $TABLE_SETTINGS", null)
            query!!.moveToFirst()
            return if (query.count > 0)
                query.getString(query.getColumnIndex("val_sort"))
            else
                query.count.toString()
        }
        @SuppressLint("Recycle")
        fun getScale(): String {
            val db = this.readableDatabase
            val query = db.rawQuery("SELECT $VAL_SCALE FROM $TABLE_SETTINGS", null)
            query!!.moveToFirst()
            return if (query.count > 0)
                query.getString(query.getColumnIndex("val_scale"))
            else
                query.count.toString()
        }

    fun updateSort(sort: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(VAL_SORT, sort)
        val _success = db.update(TABLE_SETTINGS, values, "", null).toLong()
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    fun updateScale(scale: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(VAL_SCALE, scale)
        val _success = db.update(TABLE_SETTINGS, values, "", null).toLong()
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

        companion object {
            private val DATABASE_VERSION = 2
            private val DATABASE_NAME = "test.db"
            val TABLE_NAME_BAR = "restos_bar"
            val TABLE_NAME_MAISON = "restos_maison"
            val TABLE_SORT = "sort"
            val TABLE_SETTINGS = "settings"
            val COLUMN_ID = "_id"
            val COLUMN_NAME = "title"
            val COLUMN_TYPE = "type"
            val COLUMN_VOTES = "votes"
            val COLUMN_VETTO = "vetto"
            val COLUMN_POS = "pos"
            val VAL_SORT = "val_sort"
            val VAL_SCALE = "val_scale"
        }
}

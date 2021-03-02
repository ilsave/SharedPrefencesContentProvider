package com.example.sharedprefscontentprovider

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

class ShapedPreferencesProvider: ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.sharedprefscontentprovider.ilsavePreferences"
        const val DIARY_ENTRY_TABLE = "DiaryEntry"
        val DIARY_TABLE_CONTENT_URI: Uri = Uri.parse("content://" +
                AUTHORITY + "/" + DIARY_ENTRY_TABLE)
        val SAVED_TEXT = "saved_text"
    }

    private val DIARY_ENTRIES = 1
    private val DIARY_ENTRY_ID = 2
    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    var sPref: SharedPreferences? = null


    init {
        sUriMatcher.addURI(AUTHORITY, DIARY_ENTRY_TABLE, DIARY_ENTRIES)
        sUriMatcher.addURI(AUTHORITY, "$DIARY_ENTRY_TABLE/#", DIARY_ENTRY_ID)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val uriType = sUriMatcher.match(uri)
        var cursor: Cursor? = null
        when(uriType){
            DIARY_ENTRY_ID -> {
                val id: Long? = uri.lastPathSegment?.toLong()
                if (id == 1L) {
                    val variable = values?.get(SAVED_TEXT).toString()
                    val ed: SharedPreferences.Editor? = sPref?.let { it.edit() }
                    ed?.putString(SAVED_TEXT, variable)
                    ed?.commit()
                }
            }
            else -> throw IllegalArgumentException("Uknown Uri")
        }
        return uri
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val uriType = sUriMatcher.match(uri)
        var cursor: MatrixCursor? = null
        when(uriType){
            DIARY_ENTRY_ID -> {
                val id: Long? = uri.lastPathSegment?.toLong()
                if (id == 1L) {
                    cursor = MatrixCursor(arrayOf<String>(SAVED_TEXT))
                    val cursorBuilder: MatrixCursor.RowBuilder = cursor.newRow()
                    cursorBuilder.add(sPref?.getString(SAVED_TEXT, "0"))
                }
            }
            else -> throw IllegalArgumentException("Uknown Uri")
        }
        return cursor
    }

    override fun onCreate(): Boolean {
        sPref =context?.getSharedPreferences("settings",Context.MODE_PRIVATE)
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val uriType = sUriMatcher.match(uri)
        var cursor: Cursor? = null
        when(uriType){
            DIARY_ENTRY_ID -> {
                val id: Long? = uri.lastPathSegment?.toLong()
                if (id == 1L) {
                    val variable = values?.get(SAVED_TEXT).toString()
                    val ed: SharedPreferences.Editor? = sPref?.let { it.edit() }
                    ed?.putString(SAVED_TEXT, variable)
                    ed?.commit()
                }
            }
            else -> throw IllegalArgumentException("Uknown Uri")
        }
        return 1
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val uriType = sUriMatcher.match(uri)
        var cursor: Cursor? = null
        when(uriType){
            DIARY_ENTRY_ID -> {
                val id: Long? = uri.lastPathSegment?.toLong()
                if (id == 1L) {
                    sPref?.edit()?.remove(SAVED_TEXT)?.apply();
                }
            }
            else -> throw IllegalArgumentException("Uknown Uri")
        }
        return 1
    }

    override fun getType(uri: Uri): String? {
        return "text"
    }

}
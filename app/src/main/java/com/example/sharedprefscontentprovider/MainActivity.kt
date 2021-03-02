package com.example.sharedprefscontentprovider

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.MatrixCursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    lateinit var buttonOverride: Button
    lateinit var editTextValue: EditText
    val SAVED_TEXT = "saved_text"

    var sPref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val DIARY_TABLE_CONTENT_URI: Uri = Uri.parse("content://"
                + ShapedPreferencesProvider.AUTHORITY + "/"
                + ShapedPreferencesProvider.DIARY_ENTRY_TABLE
                + "/"
                + "1"
        )

        sPref = getSharedPreferences("settings" ,Context.MODE_PRIVATE)

        buttonOverride = findViewById(R.id.buttonWriteToPrefs)
        editTextValue = findViewById(R.id.editTextTextPrefs)

        // FOR THE FIRST PART OF HOMEWORK!
       // val savedText = sPref?.getString(SAVED_TEXT, "0")
       // editTextValue.setText(savedText)

        Log.d("Ilsave", DIARY_TABLE_CONTENT_URI.toString())

        val savedtext  = contentResolver.query(DIARY_TABLE_CONTENT_URI,
            null,
            null,
            null,
        null)
        if (savedtext!!.moveToFirst()){
            editTextValue.setText(savedtext.getString(savedtext.getColumnIndex(SAVED_TEXT)))
        }

        buttonOverride.setOnClickListener {
            val contentValues = ContentValues()
            contentValues.put(SAVED_TEXT, editTextValue.text.toString())
            contentResolver.update(DIARY_TABLE_CONTENT_URI, contentValues, null, null)

            // FOR THE FIRST PART OF HOMEWORK!
//            val ed: SharedPreferences.Editor? = sPref?.let { it.edit() }
//            ed?.putString(SAVED_TEXT, editTextValue.text.toString())
//            ed?.commit()
        }



    }
}
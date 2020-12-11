package com.example.notetoself

import android.content.Context
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONTokener
import java.io.*
import java.lang.StringBuilder

class JSONSerializer(private val filename: String, private val context: Context) {

    @Throws(IOException::class, JSONException::class)
    fun save(notes: List<Note>){
        val jArray = JSONArray()

        for (n in notes)
            jArray.put(n.convertToJSON())

        var writer: Writer? = null
        try {
            val out = context.openFileOutput(filename,Context.MODE_PRIVATE)

            writer = OutputStreamWriter(out)
            writer.write(jArray.toString())

        }
        finally {
            if (writer != null){
                writer.close()
            }
        }
    }

    @Throws(IOException::class, JSONException::class)
    fun load() : ArrayList<Note>{
        val noteList = ArrayList<Note>()
        var reader: BufferedReader? = null

        try {
            val `in` = context.openFileInput(filename)
            reader = BufferedReader(InputStreamReader(`in`))
            val jsonString = StringBuilder()

            for (line in reader.readLines()) {
                jsonString.append(line)
            }

            val jArray = JSONTokener(jsonString.toString()).nextValue() as JSONArray

            for (i in 0 until jArray.length()){
                noteList.add(Note(jArray.getJSONObject(i)))
            }

        }catch (e: FileNotFoundException) {

        }finally {
            reader!!.close()
        }
        return noteList
    }
}
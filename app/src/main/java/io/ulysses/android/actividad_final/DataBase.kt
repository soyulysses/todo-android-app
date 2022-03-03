package io.ulysses.android.actividad_final

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    val TAG = "SQLite"

    companion object {
        const val DATABASE_VERSION = 4
        const val DATABASE_NAME = "recordatorios_app.db"
        const val TABLA_RECORDATORIOS = "recordatorios"
        const val COLUMNA_ID = "id"
        const val COLUMNA_TITULO = "titulo"
        const val COLUMNA_DESCRIPCION = "descripcion"
        const val COLUMNA_IMPORTANTE = "importante"
        const val COLUMNA_TERMINADO = "terminado"
        const val COLUMNA_FOTO = "foto"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val crearTablaAmigos = "CREATE TABLE $TABLA_RECORDATORIOS (" +
                    "$COLUMNA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMNA_TITULO TEXT," +
                    " $COLUMNA_DESCRIPCION TEXT," +
                    " $COLUMNA_IMPORTANTE BOOLEAN," +
                    " $COLUMNA_TERMINADO BOOLEAN," +
                    " $COLUMNA_FOTO INTEGER)"
            db!!.execSQL(crearTablaAmigos)
        } catch (e: SQLiteException) {

        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            val dropTablaAmigos = "DROP TABLE IF EXISTS $TABLA_RECORDATORIOS"
            db!!.execSQL(dropTablaAmigos)
            onCreate(db)
        } catch (e: SQLiteException) {

        }
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

    /**
     * Añade un recordatorio a la Base de Datos
     * */
    fun anyadirRecordatorio(titulo: String, descripcion: String, importante: Boolean, foto: Int) {
        val data = ContentValues()
        data.put(COLUMNA_TITULO, titulo)
        data.put(COLUMNA_DESCRIPCION, descripcion)
        data.put(COLUMNA_IMPORTANTE, importante)
        data.put(COLUMNA_TERMINADO, false)
        data.put(COLUMNA_FOTO, foto)

        val sqLiteDatabase = writableDatabase
        sqLiteDatabase.insert(TABLA_RECORDATORIOS, null, data)
        sqLiteDatabase.close()
    }

    /**
     * Obtiene un cursor de todos los recordatorios
     */
    fun obtenerRecordatorios(importante: Boolean): Cursor {
        return if (importante) readableDatabase.rawQuery("SELECT * FROM ${TABLA_RECORDATORIOS} WHERE ${COLUMNA_IMPORTANTE} = 1;", null) else readableDatabase.rawQuery("SELECT * FROM ${TABLA_RECORDATORIOS};", null)
    }

    /**
     * Elimina un recordatorio en la Base de Datos
     */
    fun eliminarRecordatorio(idRecordatorio: Int): Int {
        return writableDatabase.delete(TABLA_RECORDATORIOS, "$COLUMNA_ID = $idRecordatorio",null)
    }

    /**
     * Modifica un recordatorio en la Base de Datos, la columna terminado
     */
    fun terminarRecordatorio(idRecordatorio: Int, terminado: Boolean): Int {
        val cv = ContentValues()
        cv.put(COLUMNA_TERMINADO, terminado)
        return writableDatabase.update(TABLA_RECORDATORIOS, cv , "$COLUMNA_ID = $idRecordatorio",null)
    }

    /**
     * Modifica un recordatorio en la Base de Datos, la columna importante
     */
    fun importanteRecordatorio(idRecordatorio: Int, importante: Boolean): Int {
        val cv = ContentValues()
        cv.put(COLUMNA_IMPORTANTE, importante)
        return writableDatabase.update(TABLA_RECORDATORIOS, cv , "$COLUMNA_ID = $idRecordatorio",null)
    }

    /**
     * Modifica un recordatorio en la Base de Datos
     */
    fun acutalizarRecordatorio(id: Int, titulo: String, descripcion: String, importante: Boolean, terminado: Boolean, foto: Int): Int {
        val cv = ContentValues()
        cv.put(COLUMNA_TITULO, titulo)
        cv.put(COLUMNA_DESCRIPCION, descripcion)
        cv.put(COLUMNA_IMPORTANTE, importante)
        cv.put(COLUMNA_TERMINADO, terminado)
        cv.put(COLUMNA_FOTO, foto)

        return writableDatabase.update(TABLA_RECORDATORIOS, cv,"$COLUMNA_ID = $id",null)
    }
}

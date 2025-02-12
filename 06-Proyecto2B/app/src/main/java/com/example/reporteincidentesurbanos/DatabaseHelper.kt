package com.example.reporteincidentesurbanos.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.reporteincidentesurbanos.Reporte
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "reportes.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_REPORTES = "reportes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITULO = "titulo"
        private const val COLUMN_DESCRIPCION = "descripcion"
        private const val COLUMN_TIPO = "tipo"
        private const val COLUMN_FOTO = "foto"
        private const val COLUMN_LATITUD = "latitud"
        private const val COLUMN_LONGITUD = "longitud"
        private const val COLUMN_FECHA = "fecha"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """CREATE TABLE $TABLE_REPORTES (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_TITULO TEXT NOT NULL,
            $COLUMN_DESCRIPCION TEXT NOT NULL,
            $COLUMN_TIPO TEXT NOT NULL,
            $COLUMN_FOTO TEXT,
            $COLUMN_LATITUD REAL NOT NULL,
            $COLUMN_LONGITUD REAL NOT NULL,
            $COLUMN_FECHA TEXT NOT NULL)"""
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REPORTES")
        onCreate(db)
    }

    fun agregarReporte(reporte: Reporte) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("titulo", reporte.titulo)
        values.put("descripcion", reporte.descripcion)
        values.put("tipo", reporte.tipo)
        values.put("foto", reporte.foto)
        values.put("latitud", reporte.latitud)
        values.put("longitud", reporte.longitud)
        values.put("fecha", reporte.fecha)

        db.insert("reportes", null, values)
        db.close()
    }


    fun obtenerReportes(): List<Reporte> {
        val reportes = mutableListOf<Reporte>()
        val db = readableDatabase
        val cursor = db.query(TABLE_REPORTES, null, null, null, null, null, "$COLUMN_FECHA DESC")

        with(cursor) {
            while (moveToNext()) {
                reportes.add(
                    Reporte(
                        getInt(getColumnIndexOrThrow(COLUMN_ID)),
                        getString(getColumnIndexOrThrow(COLUMN_TITULO)),
                        getString(getColumnIndexOrThrow(COLUMN_DESCRIPCION)),
                        getString(getColumnIndexOrThrow(COLUMN_TIPO)),
                        getString(getColumnIndexOrThrow(COLUMN_FOTO)),
                        getDouble(getColumnIndexOrThrow(COLUMN_LATITUD)),
                        getDouble(getColumnIndexOrThrow(COLUMN_LONGITUD)),
                        getString(getColumnIndexOrThrow(COLUMN_FECHA))
                    )
                )
            }
        }
        cursor.close()
        return reportes
    }

    fun actualizarReporte(reporte: Reporte) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITULO, reporte.titulo)
            put(COLUMN_DESCRIPCION, reporte.descripcion)
            put(COLUMN_TIPO, reporte.tipo)
            put(COLUMN_FOTO, reporte.foto)
            put(COLUMN_LATITUD, reporte.latitud)
            put(COLUMN_LONGITUD, reporte.longitud)
        }
        db.update(
            TABLE_REPORTES,
            values,
            "$COLUMN_ID = ?",
            arrayOf(reporte.id.toString())
        )
        db.close()
    }

    fun obtenerReportePorId(id: Int): Reporte? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_REPORTES,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                Reporte(
                    it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_TITULO)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPCION)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_TIPO)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_FOTO)),
                    it.getDouble(it.getColumnIndexOrThrow(COLUMN_LATITUD)),
                    it.getDouble(it.getColumnIndexOrThrow(COLUMN_LONGITUD)),
                    it.getString(it.getColumnIndexOrThrow(COLUMN_FECHA))
                )
            } else {
                null
            }
        }
    }
    fun eliminarReporte(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_REPORTES, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}

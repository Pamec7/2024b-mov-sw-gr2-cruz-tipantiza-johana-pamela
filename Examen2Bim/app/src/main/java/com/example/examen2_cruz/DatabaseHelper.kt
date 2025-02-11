package com.example.examen2_cruz

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PaisesCiudades.db"
        private const val DATABASE_VERSION = 2

        // Tabla Países
        const val TABLE_PAISES = "paises"
        const val COL_PAIS_ID = "id"
        const val COL_NOMBRE = "nombre"
        const val COL_CODIGO_ISO = "codigo_iso"
        const val COL_CONTINENTE = "continente"
        const val COL_POBLACION = "poblacion"
        const val COL_ES_MIEMBRO_ONU = "es_miembro_onu"

        // Tabla Ciudades
        const val TABLE_CIUDADES = "ciudades"
        const val COL_CIUDAD_ID = "id"
        const val COL_PAIS_ID_FK = "pais_id"
        const val COL_NOMBRE_CIUDAD = "nombre_ciudad"
        const val COL_POBLACION_CIUDAD = "poblacion_ciudad"
        const val COL_ALTITUD = "altitud"
        const val COL_FECHA_FUNDACION = "fecha_fundacion"
        const val COL_ES_CAPITAL = "es_capital"
        const val COL_LATITUD = "latitud"
        const val COL_LONGITUD = "longitud"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla Países
        db.execSQL("""
            CREATE TABLE $TABLE_PAISES (
                $COL_PAIS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT NOT NULL,
                $COL_CODIGO_ISO TEXT NOT NULL,
                $COL_CONTINENTE TEXT NOT NULL,
                $COL_POBLACION INTEGER NOT NULL,
                $COL_ES_MIEMBRO_ONU INTEGER NOT NULL
            )
        """)

        // Crear tabla Ciudades
        db.execSQL("""
            CREATE TABLE $TABLE_CIUDADES (
                $COL_CIUDAD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PAIS_ID_FK INTEGER NOT NULL,
                $COL_NOMBRE_CIUDAD TEXT NOT NULL,
                $COL_POBLACION_CIUDAD INTEGER NOT NULL,
                $COL_ALTITUD REAL NOT NULL,
                $COL_FECHA_FUNDACION TEXT NOT NULL,
                $COL_ES_CAPITAL INTEGER NOT NULL,
                $COL_LATITUD REAL NOT NULL,
                $COL_LONGITUD REAL NOT NULL,
                FOREIGN KEY ($COL_PAIS_ID_FK) REFERENCES $TABLE_PAISES($COL_PAIS_ID)
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CIUDADES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PAISES")
        onCreate(db)
    }

    // Operaciones CRUD para Países
    fun insertarPais(pais: Pais): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(com.example.examen2_cruz.DatabaseHelper.Companion.COL_NOMBRE, pais.nombre)
            put(com.example.examen2_cruz.DatabaseHelper.Companion.COL_CODIGO_ISO, pais.codigoISO)
            put(com.example.examen2_cruz.DatabaseHelper.Companion.COL_CONTINENTE, pais.continente)
            put(com.example.examen2_cruz.DatabaseHelper.Companion.COL_POBLACION, pais.poblacion)
            put(com.example.examen2_cruz.DatabaseHelper.Companion.COL_ES_MIEMBRO_ONU, if (pais.esMiembroONU) 1 else 0)
        }
        val id = db.insert(com.example.examen2_cruz.DatabaseHelper.Companion.TABLE_PAISES, null, values)
        db.close()
        return id
    }

    fun obtenerTodosPaises(): List<Pais> {
        val paises = mutableListOf<Pais>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${com.example.examen2_cruz.DatabaseHelper.Companion.TABLE_PAISES}", null)

        if (cursor.moveToFirst()) {
            do {
                paises.add(
                    Pais(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(com.example.examen2_cruz.DatabaseHelper.Companion.COL_PAIS_ID)),
                        nombre = cursor.getString(cursor.getColumnIndexOrThrow(com.example.examen2_cruz.DatabaseHelper.Companion.COL_NOMBRE)),
                        codigoISO = cursor.getString(cursor.getColumnIndexOrThrow(com.example.examen2_cruz.DatabaseHelper.Companion.COL_CODIGO_ISO)),
                        continente = cursor.getString(cursor.getColumnIndexOrThrow(com.example.examen2_cruz.DatabaseHelper.Companion.COL_CONTINENTE)),
                        poblacion = cursor.getInt(cursor.getColumnIndexOrThrow(com.example.examen2_cruz.DatabaseHelper.Companion.COL_POBLACION)),
                        esMiembroONU = cursor.getInt(cursor.getColumnIndexOrThrow(com.example.examen2_cruz.DatabaseHelper.Companion.COL_ES_MIEMBRO_ONU)) == 1
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return paises
    }

    fun eliminarPais(paisId: Int) {
        val db = this.writableDatabase
        db.delete(com.example.examen2_cruz.DatabaseHelper.Companion.TABLE_PAISES, "${com.example.examen2_cruz.DatabaseHelper.Companion.COL_PAIS_ID} = ?", arrayOf(paisId.toString()))
        db.close()
    }

    // Operaciones CRUD para Ciudades
    fun insertarCiudad(ciudad: Ciudad, paisId: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_PAIS_ID_FK, paisId)
            put(COL_NOMBRE_CIUDAD, ciudad.nombre)
            put(COL_POBLACION_CIUDAD, ciudad.poblacion)
            put(COL_ALTITUD, ciudad.altitud)
            put(COL_FECHA_FUNDACION, ciudad.fechaFundacion)
            put(COL_ES_CAPITAL, if (ciudad.esCapital) 1 else 0)
            put(COL_LATITUD, ciudad.latitud)
            put(COL_LONGITUD, ciudad.longitud)
        }
        return db.insert(TABLE_CIUDADES, null, values)
    }

    fun obtenerCiudadesDePais(paisId: Int): List<Ciudad> {
        val ciudades = mutableListOf<Ciudad>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_CIUDADES WHERE $COL_PAIS_ID_FK = ?",
            arrayOf(paisId.toString())
        )
        while (cursor.moveToNext()) {
            ciudades.add(
                Ciudad(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CIUDAD_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE_CIUDAD)),
                    poblacion = cursor.getInt(cursor.getColumnIndexOrThrow(COL_POBLACION_CIUDAD)),
                    altitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ALTITUD)),
                    fechaFundacion = cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA_FUNDACION)),
                    esCapital = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ES_CAPITAL)) == 1,
                    latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LATITUD)),
                    longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LONGITUD))
                )
            )
        }
        cursor.close()
        return ciudades
    }
    fun eliminarCiudad(ciudadId: Int) {
        val db = this.writableDatabase
        db.delete(com.example.examen2_cruz.DatabaseHelper.Companion.TABLE_CIUDADES, "${com.example.examen2_cruz.DatabaseHelper.Companion.COL_CIUDAD_ID} = ?", arrayOf(ciudadId.toString()))
        db.close()
    }

}
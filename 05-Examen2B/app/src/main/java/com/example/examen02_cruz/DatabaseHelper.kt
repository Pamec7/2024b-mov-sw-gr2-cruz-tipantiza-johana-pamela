package com.example.examen02_cruz

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log

class DatabaseHelper(context: Context) : android.database.sqlite.SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PaisesCiudades.db"
        private const val DATABASE_VERSION = 2

        // Tabla Países
        const val TABLE_PAISES = "paises"
        const val COL_PAIS_ID = "id"
        const val COL_PAIS_NOMBRE = "nombre_pais"
        const val COL_PAIS_POBLACION = "poblacion_pais"
        const val COL_CODIGO_ISO = "codigo_iso"
        const val COL_CONTINENTE = "continente"
        const val COL_ES_MIEMBRO_ONU = "es_miembro_onu"

        // Tabla Ciudades
        const val TABLE_CIUDADES = "ciudades"
        const val COL_CIUDAD_ID = "id"
        const val COL_PAIS_ID_FK = "pais_id"
        const val COL_CIUDAD_NOMBRE = "nombre_ciudad"
        const val COL_CIUDAD_POBLACION = "poblacion_ciudad"
        const val COL_CIUDAD_ALTITUD = "altitud"
        const val COL_FECHA_FUNDACION = "fecha_fundacion"
        const val COL_ES_CAPITAL = "es_capital"
        const val COL_LATITUD = "latitud"
        const val COL_LONGITUD = "longitud"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla Países
        val createPaisesTable = """
            CREATE TABLE $TABLE_PAISES (
                $COL_PAIS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PAIS_NOMBRE TEXT NOT NULL,
                $COL_CODIGO_ISO TEXT NOT NULL,
                $COL_CONTINENTE TEXT NOT NULL,
                 $COL_PAIS_POBLACION INTEGER NOT NULL,
                $COL_ES_MIEMBRO_ONU INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createPaisesTable)

        // Crear tabla Ciudades
        val createCiudadesTable = """
            CREATE TABLE $TABLE_CIUDADES (
                $COL_CIUDAD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PAIS_ID_FK INTEGER NOT NULL,
                $COL_CIUDAD_NOMBRE TEXT NOT NULL,
                $COL_CIUDAD_POBLACION INTEGER NOT NULL,
                $COL_CIUDAD_ALTITUD REAL NOT NULL,
                $COL_FECHA_FUNDACION TEXT NOT NULL,
                $COL_ES_CAPITAL INTEGER NOT NULL,
                $COL_LATITUD REAL NOT NULL,
                $COL_LONGITUD REAL NOT NULL,
                FOREIGN KEY ($COL_PAIS_ID_FK) REFERENCES $TABLE_PAISES($COL_PAIS_ID) ON DELETE CASCADE
            )
        """.trimIndent()
        db.execSQL(createCiudadesTable)
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
            put(COL_PAIS_NOMBRE, pais.nombre)
            put(COL_CODIGO_ISO, pais.codigoISO)
            put(COL_CONTINENTE, pais.continente)
            put(COL_PAIS_POBLACION, pais.poblacion)
            put(COL_ES_MIEMBRO_ONU, if (pais.esMiembroONU) 1 else 0)
        }
        val id = db.insert(TABLE_PAISES, null, values)
        db.close()
        return id
    }

    fun obtenerTodosPaises(): List<Pais> {
        val paises = mutableListOf<Pais>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PAISES", null)

        if (cursor.moveToFirst()) {
            do {
                paises.add(
                    Pais(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAIS_ID)),
                        nombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_PAIS_NOMBRE)),
                        codigoISO = cursor.getString(cursor.getColumnIndexOrThrow(COL_CODIGO_ISO)),
                        continente = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTINENTE)),
                        poblacion = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAIS_POBLACION)),
                        esMiembroONU = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ES_MIEMBRO_ONU)) == 1
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
        db.delete(TABLE_PAISES, "$COL_PAIS_ID = ?", arrayOf(paisId.toString()))
        db.close()
    }

    // Operaciones CRUD para Ciudades
    fun insertarCiudad(ciudad: Ciudad, paisId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_PAIS_ID_FK, paisId)
            put(COL_CIUDAD_NOMBRE, ciudad.nombre)
            put(COL_CIUDAD_POBLACION, ciudad.poblacion)
            put(COL_CIUDAD_ALTITUD, ciudad.altitud)
            put(COL_FECHA_FUNDACION, ciudad.fechaFundacion)
            put(COL_ES_CAPITAL, if (ciudad.esCapital) 1 else 0)
            put(COL_LATITUD, ciudad.latitud)
            put(COL_LONGITUD, ciudad.longitud)
        }
        val id = db.insert(TABLE_CIUDADES, null, values)
        db.close()
        return id
    }

    fun actualizarPais(pais: Pais): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_PAIS_NOMBRE, pais.nombre)
            put(COL_CODIGO_ISO, pais.codigoISO)
            put(COL_CONTINENTE, pais.continente)
            put(COL_PAIS_POBLACION, pais.poblacion)
            put(COL_ES_MIEMBRO_ONU, pais.esMiembroONU)
        }
        val filasAfectadas = db.update(
            TABLE_PAISES,
            values,
            "$COL_PAIS_ID = ?",
            arrayOf(pais.id.toString())
        )
        db.close()
        return filasAfectadas
    }

    fun obtenerCiudadesDePais(paisId: Int): List<Ciudad> {
        val ciudades = mutableListOf<Ciudad>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_CIUDADES WHERE $COL_PAIS_ID_FK = ?",
            arrayOf(paisId.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                ciudades.add(
                    Ciudad(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CIUDAD_ID)),
                        nombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_CIUDAD_NOMBRE)),
                        poblacion = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CIUDAD_POBLACION)),
                        altitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_CIUDAD_ALTITUD)),
                        fechaFundacion = cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA_FUNDACION)),
                        esCapital = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ES_CAPITAL)) == 1,
                        latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LATITUD)),
                        longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LONGITUD))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return ciudades
    }

    fun eliminarCiudad(ciudadId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_CIUDADES, "$COL_CIUDAD_ID = ?", arrayOf(ciudadId.toString()))
        db.close()
    }

    fun actualizarCiudad(ciudad: Ciudad): Int {
        val db = this.writableDatabase
        try {
            val values = ContentValues().apply {
                put(COL_CIUDAD_NOMBRE, ciudad.nombre)
                put(COL_CIUDAD_POBLACION, ciudad.poblacion)
                put(COL_CIUDAD_ALTITUD, ciudad.altitud)
                put(COL_FECHA_FUNDACION, ciudad.fechaFundacion)
                put(COL_ES_CAPITAL, ciudad.esCapital)
                put(COL_LATITUD, ciudad.latitud)
                put(COL_LONGITUD, ciudad.longitud)
            }
            val result = db.update(
                TABLE_CIUDADES,
                values,
                "$COL_CIUDAD_ID = ?",
                arrayOf(ciudad.id.toString())
            )
            Log.d("UPDATE_CIUDAD", "Resultado: $result, Valores: $values")
            return result
        } catch (e: SQLiteException) {
            Log.e("UPDATE_CIUDAD", "Error al actualizar: ${e.message}")
            return -1
        } finally {
            db.close()
        }
    }
}
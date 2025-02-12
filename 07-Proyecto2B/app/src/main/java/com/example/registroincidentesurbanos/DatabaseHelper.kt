package com.example.registroincidentesurbanos

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "reportes.db"
        const val DATABASE_VERSION = 1

        // Tabla Reportes
        const val TABLE_REPORTES = "reportes"
        const val COL_ID = "id"
        const val COL_USUARIO_ID = "usuario_id"
        const val COL_TITULO = "titulo"
        const val COL_DESCRIPCION = "descripcion"
        const val COL_TIPO = "tipo"
        const val COL_FOTO_URI = "foto_uri"
        const val COL_LATITUD = "latitud"
        const val COL_LONGITUD = "longitud"
        const val COL_FECHA = "fecha"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_REPORTES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USUARIO_ID TEXT NOT NULL,
                $COL_TITULO TEXT NOT NULL,
                $COL_DESCRIPCION TEXT NOT NULL,
                $COL_TIPO TEXT NOT NULL,
                $COL_FOTO_URI TEXT NOT NULL,
                $COL_LATITUD REAL NOT NULL,
                $COL_LONGITUD REAL NOT NULL,
                $COL_FECHA TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REPORTES")
        onCreate(db)
    }

    fun insertarReporte(reporte: Reporte): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_USUARIO_ID, reporte.usuarioId)
            put(COL_TITULO, reporte.titulo)
            put(COL_DESCRIPCION, reporte.descripcion)
            put(COL_TIPO, reporte.tipo)
            put(COL_FOTO_URI, reporte.fotoUri)
            put(COL_LATITUD, reporte.latitud)
            put(COL_LONGITUD, reporte.longitud)
            put(COL_FECHA, reporte.fecha)
        }
        return db.insert(TABLE_REPORTES, null, values)
    }

    fun obtenerTodosReportes(): List<Reporte> {
        val reportes = mutableListOf<Reporte>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_REPORTES", null)

        while (cursor.moveToNext()) {
            reportes.add(
                Reporte(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    usuarioId = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_ID)),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO)),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPCION)),
                    tipo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIPO)),
                    fotoUri = cursor.getString(cursor.getColumnIndexOrThrow(COL_FOTO_URI)),
                    latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LATITUD)),
                    longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LONGITUD)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA))
                )
            )
        }
        cursor.close()
        return reportes
    }

    fun actualizarReporte(reporte: Reporte): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_TITULO, reporte.titulo)
            put(COL_DESCRIPCION, reporte.descripcion)
            put(COL_TIPO, reporte.tipo)
            put(COL_FOTO_URI, reporte.fotoUri)
            put(COL_LATITUD, reporte.latitud)
            put(COL_LONGITUD, reporte.longitud)
        }
        return db.update(
            TABLE_REPORTES,
            values,
            "$COL_ID = ?",
            arrayOf(reporte.id.toString())
        )
    }

    fun eliminarReporte(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(
            TABLE_REPORTES,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
    }
}
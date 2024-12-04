import java.util.*

fun main(args:Array<String>) {
    //INMUTABLES(No se Re asigna "=")
    val inmutable: String = "Adrian";
    //inmutable = "Vicente" //Error!
    //MUTABLES
    var mutable: String = "Vicente"
    mutable = "Adrian"
    //VAL > VAR


    //Duck Typing
    val ejemploVariable = "Pamela Cruz"
    ejemploVariable.trim()

    val edadEjemplo: Int = 12
    // ejemploVariable = edadEjemplo //Error!

    // Variables Primitivos
    val nombreProfesor: String = "Pamela Cruz"
    val suelto: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true

    //Clases en Java
    val fechanacimeinto: Date = Date()

    //when (switch)
    val estadoCivilWhen = "C"
    when (estadoCivilWhen) {
        ("C") -> {
            print("Casado")
        }

        "S" -> {
            println("Soltero")
        }

        else -> {
            println("No sabemos")
        }
    }
    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if (esSoltero) "Si" else "No" //if else chiquito

    imprimirNombre("Pamela")

    calcularSueldo(10.00) //solo parámetro requerido
    calcularSueldo(10.00, 15.00, 13.00)
    //parámetro requerido y sobreescribir parametros adicionales.
    //Named parameters
    //calcularSueldo(sueldo,tasa,bonoEspecial)
    calcularSueldo(10.00, bonoEspecial = 34.00)
    //usando el parámetro bonoEspecial en 2da posicion
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 15.00)
    //usando el parametro bonoEspecial en 1era posicion
    //usando el parámetro sueldo en 2da posicion
    //usando el parámetro tasa en 3era posicion


    //Utilizar las clases
    val sumaA = suma(1, 1)
    val sumaB = suma(null, 1)
    val sumaC = suma(1, null)
    val sumaD = suma(null, null)
    sumaA.sumar()
    sumaB.sumar()
    sumaC.sumar()
    sumaD.sumar()
    println(suma.pi)
    println(suma.elevarAlCuadrado(2))
    println(suma.historialSumas)

    //Arreglos
    //Estáticos
    val arregloEstatico: Array<Int> = arrayOf<Int>(1, 2, 3)
    println(arregloEstatico)
    //Binarios
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    )
    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)

    //for each => Unit
    //Iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico
        .forEach { valorActual: Int -> //=>
            println("Valor actual: ${valorActual}");
        }
    //"it" (en ingles "eso") significa el elemento iterado
    arregloDinamico.forEach { println("Valor Actual(it): ${it}") }

    //MAP -> MUTA (Modifica cambia) el arreglo
    //1) Enviamos el nuevo valor de la iteracion
    //2) Nos devuelve un NUEVO ARREGLO con valores de las iteraciones

    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }

    println(respuestaMap)
    val respuestaMapDos = arregloDinamico.map { it + 15 }
    println(respuestaMapDos)

    //FILTER -> Filtrar el ARREGLO
    //1) Devolver una expresion (TRUE o FALSE)
    //2) Nuevo arreglo FILTRADO
    val respuestaFilter: List<Int> = arregloDinamico
        .filter { valorActual:Int ->
            //Expresion o CONDICION
            val mayoresACinco: Boolean = valorActual > 5
            return@filter mayoresACinco
    }

    val respuestaFilterDos = arregloDinamico.filter {it <=5}
    println(respuestaFilter)
    println(respuestaFilterDos)

    //OR AND
    //OR -> ANY (Alguno cumple?)
    //And -> ALL (Todos cumplen?)
    val respuestaAny: Boolean = arregloDinamico
        .any{ valorrActual:Int ->
            return@any(valorrActual>5)
        }
    println(respuestaAny) //True

    val respuestaAll:Boolean =arregloDinamico
        .all {valorActual:Int->
            return@all (valorActual > 5)
        }
    println(respuestaAll)//False

    //REDUCE -> Valor acumulado
    //Valor acumulado = 0 (Siempre empieza en 0 en kotlin)
    //[1,2,3,4,5} -> Acumular "SUMAR" estos valores del arreglo
    // valorIteración1 = valorEmpieza + 1 =0 + 1 =1 ->Iteracion 1
    //valorIteración2 = valorAcumuladoIteracion1 + 2 = 1 + 2 = 3 ->Iteracion 1
    //valorIteración3 = valorAcumuladoIteracion2 + 3 = 3 + 3 = 6 ->Iteracion 1
    //valorIteración4 = valorAcumuladoIteracion3 + 4 = 6 + 4 = 10 ->Iteracion 1
    //valorIteración5 = valorAcumuladoIteracion4 + 5 = 10 + 5 = 15 ->Iteracion 1

    val respuestaReduce: Int = arregloDinamico
        .reduce{acumulado:Int, valorActual:Int ->
            return@reduce(acumulado+valorActual) // -> Cambiar o usar la logica de negocio
        }
    println(respuestaReduce)
    //return@reduce acumulado + (itemCarrito.cantidad * itemCarrito.precio)
}






    fun imprimirNombre(nombre: String): Unit {
        fun otraFuncionAdentro(): Unit {
            println("Otra funcion adentro")
        }
        //Template String
        println("Nombre: $nombre") //Uso sin llaves
        println("Nombre: ${nombre}") //Uso con llaves opcional
        println("Nombre: ${nombre + nombre}")//Uso con llaves (Concatenado)
        println("Nombre: ${nombre.toString()}")//Uso con llaves (funcion)
        println("Nombre: $nombre.toString()") //incorrecto, no se puede usar sin llaves

        otraFuncionAdentro()
    }

    fun calcularSueldo(
        sueldo: Double, //Requerido
        tasa: Double = 12.00, //Opcional(defecto)
        bonoEspecial: Double? = null //Opcional (nullable)
        //variable? -> "?" Es nullable(osea que puede en algun
        // momento ser nulo)
    ): Double {
        //Int -> Int? (nullable)
        //String -> String? (nullable)
        //Date -> Date? (nullable)
        if (bonoEspecial == null) {
            return sueldo * (100 / tasa)
        } else {
            return sueldo * (100 / tasa) * bonoEspecial
        }
    }

    abstract class NumerosJava {
        protected val numeroUno: Int
        private val numeroDos: Int

        constructor(
            uno: Int,
            dos: Int
        ) {
            this.numeroUno = uno
            this.numeroDos = dos
            println("Inicializando")
        }
    }

    abstract class Numeros(
        //Constructor Primario
        //Caso 1) Parametro normal
        //uno:Int, (parametro (sin modificador acceso))

        //Caso 2) Parametro y propiedad (Atributo)(protected)
        //private var uno:Int (propiedad "instancia.uno")
        protected val numeroUno: Int, //instancia.numeroUno
        protected val numeroDos: Int, //instancia.numeroDos
        parametroNoUsadoNoPropiedadDeLaClase: Int? = null
    ) {
        init { //bloque constructor primario OPCIONAL
            this.numeroUno
            this.numeroDos
            println("Inicializando")
        }
    }

    class suma(
//constructor primario
        unoParametro: Int, //parametro
        dosParametro: Int, //parametro
    ) : Numeros( //Clase papa, Numeros(extendiendo
        unoParametro,
        dosParametro
    ) {
        public val soyPublicExplicito: String = "Publicas"
        val soyPublicoImplicite: String = "Publico implicito"

        init { //bloque de constructor primario
            this.numeroUno
            this.numeroDos
            numeroUno //this es OPCIONAL [propiedades,métodos]
            numeroDos //this es OPCIONAL [propiedades,métodos]
            this.soyPublicExplicito
            soyPublicExplicito
        }


        constructor(
            //constructor secundario
            uno: Int?, //Entero nulleable
            dos: Int,
        ) : this(
            if (uno == null) 0 else uno,
            dos
        ) {
            //bloque codigo constructor secundario
        }

        constructor(
            //constructor secundario
            uno: Int,
            dos: Int?, //Entero nulleable
        ) : this(
            uno,
            if (dos == null) 0 else dos
        )

        constructor(
            //constructor secundario
            uno: Int?, //Entero nulleable
            dos: Int?, //Entero nulleablr
        ) : this(
            if (uno == null) 0 else uno,
            if (dos == null) 0 else dos
        )

        fun sumar(): Int {
            val total = numeroUno + numeroDos
            agregarHistorial(total)
            return total
        }

        companion object { //Comparte entre todas las instancias, similar al STATIC
            //funciones, variables
            //NombreClase.metodo, NombreClase.funcion =>

            //Suma.pi
            val pi = 3.14

            //Suma.elavarAlCuadrado
            fun elevarAlCuadrado(num: Int): Int {
                return num * num
            }

            val historialSumas = arrayListOf<Int>()

            fun agregarHistorial(valorTotalSuma: Int) { // Suma.agregarHistorial
                historialSumas.add(valorTotalSuma)
            }
        }

    }



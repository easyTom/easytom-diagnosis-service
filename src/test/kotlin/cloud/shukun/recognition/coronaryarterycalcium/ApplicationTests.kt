package cloud.shukun.recognition.coronaryarterycalcium

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.properties.Delegates


fun printColoredText(text: String) {
    val coloredText = "\u001B[34m$text\u001B[0m"
    println(coloredText)
}

class ApplicationTests {


    private val lazyValue: String by lazy {
        println("computed!")
        "Hello"
    }

    @Test
    fun lazyT() {

        printColoredText("init ------------------------")
        println(lazyValue)
        println(lazyValue)
        println(lazyValue)
        printColoredText("over ------------------------")
    }

    class User {
        var name: String by Delegates.observable("默认值") { _, old, new ->
            printColoredText("$old -> $new ")
        }

        var age: Int by Delegates.vetoable(0) { _, oldValue, newValue ->
            if (newValue > oldValue) true else throw IllegalArgumentException("New value must be larger than old value.")
        }
    }

    @Test
    fun observableT() {
        val user = User()
        user.name = "first"
        user.name = "second"
        user.age = 10
        user.age = 5
    }


    @Test
    fun de() {
        val files = File("Test").listFiles()
        printColoredText(files?.size.toString())
    }

    @Test
    fun hh() {
        val text = """
|Tell me and I forget.
|Teach me and I remember.
|Involve me and I learn.
|(Benjamin Franklin)
    """.trimMargin()
        println(text)

        for (i in 1..3) {
            println(i)
        }
        for (i in 6 downTo 0 step 2) {
            println(i)
        }
    }


    private fun foo() {
        listOf(1, 2, 3, 4, 5).forEach lit@{
            if (it == 3) return@lit // 局部返回到该 lambda 表达式的调用者——forEach 循环
            printColoredText(it.toString())
        }
        printColoredText(" done with explicit label")
    }

    @Test
    fun hh2(){
        foo()
    }
}

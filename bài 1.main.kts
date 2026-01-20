
println("this is the text to print!")
// This is comment line.
//this is another comment.

// Assign once, cannot change.
val age = "5"
val name ="rover"

//Assign and change add needed
var roll = 6
var rikkedValue: Int = 4
println("You are akready ${age}!")
println("Youo ate already ${age} dáy old, ${name}!")

//define the function.
fun printHello (){
    println("Hello Kotlin")
}

//call the function
printHello()

fun printBorder(border: String, timesToRepeat: Int){
    repeat(timesToRepeat){
        print(border)
    }
    println()
}

fun roll(): Int {
    val randomNumber = (1..6).random()
    return randomNumber
}
fun printBorder() {
    repeat(23) {
        print("=")
    }
}

fun printCakeBottom(age: Int, layers: Int) {
    repeat(layers) {
        repeat(age + 2) {
            print("@")
        }
        println()
    }
}

fun main() {
    val num = 4
    if (num > 4) {
        println("The variable is greater than 4")
    } else if (num == 4) {
        println("The variable is equal to 4")
    } else {
        println("The variable is less than 4")
    }
}

main()


class Dice {
    var sides = 6

    fun roll() {
        val randomNumber = (1..6).random()
        println(randomNumber)
    }
}

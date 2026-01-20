val numbers = listOf(1,2,3,4,5,6)
numbers.size
numbers[0]

listOf("red", "blue","green" ).reversed()
val entrees = mutableListOf<String>()
entrees.add("spaghetti")
 entrees[0] = "lasagna"
entrees.remove("lasagna")

for (element in numbers) {
    // Perform an operation with each item
    println(element)
}

var index = 0
while (index < numbers.size) {
    println(numbers[index])
    index++
}

val name = "Android"
println(name.length)

val number = 10
val groups = 5
println("${number * groups} people")


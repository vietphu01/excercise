suspend fun getValue(): Double {
    // long running work or calls to other suspend functions
}
GlobalScope.launch {
    val output = getValue()
}

suspend fun processValue() {
    val value = getValue()
    // modify the value
}
val job: Job = GlobalScope.launch {
    val output = getValue()
}
job.cancel()

runBlocking {
    val output = getValue()
}
runBlocking {
    val output = await { getValue() }

    println("Output is ${output.await()}")
}
object DataProviderManager {

}try {
    // code that may throw an error
} catch (exception: Exception) {
    // handle the thrown exception
}enum class Direction {
    NORTH, SOUTH, WEST, EAST
}val dire
when (direction) {
    Directon.NORTH -> {

    }
    Direction.SOUTH -> {

    }
    Direction.WEST -> {

    }
    Direction.EAST -> {

    }
}ction = Direction.NORTH
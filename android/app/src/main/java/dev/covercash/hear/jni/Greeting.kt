package dev.covercash.hear.jni

class Greeting {
    private external fun greeting(pattern: String): String

    fun sayHello(to: String): String {
        return greeting(to)
    }

    companion object {
        init {
            try {
                System.getenv().mapValues { println("value: $it") }
                System.loadLibrary("hear")
            } catch (e: UnsatisfiedLinkError) {
                println("unable to load jni lib libhear.so\n$e")
            }
        }
    }
}
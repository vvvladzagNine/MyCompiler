import java.lang.Exception
import java.lang.RuntimeException
import java.util.*

class Analizator(val table: Map<Pair<String, String>, Pair<String, String>>) {
    var input = mutableListOf<String>()
    var stack = Stack<String>()
    var output = Pair<String, String>("","")

    fun go(){
        input= mutableListOf("i","+","i","*","i","$")
        //input= mutableListOf(")","i","+","*","i","$")
        stack.push("$")
        stack.push("E")

        println("stack $stack")
        println("input $input")
        println("  \\/")
        while(true) {

            val poped = stack.pop()
            var tokenFromInput = input.first()
            if(poped=="$" && tokenFromInput=="$") break
            output = table[Pair(poped,tokenFromInput)]!!

            var error = false
            var errorUntilEnd = false

            if(output.first=="Synch" || output.first=="NotSynch") {
                println("Error symbol $tokenFromInput")
                error = true
                stack.push(poped)
                while(true){
                    input.removeAt(0)
                    if(output.first=="Synch") {
                        println("synchronising symbol $tokenFromInput")
                        println("  \\/")
                        if(tokenFromInput=="$") errorUntilEnd = true
                        break
                    }
                    println("skip symbol $tokenFromInput")
                    tokenFromInput = input.first()
                    output = table[Pair(poped,tokenFromInput)]!!
                    continue
                }

            }

            if (errorUntilEnd) break
            if (error) continue

            val reversed =output.second.reversed().trim().split("")
            for(token in reversed){
                if(token != "$" && token !=" " && token !="")
                    stack.push(token)
                if(token == tokenFromInput && token!="$") {
                    input.removeAt(0)
                    stack.pop()
                }
            }
            println("stack $stack")
            println("input $input")
            println("output $output")
            println("  \\/")
        }
        println("__END__")
    }

}
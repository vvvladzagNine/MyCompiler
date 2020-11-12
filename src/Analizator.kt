import java.util.*

class Analizator(val table: Map<Pair<String, String>, Pair<String, String>>) {
    var input = mutableListOf<String>()
    var stack = Stack<String>()
    var output = Pair<String, String>("","")

    fun go(){
        input= mutableListOf("i","+","i","*","i","$")
        stack.push("$")
        stack.push("E")

        while(true) {
            println("stack $stack")
            println("input $input")
            val poped = stack.pop()
            val tokenFromInput = input.first()
            if(poped=="$" && tokenFromInput=="$") break;
            output = table[Pair(poped,tokenFromInput)]!!
            val reversed =output.second.reversed().split("")
            for(token in reversed){
                if(token != "$")
                    stack.push(token)
            }
        }


    }

}
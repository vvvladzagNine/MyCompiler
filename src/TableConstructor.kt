import java.lang.Exception
import java.lang.RuntimeException

class TableConstructor(replacings: Set<MagazineReplacing>) {
    var FIRST = mutableMapOf<String,Set<String>>()
    var FOLLOW = mutableMapOf<String,Set<String>>()
    var rules: Map<String, List<String>>
    val table = mutableMapOf<Pair<String,String>, Pair<String, String>>()

    private val magazineAvailableSymbols = "QWERTYUIOPASDFGHJKLZXCVBNM"

    init {
        rules = replacings.associateBy({ it.read }, { it.write })
    }

    fun FIRST(token: String): Set<String> {
        if(token.length == 1) {
            if(!magazineAvailableSymbols.contains(token)) {
                return setOf(token)
            } else {
                var result = mutableSetOf<String>()
                try {
                    for(end in rules.getValue(token)) {
                        val new = result union FIRST(end)
                        result = new.toMutableSet()
                    }
                    return result
                } catch (e:Exception){
                    println("В rules нет $token")
                    println(rules)
                    e.printStackTrace()
                    throw RuntimeException()
                }
            }
        } else {
            var result = mutableSetOf<String>()
            for(t in token) {
                val new = result union FIRST(t.toString())
                result = new.toMutableSet()

                val derives = magazineAvailableSymbols.contains(t) && rules[t.toString()]!!.contains("ε")
                if (!derives) break
            }
            return result
        }
    }

    fun constructTable() {
        for(NonT in rules.keys) {
            for(end in rules[NonT]!!) {
                val first = FIRST(end)
                for(token in first){
                    if(!magazineAvailableSymbols.contains(token)) {
                        table[Pair(NonT,token)] = Pair(NonT,end)
                    }
                }

                if(first.contains("$")){
                    val follow = FOLLOW[NonT]
                    for(token in follow!!){
                        if(!magazineAvailableSymbols.contains(token)) {
                            table[Pair(NonT,token)] = Pair(NonT,end)
                        }
                    }
                    if(first.contains("$") && follow.contains("$")) {
                        table[Pair(NonT,"$")] = Pair(NonT,end)
                    }
                }
            }
        }
        for(entry in FOLLOW) {
            for(end in entry.value) {
                if(table[Pair(entry.key,end)] == null)
                    table[Pair(entry.key,end)] = Pair("Synch","Synch")
            }
        }
    }


    fun constructFIRST() {
        for(NonT in rules.keys){
            FIRST[NonT] = mutableSetOf()
            val new = FIRST[NonT]!! union FIRST(NonT)
            FIRST[NonT] = new.toMutableSet()

        }
    }

    fun printFIRST() {
        println("\n\n...FIRST...")
        for(f in FIRST){
            println(f)
        }
        println("============")
    }

    fun printTable() {
        println("\n\n...TABLE...")
        for(f in table){
            println(f)
        }
        println("============")
    }
}
import java.lang.Exception
import java.lang.RuntimeException

class TableConstructor(replacings: Set<MagazineReplacing>,firstItem: String) {
    var FIRST = mutableMapOf<String,Set<String>>()
    var FOLLOW = mutableMapOf<String,MutableSet<String>>()
    val rules: Map<String, List<String>>
    val table = mutableMapOf<Pair<String,String>, Pair<String, String>>()
    val firstItem: String
    var currentFollowUpRule = "null";
    var isStartTaken = false
    val terminals: Set<String>

    private val magazineAvailableSymbols = "QWERTYUIOPASDFGHJKLZXCVBNM"

    init {
        rules = replacings.associateBy({ it.read }, { it.write })
        terminals = rules.
            values.
            flatten().
            flatMap{it.toCharArray().toList()}.
            filter{!magazineAvailableSymbols.contains(it)}.
            map{it.toString()}.
            toSet()
        this.firstItem = firstItem
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

                val derives = magazineAvailableSymbols.contains(t) && rules[t.toString()]!!.contains("$")
                if (!derives) break
            }
            return result
        }
    }

    //region depricated follow
    fun follow(rule: String) : MutableSet<String> {
        var rulesSet = getFollowUps(rule)
        var result = mutableSetOf<String>()
        if (rule.equals(firstItem)) {
            result.add("$")
        }
        if (!magazineAvailableSymbols.contains(rule)) {
            result.add(rule)
            return result
        }
        if (!rule.isEmpty()) {
            rulesSet.forEach { pair ->
                var firItem = getNextFollowUp(rule, pair)
                if (firItem.isEmpty()) {

                } else if (!magazineAvailableSymbols.contains(firItem)) {
                    result.add(firItem)
                } else {
                    var new = setOf<String>()
                    if (isStartTaken) {
                        new = result union follow(firItem)
                    } else {
                        if (FIRST[firItem]!!.contains("$")) {
                            var newnew = FIRST[firItem]!!.toMutableSet()
                            newnew.remove("$")
                            var nextItem = getNextFollowUp(firItem, pair)
                            new = newnew union follow(getNextFollowUp(firItem, pair))
                        } else {
                            new = result union FIRST[firItem]!!.toMutableSet()
                        }
                    }
                    var newnew = result union new
                    result = newnew.toMutableSet()
                }
            }
        }
        return result

    }
    //endregion

    fun followWithInner(rule: String) : MutableSet<String> {
        var rulesSet = getFollowUps(rule)
        var result = mutableSetOf<String>()
        if (rule.equals(firstItem)) {
            result.add("$")
        }
        if (!magazineAvailableSymbols.contains(rule)) {
            result.add(rule)
            return result
        }
        if (!rule.isEmpty()) {
            rulesSet.forEach { pair ->
                var new = result union followInner(getNextFollowUp(rule, pair), pair, result)
                result =  new.toMutableSet()
            }
        }
        return result

    }

    fun followInner(rule: String, pair: Pair<String, String>, result: MutableSet<String>): MutableSet<String> {
        var innerResult = result;
        var firItem = rule
        if (firItem.isEmpty()) {

        } else if (!magazineAvailableSymbols.contains(firItem)) {
            innerResult.add(firItem)
        } else {
            var new = setOf<String>()
            if (isStartTaken) {
                new = innerResult union followWithInner(firItem)
            } else {
                if (FIRST[firItem]!!.contains("$")) {
                    var newnew = FIRST[firItem]!!.toMutableSet()
                    newnew.remove("$")
                    new = newnew union followInner(getNextFollowUp(firItem, pair), pair, innerResult)
                } else {
                    new = innerResult union FIRST[firItem]!!.toMutableSet()
                }
            }
            var newnew = innerResult union new
            innerResult = newnew.toMutableSet()
        }
        return innerResult
    }




    fun constructFollow() {
        rules.forEach{
                FOLLOW.put(it.key, mutableSetOf())
        }

        rules.forEach{k,v ->
            currentFollowUpRule = k
            FOLLOW[k] = followWithInner(k)
        }
    }

    /**
     * Функция выводит рулы, где item находится в end
     */
    fun getFollowUps(item: String): MutableSet<Pair<String, String>> {
        val result = mutableSetOf<Pair<String, String>>()
        rules.forEach{ k,v ->
            v.forEach() {
                if (it.contains(item)) {
                    result.add(Pair(k, it))
                }
            }
        }
        return result
    }

    /**
     * Находит следующий элемент в end секции правила
     */
    fun getNextFollowUp(item: String, pair: Pair<String, String> ): String {
        isStartTaken = false
        if (pair.second.indexOf(item).equals(pair.second.lastIndex)) {
            isStartTaken = true
            return if (!pair.first.equals(currentFollowUpRule) && !pair.first.equals(pair.second.last().toString())) pair.first else ""
        }
        var result = pair.second.toCharArray()[pair.second.indexOf(item) + 1].toString()
        return result
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
        for(nonT in rules.keys) {
            for (terminal in terminals) {
                if(table[Pair(nonT,terminal.toString())] == null) {
                    table[Pair(nonT, terminal.toString())] = Pair("NotSynch","NotSynch")
                }
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

    fun printRules() {
        println("\n\n...RULES...")
        for(f in rules){
            println(f)
        }
        println("============")
    }
}
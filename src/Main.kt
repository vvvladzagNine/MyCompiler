fun main(){
    val rules = FileParcer.read("files/file2.txt")
    val tableConstructor = TableConstructor(rules)
    print(tableConstructor.terminals)
    tableConstructor.constructFIRST()
    tableConstructor.printFIRST()
    tableConstructor.FOLLOW = mutableMapOf(
        "E" to setOf(")","$"),
        "A" to setOf(")","$"),
        "T" to setOf(")","$","+"),
        "B" to setOf(")","$","+"),
        "F" to setOf(")","$","+","*")

    )
    tableConstructor.constructTable()
    tableConstructor.printTable()
    val anal = Analizator(tableConstructor.table)
    anal.go()
}
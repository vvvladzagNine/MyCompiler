fun main(){
    val rules = FileParcer.read("files/file4.txt")
    val firstItem = FileParcer.getFirstItem("files/file4.txt")
    val tableConstructor = TableConstructor(rules, firstItem)
    tableConstructor.printRules()
    println("\n\n")
    println(tableConstructor.terminals)

    tableConstructor.constructFIRST()
    tableConstructor.printFIRST()
    tableConstructor.constructFollow();
    println(tableConstructor.FOLLOW)
//    tableConstructor.FOLLOW = mutableMapOf(
//        "E" to mutableSetOf(")","$"),
//        "A" to mutableSetOf(")","$"),
//        "T" to mutableSetOf(")","$","+"),
//        "B" to mutableSetOf(")","$","+"),
//        "F" to mutableSetOf(")","$","+","*")
//    )
//    tableConstructor.constructTable()
//    tableConstructor.printTable()
//    val anal = Analizator(tableConstructor.table)
//    anal.go()
}
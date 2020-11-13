fun main(){
    val rules = FileParcer.read("files/file2.txt")
    val firstItem = FileParcer.getFirstItem("files/file2.txt")
    val tableConstructor = TableConstructor(rules, firstItem)
    tableConstructor.printRules()
    println("\n\n")
    tableConstructor.constructTable()

    tableConstructor.printFIRST()

    tableConstructor.printFOLLOW()

    tableConstructor.printTable()

    val analizator = Analizator(tableConstructor.table)
    //"i","+","i","*","i","$"
    analizator.go(")i+i+*i$")
}
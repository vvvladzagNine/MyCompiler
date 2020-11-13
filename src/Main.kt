fun main(){
//    val path = "files/fileLightCSimple.txt"
    val path = "files/file2.txt"
    val rules = FileParcer.read(path)
    val firstItem = FileParcer.getFirstItem(path)
    val tableConstructor = TableConstructor(rules, firstItem)
    tableConstructor.printRules()
    tableConstructor.constructTable()

    tableConstructor.printFIRST()

    tableConstructor.printFOLLOW()

    tableConstructor.printTable()

    val analizator = Analizator(tableConstructor.table)
    analizator.go(")i+i+*i$")
//    analizator.go("tm(){to=5}")
}
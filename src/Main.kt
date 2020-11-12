fun main(){
    val rules = FileParcer.read("files/file2.txt")
    val tableConstructor = TableConstructor(rules)
    tableConstructor.constructFIRST()
    tableConstructor.printFIRST()
}
object InputTapeHandler {

    private val magazineAvailableSymbols = "QWERTYUIOPASDFGHJKLZXCVBNM"

    fun getInputTapeSymbols(set: Set<MagazineReplacing>): Set<Char> {
        var inputAlphabet = mutableSetOf<Char>()
        set.forEach {
            it.write.forEach { string ->
                string.forEach { char ->
                    if(!magazineAvailableSymbols.contains(char)) {
                        inputAlphabet.add(char)
                    }
                }
            }
        }
        return inputAlphabet.toSet()
    }
}
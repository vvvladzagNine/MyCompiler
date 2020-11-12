import java.io.File

object FileParcer {

    fun read(path: String): Set<MagazineReplacing> {
        var setOfInputs = mutableSetOf<MagazineReplacing>()
        File(path).forEachLine {
            setOfInputs.add(parceAndValidate(it))
        }
        return setOfInputs
    }

    private fun parceAndValidate(string: String):MagazineReplacing {
        val readed = string.substringBefore(">")
        val writed = string.substringAfter(">").split("|")
        return MagazineReplacing(readed,writed)
    }

    fun getFirstItem(path: String): String {
        return File(path).readLines()[0].substringBefore(">")
    }
}
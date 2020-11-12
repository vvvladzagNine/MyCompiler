data class MagazineReplacing(val read: String, val write: List<String>) {

    override fun toString(): String {
        if(write.size==1){
            return "$read>$write[0]"
        } else {
            return "$read>$write"
        }

    }
}
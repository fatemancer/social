package info.hauu.highloadsocial.model

data class SafeLoginRequest(val id: String, val password: String) {
    override fun toString(): String {
        return "SafeLoginRequest(id='$id',password='***')"
    }
}
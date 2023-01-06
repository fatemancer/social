package info.hauu.highloadsocial.model

import io.swagger.v3.oas.annotations.media.Schema

data class UserInternal(

    val id: String,

    @Schema(example = "Имя", description = "")
    val firstName: String,

    @Schema(example = "Фамилия", description = "")
    val secondName: String,

    @Schema(example = "18", description = "")
    val age: Int? = null,

    @Schema(example = "Хобби, интересы и т.п.", description = "")
    val biography: String? = null,

    @Schema(example = "Москва", description = "")
    val city: String? = null,

    @Schema(example = "Секретная строка", description = "")
    val password: String

) {
    override fun toString(): String {
        return "SafeUserRegisterPostRequest(id=$id, firstName='$firstName', secondName='$secondName', age=$age, biography=$biography, city=$city)"
    }
}
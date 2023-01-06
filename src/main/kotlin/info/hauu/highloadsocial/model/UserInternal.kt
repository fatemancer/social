package info.hauu.highloadsocial.model

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class UserInternal(

    val id: String,

    @Schema(example = "Имя", description = "")
    val firstName: kotlin.String,

    @Schema(example = "Фамилия", description = "")
    val secondName: kotlin.String,

    @Schema(example = "18", description = "")
    val age: kotlin.Int? = null,

    @Schema(example = "Хобби, интересы и т.п.", description = "")
    val biography: kotlin.String? = null,

    @Schema(example = "Москва", description = "")
    val city: kotlin.String? = null,

    @Schema(example = "Секретная строка", description = "")
    val password: kotlin.String

) {

    override fun toString(): String {
        return "SafeUserRegisterPostRequest(id=$id, firstName='$firstName', secondName='$secondName', age=$age, biography=$biography, city=$city)"
    }
}
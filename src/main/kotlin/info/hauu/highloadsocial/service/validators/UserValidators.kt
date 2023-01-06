package info.hauu.highloadsocial.service.validators

import org.openapi.model.LoginPostRequest
import org.openapi.model.UserRegisterPostRequest

private const val PASSWORD = "No user password"
private const val FIRST_NAME = "No first name"
private const val USER_ID = "No user id"

fun hasPassword(req: UserRegisterPostRequest?): String? = if (req?.password == null) { PASSWORD } else { null }
fun hasId(req: UserRegisterPostRequest?): String? = if (req?.firstName == null) { FIRST_NAME} else { null }

fun hasPassword(req: LoginPostRequest?): String? = if (req?.password == null) { PASSWORD } else { null }
fun hasId(req: LoginPostRequest?): String? = if (req?.id == null) { USER_ID } else { null }

class Constraints private constructor(val constraints: List<String>) {

    constructor(vararg cs: String?): this(listOfNotNull(cs).map { it.toString()})

    fun test() {
        if (constraints.isEmpty()) {
            return
        } else {
            throw java.lang.IllegalArgumentException("Broken constraints: {}".format(constraints))
        }
    }
}

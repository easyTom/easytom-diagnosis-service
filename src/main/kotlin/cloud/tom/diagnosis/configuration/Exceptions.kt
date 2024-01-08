package cloud.tom.diagnosis.configuration

import java.util.*


sealed class Exceptions(type: String, locationUuid: UUID, message: String) : RuntimeException(message) {

    class RequestNullException(locationUuid: String, message: String = "Request Is Null") : Exceptions(
        type = "RequestNullException", message = message, locationUuid = UUID.fromString(locationUuid)
    )

    class StateNullException(locationUuid: String, message: String = "State Is Null") : Exceptions(
        type = "StateNullException", message = message, locationUuid = UUID.fromString(locationUuid)
    )

    class DiagnosisNotFoundException(locationUuid: String, message: String = "Diagnosis Not Found") : Exceptions(
        type = "DiagnosisNotFoundException", message = message, locationUuid = UUID.fromString(locationUuid)
    )
}
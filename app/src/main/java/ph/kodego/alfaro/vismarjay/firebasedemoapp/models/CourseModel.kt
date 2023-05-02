package ph.kodego.alfaro.vismarjay.firebasedemoapp.models

data class CourseModel(
    var courseId:String? = null,
    var courseTitle: String? = null,
    var program: String? = null,
    var weeklySchedule: String? = null,
    var timeSchedule: String? = null,
    var roomAssignment:String? = null,
    var instructor:String? = null
)
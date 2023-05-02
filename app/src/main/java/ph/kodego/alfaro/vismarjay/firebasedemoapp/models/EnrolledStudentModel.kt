package ph.kodego.alfaro.vismarjay.firebasedemoapp.models

data class EnrolledStudentModel (
    var studentId: String? = null,
    var lastName:String? = null,
    var firstName:String? = null,
    var program:String? = null){

    var course: ArrayList<CourseModel> = arrayListOf()
    var midActivities: ArrayList<ActivityListModel> = arrayListOf()
}
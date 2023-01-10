package com.b18dccn562.phonemanager.common

object Constants {

    const val ONE_DAY_MILLISECOND_COUNT: Long = 1000 * 60 * 60 * 24
    const val NOTIFICATION_CHANNEL_ID = "notification_channel"

    var launcherName = ""

    var classId: Long = -1

    const val pattern = "saved_pattern"

    object ApiReferences {

        const val REQUEST_REFERENCE = "api/request/"
        const val GET_MY_REQUEST = "get_my_requests"
        const val ACCEPT_REQUEST = "accept_request"
        const val DENY_REQUEST = "deny_request"

        const val CLASS_REFERENCE = "api/class/"
        const val CREATE_CLASS = "create_class"
        const val GET_TEACHER_CLASSES = "get_classes"
        const val GET_STUDENT_CLASSES = "get_student_classes"
        const val GET_STUDENT_IN_CLASS = "get_student_in_class"
        const val SEARCH_CLASS = "search_class"
        const val REQUEST_JOIN_CLASS = "join_class"
        const val ACCEPT_JOIN_CLASS = "accept_join_class"
        const val DENY_JOIN_CLASS = "deny_join_class"
        const val REMOVE_FROM_CLASS = "remove_from_class"
        const val BAN_FROM_CLASS = "banned_from_class"
        const val GET_CLASS_REQUEST = "get_class_request"
        const val GET_USER_APP_SETTING = "get_user_setting"
        const val UPLOAD_USER_APP_SETTINGS = "upload_user_settings"
        const val UPLOAD_USER_APP_SETTING = "upload_user_setting"
        const val UPLOAD_APP_FOR_DATABASE_CHECK = "upload_app_for_database_check"

        const val BASE_URL = "http://192.168.0.3:8081/"
//        const val BASE_URL = "http://192.168.238.212:8081/"

        const val USER_REFERENCE = "api/user/"
        const val CREATE_ACCOUNT = "create_account"
        const val CREATE_SUB_ACCOUNT = "create_sub_account"
        const val CHANGE_AVATAR = "change_avatar"
        const val LOGIN = "sign_in"
        const val GET_AVATAR = "get_user_image"
        const val UPDATE_USER_INFORMATION = "update_user_information"
        const val GET_PARTNER_INFO = "get_partner_info"
        const val GET_CHILDREN_INFO = "get_all_children"
        const val GET_APP_ICON = "get_app_icon"

        const val APP_REFERENCE = "api/app/"
        const val UPLOAD_APP = "insert_new_app"
        const val UPLOAD_APP_USAGE = "upload_app_usage"
        const val GET_APP_USAGE = "get_user_app_usage"
        const val UPLOAD_APP_USAGE_VIOLATION = "upload_violation"
        const val GET_VIOLATION = "get_violation"

        const val SEARCH_CHILDREN = "search_children"
        const val SEARCH_PARTNER = "search_partner"
        const val ADD_PARTNER = "add_partner"
        const val ADD_CHILDREN = "add_child"
        const val ACTION_ADD_PARTNER_REQUEST = "action_request"

        const val IMAGE_REFERENCE = "api/image"

        const val NOTIFICATION_REFERENCE = "api/notification/"
        const val GET_NOTIFICATION = "get_user_notifications"

    }

    object ResponseCode {
        const val SUCCESS = 200
        const val ERROR = 201
        const val EMPTY = 202
        const val NEED_IMAGE = 203
    }

    object Preference {
        const val PREF = "user_pref"
        const val EMAIL = "email"
        const val PASSWORD = "password"
    }

    object Role {
        const val PARENT = 3
        const val TEACHER = 2
        const val CHILD = 5
    }

    object RequestType {
        const val REQUEST_ADD_PARTNER = 0
        const val ACCEPT_ADD_PARTNER = 1
        const val REJECT_ADD_PARTNER = 2

        const val REQUEST_ADD_CHILD = 3
    }

    object NotificationType {
        const val REQUEST_PARTNER = 1
        const val REQUEST_PARTNER_RESPONSE = 2
        const val REQUEST_KID = 100
    }

    object FirebaseReference {
        const val CHANGE_LIMIT = "change_limit"
        const val HAS_NOTIFICATION = "has_notification"
    }

    object AppUsageQueryType {
        const val TODAY = 1
        const val LAST_7_DAY = 2
        const val LAST_MONTH = 3
        const val ALL = 4
    }

    object AppActions {
        const val OPEN_APP = 1
        const val CLOSE_APP = 2
    }

}
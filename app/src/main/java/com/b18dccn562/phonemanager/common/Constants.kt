package com.b18dccn562.phonemanager.common

object Constants {

    const val ONE_DAY_MILLISECOND_COUNT: Long = 1000 * 60 * 60 * 24
    const val NOTIFICATION_CHANNEL_ID = "notification_channel"

    var launcherName = ""

    var classId: Long = -1

    const val pattern = "saved_pattern"

    object Service {
        const val CHANNEL_ID = "com.b18dccn562.finalproject"
        const val NAME = "AppLock"
    }

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
        const val JOIN_CLASS = "join_class"

        const val BASE_URL = "http://192.168.0.5:8081/"
//        const val BASE_URL = "http://192.168.238.212:8081/"

        const val USER_REFERENCE = "api/user/"
        const val CREATE_ACCOUNT = "create_account"
        const val CREATE_SUB_ACCOUNT = "create_sub_account"
        const val LOGIN = "sign_in"
        const val GET_AVATAR = "get_user_image"
        const val UPDATE_USER_INFORMATION = "update_user_information"
        const val GET_PARTNER_INFO = "get_partner_info"
        const val GET_CHILDREN_INFO = "get_all_children"
        const val GET_APP_ICON = "get_app_icon"

        const val APP_REFERENCE = "api/app/"
        const val UPLOAD_APP = "insert_new_app"
        const val GET_ALL_APP = "get_all_app_status"
        const val REQUEST_LOCK_LIMIT = "lock_app"
        const val UPLOAD_APP_USAGE = "upload_app_usage"
        const val UPLOAD_APP_SETTING = "upload_app_setting"
        const val GET_APP_USAGE = "get_user_app_usage"
        const val UPLOAD_APP_USAGE_VIOLATION = "upload_violation"
        const val GET_VIOLATION = "get_violation"

        const val GET_USER = "get_user_info"
        const val SEARCH_CHILDREN = "search_children"
        const val SEARCH_PARTNER = "search_partner"
        const val ADD_PARTNER = "add_partner"
        const val ADD_CHILDREN = "add_child"
        const val ACTION_ADD_PARTNER_REQUEST = "action_request"

        const val IMAGE_REFERENCE = "api/image"
        const val CHANGE_AVATAR = "change_avatar"
        const val CHANGE_USERNAME = "change_username"
        const val CHANGE_PASSWORD = "change_password"

        const val NOTIFICATION_REFERENCE = "api/notification/"
        const val GET_NOTIFICATION = "get_user_notifications"
        const val CREATE_NOTIFICATION = "create_notification"

    }

    object ResponseCode {
        const val SUCCESS = 200
        const val ERROR = 201
        const val EMPTY = 202
        const val NEED_IMAGE = 203
    }

    object Preference {
        const val AVATAR_URL = "avatar"
        const val USER_ROLE = "role"
        const val USERNAME: String = "username"
        const val PREF = "user_pref"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val ACCESS_TOKEN = "access_token"
        const val SUPER_USER = "super_user"
        const val UID = "uid"
    }

    object Role {
        const val PERSONAL = 1
        const val PARENT = 3
        const val TEACHER = 2
        const val BOSS = 4
        const val CHILD = 5
        const val STUDENT = 6
        const val EMPLOYEE = 7
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
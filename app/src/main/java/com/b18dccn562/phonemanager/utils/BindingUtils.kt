package com.b18dccn562.phonemanager.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import com.b18dccn562.phonemanager.network.dto.NotificationDTO
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.user_report.getAppIcon

@BindingAdapter("load_app_image")
fun setup(imageView: ImageView, appIcon: Drawable) {
    loadImage(imageView, appIcon)
}

@BindingAdapter("load_lock_icon")
fun loadLockIcon(imageView: ImageView, itemApp: ItemApp) {
    if (itemApp.isLock) {
        loadImage(imageView, R.drawable.ic_lock)
    } else {
        loadImage(imageView, R.drawable.ic_unlock)
    }
}

@BindingAdapter("load_limit_icon")
fun loadLimitIcon(imageView: ImageView, itemApp: ItemApp) {
    if (itemApp.timeLimited >= 0L) {
        loadImage(imageView, R.drawable.ic_limited)
    } else {
        loadImage(imageView, R.drawable.ic_unlimited)
    }
}

@BindingAdapter("show_app_usage")
fun loadAppUsage(textView: TextView, timeUsed: Long) {
    val timeUsedString = timeUsed.toString()
    textView.text = timeUsedString
}

@BindingAdapter("load_avatar")
fun loadAvatar(imageView: ImageView, email: String?) {
    if (email != null)
        loadImage(imageView, getUserAvatarImage(email))
}

@BindingAdapter("load_app_image_from_internet")
fun loadAppImageFromInternet(imageView: ImageView, appIcon: String?) {
    if (appIcon != null)
        loadImage(imageView, appIcon)
}

@BindingAdapter("load_user_name")
fun loadUsername(textView: TextView, name: String?) {
    if (name != null) {
        textView.text = textView.context.getString(R.string.greeting_user, name)
    }
}
//
//@BindingAdapter("load_class_name")
//fun loadClassName(textView: AppCompatTextView, classEntity: ClassEntity) {
//    textView.text = textView.context.getString(R.string.class_name, classEntity.className)
//}

@BindingAdapter("load_class_name")
fun loadClassname(textView: TextView, itemClass: ClassDTO) {
    val classname = textView.context.getString(R.string.get_classname, itemClass.classname)
    textView.text = classname
}

@BindingAdapter("load_class_teacher_name")
fun loadClassTeacherName(textView: TextView, itemClass: ClassDTO) {
    val classname =
        textView.context.getString(R.string.get_class_teacher_name, itemClass.teacher.username)
    textView.text = classname
}

@BindingAdapter("load_class_id")
fun loadClassID(textView: TextView, itemClass: ClassDTO) {
    val classname = textView.context.getString(R.string.get_class_id, itemClass.classId.toString())
    textView.text = classname
}

@BindingAdapter("load_subject_name")
fun loadSubjectName(textView: TextView, itemClass: ClassDTO) {
    val classname = textView.context.getString(R.string.get_subject_name, itemClass.subject)
    textView.text = classname
}

@BindingAdapter("load_notification_title")
fun loadNotificationTitle(textView: TextView, notification: NotificationDTO) {
    val context = textView.context
    val title = when (notification.notificationType) {
        Constants.NotificationType.REQUEST_PARTNER -> {
            context.getString(R.string.notification_title_new_request)
        }
        else -> {
            context.getString(R.string.notification_title_request_accepted)
        }
    }
    textView.text = title
}

@BindingAdapter("set_seen_notification_status")
fun loadNotificationSeenStatus(view: View, notification: NotificationDTO) {
    if (notification.isRead) {
        view.visibility = View.INVISIBLE
    } else {
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("set_notification_icon")
fun loadNotificationIcon(view: ImageView, notification: NotificationDTO) {
    if (notification.notificationType == Constants.NotificationType.REQUEST_PARTNER_RESPONSE) {
        view.setBackgroundResource(R.drawable.ic_request_answered)
    } else {
        view.setBackgroundResource(R.drawable.ic_new_request)
    }
}
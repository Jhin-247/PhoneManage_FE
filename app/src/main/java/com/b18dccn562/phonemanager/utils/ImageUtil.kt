package com.b18dccn562.phonemanager.utils

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.b18dccn562.phonemanager.common.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


fun loadImage(imageView: ImageView, resource: Uri) {
    Glide.with(imageView).load(resource).diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true).into(imageView)
}

fun loadImage(imageView: ImageView, resource: String) {
    Glide.with(imageView).load(resource).diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true).into(imageView)
}

fun loadImage(imageView: ImageView, resource: Drawable?) {
    Glide.with(imageView).load(resource).diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true).into(imageView)
}

fun loadImage(imageView: ImageView, resource: Int) {
    Glide.with(imageView).load(resource).diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true).into(imageView)
}

fun loadUserAvatar(imageView: ImageView, email: String?) {
    if (email != null)
        loadImage(imageView, getUserAvatarImage(email))
}

fun getUserAvatarImage(userEmail: String): String =
    "${Constants.ApiReferences.BASE_URL}${Constants.ApiReferences.IMAGE_REFERENCE}/${Constants.ApiReferences.GET_AVATAR}/$userEmail"
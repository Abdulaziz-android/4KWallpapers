package com.abdulaziz.a4kfullwallpapers.models.searchmodel

data class Result(
    val alt_description: String,
    val blur_hash: String,
    val categories: List<Any>,
    val color: String,
    val created_at: String,
    val current_user_collections: List<Any>,
    val description: String,
    val height: Int,
    val id: String,
    val liked_by_user: Boolean,
    val likes: Int,
    val links: Links,
    val promoted_at: String,
    val sponsorship: Any,
    val tags: List<Tag>,
    val topic_submissions: TopicSubmissionsX,
    val updated_at: String,
    val urls: UrlsX,
    val user: UserX,
    val width: Int
)
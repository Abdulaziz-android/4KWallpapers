package com.abdulaziz.a4kfullwallpapers.models.searchmodel

data class UserX(
    val accepted_tos: Boolean,
    val bio: String,
    val first_name: String,
    val for_hire: Boolean,
    val id: String,
    val instagram_username: String,
    val last_name: String,
    val links: LinksXXX,
    val location: String,
    val name: String,
    val portfolio_url: String,
    val profile_image: ProfileImageX,
    val social: SocialX,
    val total_collections: Int,
    val total_likes: Int,
    val total_photos: Int,
    val twitter_username: String,
    val updated_at: String,
    val username: String
)
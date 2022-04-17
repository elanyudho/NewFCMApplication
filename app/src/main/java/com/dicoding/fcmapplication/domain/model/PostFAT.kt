package com.dicoding.fcmapplication.domain.model

data class PostFAT (
    val fat_name: String,
    val fat_total_core: String,
    val fat_core_used: String,
    val fat_backup_core: String,
    val fat_loss: String,
    val home_covered: String,
    val fat_note: String,
    val fat_location: String,
    val fat_activated: String,
    val fat_region: String,
    val fat_in_repair: Boolean,
    val fdt : FDT
) {
    data class FDT(
        val id: String
    )
}


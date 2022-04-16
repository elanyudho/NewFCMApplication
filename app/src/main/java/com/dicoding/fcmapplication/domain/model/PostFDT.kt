package com.dicoding.fcmapplication.domain.model

data class PostFDT(
    val fdt_name: String,
    val fdt_total_core: String,
    val fdt_core_used: String,
    val fdt_backup_core: String,
    val fdt_loss: String,
    val fdt_in_repair: Boolean,
    val fdt_note: String,
    val fdt_location: String,
    val fdt_activated: String,
    val fdt_region: String,
    val fat_covered_lists: List<CoveredFAT> = mutableListOf(),
) {
   data class CoveredFAT(
    val id: String
    )
}
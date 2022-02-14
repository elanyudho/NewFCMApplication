package com.dicoding.fcmapplication.utils.customview.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val spaceSize: Int,
                           private val leftMargin:Int?=null,
                           private val rightMargin:Int?=null ,
                           private val bottomMargin:Int?=null,
                           private val topMargin:Int?=null
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if(topMargin== null){
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = spaceSize
                }
            }else{
                top = topMargin
            }

            if(leftMargin == null){
                left = spaceSize
            }else{
                left = leftMargin
            }

            if(rightMargin == null){
                right = spaceSize
            }else{
                right = rightMargin
            }

            if(bottomMargin == null){
                bottom = spaceSize
            }else{
                bottom = bottomMargin
            }
        }
    }
}
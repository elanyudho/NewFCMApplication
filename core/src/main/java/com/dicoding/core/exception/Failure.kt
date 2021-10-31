package com.dicoding.core.exception

import com.dicoding.core.vo.RequestResults

data class  Failure(val requestResults: RequestResults, val throwable: Throwable, val code:String="")
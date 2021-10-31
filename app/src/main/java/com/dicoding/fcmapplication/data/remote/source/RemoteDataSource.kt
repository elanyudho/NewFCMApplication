package com.dicoding.fcmapplication.data.remote.source

import com.dicoding.fcmapplication.data.remote.service.ApiService
import javax.inject.Inject

class RemoteDataSource
@Inject constructor(private val api: ApiService) : RemoteSafeRequest() {}
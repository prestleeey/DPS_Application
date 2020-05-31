package com.example.dps_application.domain.entities

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList

data class Listing<T>(
        val pagedList: LiveData<PagedList<T>>,
        val networkState: LiveData<NetworkState>,
        val refresh: () -> Unit,
        val retry: () -> Unit)
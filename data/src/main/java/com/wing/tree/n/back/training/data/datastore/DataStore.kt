package com.wing.tree.n.back.training.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.wing.tree.n.back.training.data.datastore.option.Option
import com.wing.tree.n.back.training.data.datastore.option.OptionSerializer

internal val Context.optionDataStore: DataStore<Option> by dataStore(
    fileName = "option.pb",
    serializer = OptionSerializer
)
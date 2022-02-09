package com.wing.tree.n.back.training.data.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wing.tree.n.back.training.data.entity.Problem

class TypeConverters {
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Problem>>() {

    }

    @TypeConverter
    fun problemToJson(value: Problem): String = gson.toJson(value)

    @TypeConverter
    fun jsonToProblem(value: String): Problem = gson.fromJson(value, Problem::class.java)

    @TypeConverter
    fun problemListToJson(value: List<Problem>): String = gson.toJson(value)

    @TypeConverter
    fun jsonToProblemList(value: String): List<Problem> = gson.fromJson(value, typeToken.type)
}
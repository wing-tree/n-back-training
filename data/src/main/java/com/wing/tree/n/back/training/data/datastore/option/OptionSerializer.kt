package com.wing.tree.n.back.training.data.datastore.option

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.wing.tree.n.back.training.data.constant.Rounds
import com.wing.tree.n.back.training.data.constant.Speed
import com.wing.tree.n.back.training.data.constant.SpeedMode
import java.io.InputStream
import java.io.OutputStream

object OptionSerializer : Serializer<Option> {
    override val defaultValue: Option = Option.newBuilder()
        .setRounds(Rounds.DEFAULT)
        .setSpeed(Speed.DEFAULT)
        .setSpeedMode(SpeedMode.DEFAULT)
        .build()

    override suspend fun readFrom(input: InputStream): Option {
        try {
            return Option.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Option, output: OutputStream) = t.writeTo(output)
}
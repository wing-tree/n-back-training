package com.wing.tree.n.back.training.domain.util

import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun Any?.`is`(other: Any?) = this == other
fun Any?.not(other: Any?) = this.`is`(other).not()

val Any?.isNull get() = this == null
val Any?.notNull: Boolean get() = this.not(null)

fun <T> weakReference(referent: T? = null): ReadWriteProperty<Any?, T?> {
    return object : ReadWriteProperty<Any?, T?> {
        var weakReference = WeakReference<T?>(referent)
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return weakReference.get()
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            weakReference = WeakReference(value)
        }
    }
}
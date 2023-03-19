package com.gitalive.composenote.utils

import android.content.Context
import android.widget.Toast
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

private val jobs: MutableMap<String, Job?> = hashMapOf()

val Context.dataStore by preferencesDataStore("composeNote")


suspend inline fun <reified T : Any> Context.getConfig(
    key: String,
    default: T,
    crossinline action: suspend (T) -> Unit
) {
    dataStore.data.map {
        when (T::class) {
            String::class -> action((it[stringPreferencesKey(key)] ?: default) as T)
            Boolean::class -> action((it[booleanPreferencesKey(key)] ?: default) as T)
            Float::class -> action((it[floatPreferencesKey(key)] ?: default) as T)
            Long::class -> action((it[longPreferencesKey(key)] ?: default) as T)
            Double::class -> action((it[doublePreferencesKey(key)] ?: default) as T)
            Int::class -> action((it[intPreferencesKey(key)] ?: default) as T)
            Set::class -> action((it[stringSetPreferencesKey(key)] ?: default) as T)
            else -> {
                Logger.e("不支持的数据类型：${T::class.java.simpleName}")
            }
        }
    }
}

suspend inline fun <reified T : Any> Context.saveConfig(key: String, value: T) {
    dataStore.edit {
        try {
            when (T::class) {
                String::class -> it[stringPreferencesKey(key)] = value as String
                Boolean::class -> it[booleanPreferencesKey(key)] = value as Boolean
                Float::class -> it[floatPreferencesKey(key)] = value as Float
                Long::class -> it[longPreferencesKey(key)] = value as Long
                Double::class -> it[doublePreferencesKey(key)] = value as Double
                Int::class -> it[intPreferencesKey(key)] = value as Int
                Set::class -> it[stringSetPreferencesKey(key)] = value as Set<String>
                else -> {
                    Logger.e("不支持的数据类型：${T::class.java.simpleName}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun Context.showToast(content: String, isLong: Boolean = false) {
    Toast.makeText(this, content, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun unregister(vararg jobNames: String) {
    jobNames.forEach {
        jobs[it].finish()
    }
}

fun CoroutineScope.register(
    jobName: String,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    needSuperVisor: Boolean = false,
    action: suspend () -> Unit
) {
    jobs[jobName].finish()
    jobs[jobName] = launch(dispatcher) {
        if (needSuperVisor) {
            supervisorScope {
                action()
            }
            return@launch
        }
        action()
    }
}

fun Job?.finish() {
    try {
        if (this?.isActive == true) {
            cancel()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun getFormattedDateTime(
    format: String = "yyyyMMddHHmmss",
    locale: Locale = Locale.CHINA,
    date: Date = Date()
): String = SimpleDateFormat(format, locale).format(date)
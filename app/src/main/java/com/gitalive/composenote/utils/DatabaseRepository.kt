package com.gitalive.composenote.utils

import android.content.Context
import com.gitalive.composenote.entities.MyObjectBox
import com.orhanobut.logger.Logger
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.awaitCallInTx
import io.objectbox.query.Query
import io.objectbox.query.QueryBuilder
import io.objectbox.query.QueryCondition
import kotlinx.coroutines.supervisorScope

/**
 * ObjectBox数据库调用源
 * */
object DatabaseRepository {
    private val onErrorAction = { Logger.e("ObjectBox尚未初始化") }
    private lateinit var boxStore: BoxStore
    private var isInitial: Boolean = false
    private const val ONE_KILO = 1024L

    /**
     * 初始化ObjectBox数据库
     *
     * @param context 应用上下文
     * */
    fun initObjectBox(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context)
            .maxSizeInKByte(10 * ONE_KILO * ONE_KILO)
            .build()
        isInitial = true
    }

    fun deInitObjectBox() {
        try {
            if (isInitial) {
                boxStore.close()
            } else {
                onErrorAction()
            }
        } catch (e: Exception) {
            Logger.e("")
        }
    }

    /**
     * 获取一个Box对象，每个对象对应一张表，可基于Box对象对数据库进行CRUD操作
     *
     * @param klass 传入数据对象所属类型
     * */
    fun <T : Any> getBox(klass: Class<T>): Box<T>? {
        return if (isInitial) {
            boxStore.boxFor(klass)
        } else {
            onErrorAction()
            null
        }
    }

    /**
     * 向表中插入或更新若干数据记录
     *
     * @param obj 可变数量的数据实体对象
     * */
    fun <T : Any> Box<T>.insertOrReplace(vararg obj: T) = put(* obj)

    /**
     * 从表中删除若干数据记录
     *
     * @param obj 可变数量的数据实体对象
     * */
    fun <T : Any> Box<T>.deleteEntities(vararg obj: T) = remove(* obj)

    /**
     * 从表中根据传入的若干ID删除指定ID对应的记录
     *
     * @param ids 可变数量的数据实体对象ID
     * */
    fun <T : Any> Box<T>.deleteByIds(vararg ids: Long) = removeByIds(ids.toCollection(arrayListOf()))

    /**
     * 移除表中所有数据记录
     * */
    fun <T : Any> Box<T>.clearAll() = removeAll()

    /**
     * 获取表中所有数据记录，以列表形式返回结果
     *
     * @return 数据记录列表
     * */
    fun <T : Any> Box<T>.getAllEntities(): List<T> = all

    /**
     * 查询符合条件的所有对象，以列表形式返回查询结果
     *
     * @param condition 调用QueryBuilder对象相关API所构成的查询条件，以lambda表达式的形式传入
     * @param query QueryCondition类型的自定义查询条件
     * @return 查询结果列表
     * */
    fun <T : Any> Box<T>.queryListWithConditions(condition: (QueryBuilder<T>) -> Unit, query: QueryCondition<T>? = null): List<T> {
        var list = emptyList<T>()
        (if (query == null) query() else query(query)).execute(condition) {
            list = it.find()
        }
        return list
    }

    /**
     * 查询符合条件的某条数据记录，以实体对象形式返回查询结果
     *
     * @param condition 调用QueryBuilder对象相关API所构成的查询条件，以lambda表达式的形式传入
     * @param query QueryCondition类型的自定义查询条件
     * @return 查询结果实体对象或null
     * */
    fun <T : Any> Box<T>.queryEntityWithConditions(condition: (QueryBuilder<T>) -> Unit, query: QueryCondition<T>? = null): T? {
        var entity: T? = null
        (if (query == null) query() else query(query)).execute(condition) {
            entity = it.findUnique()
        }
        return entity
    }

    /**
     * 基于Kotlin协程的异步事务处理逻辑封装
     *
     * @param input 具体执行事务，以lambda表达式形式传入
     * @param output 事务执行结果处理，接收实体对象或null，以lambda表达式形式传入
     * */
    suspend fun <T> runInCoroutine(input: () -> T, output: (T?) -> Unit) {
        supervisorScope {
            try {
                if (isInitial) {
                    boxStore.awaitCallInTx {
                        input()
                    }.apply { output(this) }
                } else {
                    onErrorAction()
                }
            } catch (e: Exception) {
                Logger.e("")
            }
        }
    }

    /**
     * QueryBuilder相关业务逻辑封装
     *
     * @param condition 调用QueryBuilder对象相关API所构成的查询条件，以lambda表达式的形式传入
     * @param action 具体查询业务，接收Query对象，以lambda表达式的形式传入
     * */
    private fun <T : Any> QueryBuilder<T>.execute(condition: (QueryBuilder<T>) -> Unit, action: (Query<T>) -> Unit) {
        if (isInitial) {
            condition(this)
            build().apply {
                action(this)
                // 查询完毕后必须关闭Query对象以释放资源
                close()
            }
        } else {
            onErrorAction()
        }
    }
}
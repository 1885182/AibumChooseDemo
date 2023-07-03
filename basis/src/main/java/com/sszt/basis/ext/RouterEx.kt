package com.sszt.basis.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.hjq.toast.ToastUtils
import com.sszt.resources.IRoute
import java.io.Serializable
import java.util.*

fun Activity.toast(msg: String, isShow: Boolean = true) {
    if (!isShow) {
        return
    }
    ToastUtils.show(msg)
    if (msg == "TOKEN超期" || msg == "登录身份过期，请重新登陆") {
        SPUtils.getInstance().clear()
        ActivityUtils.finishAllActivities()
        router(IRoute.login_login)
    }
}

fun Fragment.toast(msg: String, isShow: Boolean = true) {
    if (!isShow) {
        return
    }
    ToastUtils.show(msg)
    if (msg == "TOKEN超期" || msg == "登录身份过期，请重新登陆") {
        SPUtils.getInstance().clear()
        ActivityUtils.finishAllActivities()
        router(IRoute.login_login)
    }
}

fun Context.toast(str: String) {
    ToastUtils.show(str)
}

inline fun <reified T : Activity> Context.navigationTo(vararg params: Pair<String, Any?>) =
    internalStartActivity(this, T::class.java, params)

inline fun <reified T : Activity> Activity.navigationTo(vararg params: Pair<String, Any?>) =
    internalStartActivity(this, T::class.java, params)

inline fun <reified T : Activity> Fragment.navigationTo(vararg params: Pair<String, Any?>) =
    context?.let { internalStartActivity(it, T::class.java, params) }

inline fun <reified T : Activity> Activity.navigationToForResult(
    requestCode: Int,
    vararg params: Pair<String, Any?>
) =
    internalStartActivityForResult(this, T::class.java, requestCode, params)

inline fun <reified T : Activity> Fragment.navigationToForResult(
    requestCode: Int,
    vararg params: Pair<String, Any?>
) =
    activity?.let { internalStartActivityForResult(it, T::class.java, requestCode, params) }

fun Activity.finishForResult(vararg params: Pair<String, Any?>) {
    setResult(Activity.RESULT_OK, createIntent<Activity>(params = params))
    finish()
}

fun Fragment.finishForResult(vararg params: Pair<String, Any?>) {
    activity?.setResult(Activity.RESULT_OK, createIntent<Activity>(params = params))
    activity?.finish()
}

fun internalStartActivity(
    ctx: Context,
    activity: Class<out Activity>,
    params: Array<out Pair<String, Any?>>
) {
    ctx.startActivity(createIntent(ctx, activity, params))
}

fun internalStartActivityForResult(
    act: Activity,
    activity: Class<out Activity>,
    requestCode: Int,
    params: Array<out Pair<String, Any?>>
) {
    act.startActivityForResult(createIntent(act, activity, params), requestCode)
}

fun <T> createIntent(
    ctx: Context? = null,
    clazz: Class<out T>? = null,
    params: Array<out Pair<String, Any?>>
): Intent {
    val intent =
        if (clazz == null) Intent()
        else Intent(ctx, clazz)
    if (params.isNotEmpty())
        fillIntentArguments(intent, params)
    return intent
}

private fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
    params.forEach {
        when (val value = it.second) {
            null -> intent.putExtra(it.first, null as Serializable?)
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is CharSequence -> intent.putExtra(it.first, value)
            is String -> intent.putExtra(it.first, value)
            is Float -> intent.putExtra(it.first, value)
            is Double -> intent.putExtra(it.first, value)
            is Char -> intent.putExtra(it.first, value)
            is Short -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
            is Serializable -> intent.putExtra(it.first, value)
            is Bundle -> intent.putExtra(it.first, value)
            is Parcelable -> intent.putExtra(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                else -> throw RuntimeException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> intent.putExtra(it.first, value)
            is LongArray -> intent.putExtra(it.first, value)
            is FloatArray -> intent.putExtra(it.first, value)
            is DoubleArray -> intent.putExtra(it.first, value)
            is CharArray -> intent.putExtra(it.first, value)
            is ShortArray -> intent.putExtra(it.first, value)
            is BooleanArray -> intent.putExtra(it.first, value)
            else -> throw RuntimeException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
        }
        return@forEach
    }
}

/**
 * @param path 路由路径
 * @param params 需要传的值
 */
fun Fragment.router(path: String, vararg params: Pair<String, Any?>) {
    val build = ARouter.getInstance().build(path)
    if (params.isNotEmpty())
        fillRouterArguments(build, params)
    build.navigation()
}



/**
 * @param path 路由路径
 * @param params 需要传的值
 */
fun Activity.router(path: String, vararg params: Pair<String, Any?>) {
    val build = ARouter.getInstance().build(path)
    if (params.isNotEmpty())
        fillRouterArguments(build, params)
    build.navigation()
}

fun FragmentActivity.getResColor(color: Int) = ContextCompat.getColor(this, color)


/**
 * @param path 路由路径
 * @param params 需要传的值
 */
fun Context.router(path: String, vararg params: Pair<String, Any?>) {
    val build = ARouter.getInstance().build(path)
    if (params.isNotEmpty())
        fillRouterArguments(build, params)
    build.navigation()
}

/**
 * @param path 路由路径
 * @param resultCode 返回值
 * @param params 需要传的值
 */
fun Activity.routerResultCode(path: String, resultCode: Int, vararg params: Pair<String, Any?>) {
    val build = ARouter.getInstance().build(path)
    if (params.isNotEmpty())
        fillRouterArguments(build, params)
    build.navigation(this, resultCode)
}

fun Context.routerResultCode(path: String, vararg params: Pair<String, Any?>) {
    val build = ARouter.getInstance().build(path)
    if (params.isNotEmpty())
        fillRouterArguments(build, params)
    build.navigation(this,  null)
}

/**
 * @param path 路由路径
 * @param resultCode 返回值
 * @param params 需要传的值
 */
fun Fragment.routerResultCode(path: String, resultCode: Int, vararg params: Pair<String, Any?>) {
    val build = ARouter.getInstance().build(path)
    if (params.isNotEmpty())
        fillRouterArguments(build, params)
    build.navigation(this.activity, resultCode)
}

private fun fillRouterArguments(aRouter: Postcard, params: Array<out Pair<String, Any?>>) {
    params.forEach {
        when (val value = it.second) {
            null -> aRouter.withSerializable(it.first, null as Serializable?)
            is Int -> aRouter.withInt(it.first, value)
            is Long -> aRouter.withLong(it.first, value)
            is CharSequence -> aRouter.withCharSequence(it.first, value)
            is String -> aRouter.withString(it.first, value)
            is Float -> aRouter.withFloat(it.first, value)
            is Double -> aRouter.withDouble(it.first, value)
            is Char -> aRouter.withChar(it.first, value)
            is Short -> aRouter.withShort(it.first, value)
            is Boolean -> aRouter.withBoolean(it.first, value)
            is Serializable -> aRouter.withSerializable(it.first, value)
            is Bundle -> aRouter.with(value)
            is Parcelable -> aRouter.withParcelable(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> aRouter.withCharSequenceArray(
                    it.first,
                    value as Array<out CharSequence>?
                )
                value.isArrayOf<String>() -> aRouter.withStringArrayList(
                    it.first,
                    value as ArrayList<String>?
                )
                value.isArrayOf<Parcelable>() -> aRouter.withParcelableArray(
                    it.first,
                    value as Array<out Parcelable>?
                )
                else -> throw RuntimeException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> aRouter.withIntegerArrayList(it.first, value as ArrayList<Int>?)
            is FloatArray -> aRouter.withFloatArray(it.first, value)
            is CharArray -> aRouter.withCharArray(it.first, value)
            is ShortArray -> aRouter.withShortArray(it.first, value)
            else -> throw RuntimeException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
        }
        return@forEach
    }
}

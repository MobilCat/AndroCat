package mustafaozhan.github.com.androcat.extensions

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mustafaozhan.github.com.androcat.tools.Notification

fun String.remove(str: String) = replace(str, "")

fun <T> Observable<T>.applySchedulers(): Observable<T> =
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

fun ArrayList<Pair<Notification, Boolean>>?.getFirstList(): ArrayList<String> {
    val items = ArrayList<String>()
    this?.forEach {
        items.add(it.first.value)
    }
    return items
}

fun ArrayList<Pair<Notification, Boolean>>?.getSecondList(): ArrayList<Boolean> {
    val items = ArrayList<Boolean>()
    this?.forEach {
        items.add(it.second)
    }
    return items
}
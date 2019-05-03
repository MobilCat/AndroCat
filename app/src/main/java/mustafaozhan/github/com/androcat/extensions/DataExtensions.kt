package mustafaozhan.github.com.androcat.extensions

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val QUICK_ACTION_SIZE = 14

fun String.remove(str: String) = replace(str, "")

fun <T> Observable<T>.applySchedulers(): Observable<T> =
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

fun String.toQuickActionSize(): String {
    var result = this
    repeat(QUICK_ACTION_SIZE - this.length) {
        result += "  "
    }
    return result
}
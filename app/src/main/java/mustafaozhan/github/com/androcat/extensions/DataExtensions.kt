package mustafaozhan.github.com.androcat.extensions

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun String.remove(str: String) = replace(str, "")

fun <T> Observable<T>.applySchedulers(): Observable<T> =
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

fun String?.isValidUsername(): Boolean {
    return !this.isNullOrEmpty() && this != "null" && !this.contains("@")
}
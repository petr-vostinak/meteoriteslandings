package cz.vostinak.meteoritelandings.api.common

import okhttp3.Request
import retrofit2.Invocation


/**
 * Check if request is annotated with given annotation
 * @param T Annotation type
 * @param clazz Annotation class
 * @return True if request is annotated with given annotation
 */
fun <T: Annotation> Request.isAnnotated(clazz: Class<T>) = tag(Invocation::class.java)
    ?.method()
    ?.getAnnotation(clazz) != null
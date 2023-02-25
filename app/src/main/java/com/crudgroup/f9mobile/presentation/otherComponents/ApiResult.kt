package com.crudgroup.f9mobile.presentation.otherComponents

data class ApiResult <out T> (
    val status: ApiStatus,
    val data: T?,
    val message: String?,
    val error: Throwable?
    )
{

    companion object {
        fun <T> success(data: T?, msg: String? = null): ApiResult<T> {
            return ApiResult(ApiStatus.SUCCESS, data, msg, null)
        }

        fun <T> error(error: Throwable?, msg: String? = null): ApiResult<T> {
            return ApiResult(ApiStatus.ERROR, null, msg, error)
        }


        fun <T> ApiResult<T>.success(body: (T?) -> Unit){
            if (status == ApiStatus.SUCCESS) body(data)
        }

        fun <T> ApiResult<T>.error(body: (Throwable?) -> T){
            if (status == ApiStatus.ERROR) body(error)
        }
    }

}
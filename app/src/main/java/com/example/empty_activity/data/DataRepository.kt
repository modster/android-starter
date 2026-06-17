package com.example.empty_activity.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class DataResult<T>(
  val status: DataStatus,
  val data: T? = null,
  val error: Throwable? = null
)

interface DataRepository {
  val data: Flow<DataResult<List<String>>>
}

class DefaultDataRepository : DataRepository {
  override val data: Flow<DataResult<List<String>>> = flow {
    emit(DataResult(DataStatus.LOADING))
    try {
      emit(DataResult(DataStatus.SUCCESS, listOf("1", "2", "3")))
    } catch (e: Exception) {
      emit(DataResult(DataStatus.ERROR, error = e))
    }
  }
}

enum class DataStatus {
  LOADING, SUCCESS, ERROR
}

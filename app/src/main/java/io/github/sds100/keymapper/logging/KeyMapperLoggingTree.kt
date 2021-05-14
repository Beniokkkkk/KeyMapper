package io.github.sds100.keymapper.logging

import android.util.Log
import io.github.sds100.keymapper.data.Keys
import io.github.sds100.keymapper.data.entities.LogEntryEntity
import io.github.sds100.keymapper.data.repositories.PreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

/**
 * Created by sds100 on 13/05/2021.
 */
class KeyMapperLoggingTree(
    private val coroutineScope: CoroutineScope,
    preferenceRepository: PreferenceRepository,
    private val logRepository: LogRepository
) : Timber.Tree() {
    private val log: StateFlow<Boolean> = preferenceRepository.get(Keys.log)
        .map { it ?: false }
        .stateIn(coroutineScope, SharingStarted.Eagerly, false)

    private val messagesToLog = MutableSharedFlow<LogEntryEntity>(
        extraBufferCapacity = 1000,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    init {
        messagesToLog
            .onEach {
                logRepository.insert(it)
            }
            .flowOn(Dispatchers.Default)
            .launchIn(coroutineScope)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (!log.value) {
            return
        }

        val severity = when (priority) {
            Log.ERROR -> LogEntryEntity.SEVERITY_ERROR
            Log.DEBUG -> LogEntryEntity.SEVERITY_DEBUG
            else -> LogEntryEntity.SEVERITY_DEBUG
        }

        coroutineScope.launch {
            messagesToLog.emit(
                LogEntryEntity(
                    id = 0,
                    time = Calendar.getInstance().timeInMillis,
                    severity = severity,
                    message = message
                )
            )
        }
    }
}
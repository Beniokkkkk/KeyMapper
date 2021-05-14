package io.github.sds100.keymapper.logging

import io.github.sds100.keymapper.R
import io.github.sds100.keymapper.data.entities.LogEntryEntity
import io.github.sds100.keymapper.system.clipboard.ClipboardAdapter
import io.github.sds100.keymapper.system.files.FileAdapter
import io.github.sds100.keymapper.util.State
import io.github.sds100.keymapper.util.ifIsData
import io.github.sds100.keymapper.util.mapData
import io.github.sds100.keymapper.util.onSuccess
import io.github.sds100.keymapper.util.ui.ResourceProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*

/**
 * Created by sds100 on 13/05/2021.
 */

class DisplayLogUseCaseImpl(
    private val repository: LogRepository,
    private val resourceProvider: ResourceProvider,
    private val clipboardAdapter: ClipboardAdapter,
    private val fileAdapter: FileAdapter
) : DisplayLogUseCase {
    override val log: Flow<State<List<LogEntry>>> = repository.log
        .map { state ->
            state.mapData { entityList -> entityList.map { LogEntryEntityMapper.fromEntity(it) } }
        }

    override fun clearLog() {
        repository.deleteAll()
    }

    override suspend fun copyToClipboard() {

        repository.log.first().ifIsData { logEntries ->
            val logText = getLogText(logEntries)

            clipboardAdapter.copy(
                label = resourceProvider.getString(R.string.clip_key_mapper_log),
                logText
            )
        }
    }

    override suspend fun saveToFile(uri: String) {
        fileAdapter.openOutputStream(uri).onSuccess { outputStream ->

            repository.log.first().ifIsData { logEntries ->
                val logText = getLogText(logEntries)

                outputStream.bufferedWriter().use { it.write(logText) }
            }
        }
    }

    private fun getLogText(logEntries: List<LogEntryEntity>): String {
        val dateFormat = LogUtils.DATE_FORMAT

        return logEntries.joinToString(separator = "\n") { entry ->
            val date = dateFormat.format(Date(entry.time))

            return@joinToString "$date  ${entry.message}"
        }
    }
}

interface DisplayLogUseCase {
    val log: Flow<State<List<LogEntry>>>
    fun clearLog()
    suspend fun copyToClipboard()
    suspend fun saveToFile(uri: String)
}
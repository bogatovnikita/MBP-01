package yin_kio.file_manager.presentation.presenters

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.text.format.Formatter.formatFileSize
import yin_kio.file_manager.domain.models.FileGroup
import yin_kio.file_manager.domain.models.FileInfo
import yin_kio.file_manager.presentation.R

class DescriptionPresenter(
    private val context: Context
) {

    private val divider = context.getString(R.string.description_divider)

    fun present(fileInfo: FileInfo) : String{
        val size = formatFileSize(context, fileInfo.size)

        return when (fileInfo.fileGroup) {
            FileGroup.Audio -> formatAudio(fileInfo.path, size)
            else -> formatSimple(size, fileInfo.path)
        }

    }

    private fun formatSimple(
        size: String?,
        path: String
    ) = "$size $divider $path"

    private fun formatAudio(
        path: String,
        size: String?
    ): String {
        val duration = getDuration(path).formatDuration()
        return "$duration $divider $size $divider $path"
    }

    private fun Long.formatDuration() : String{
        val millis = this
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        val minutesStr = (minutes % 60).formatForTwoSymbols()
        val secondsStr = (seconds % 60).formatForTwoSymbols()

        return when{
            hours > 0 -> "$hours:$minutesStr:$secondsStr"
            else -> "$minutesStr:$secondsStr"
        }
    }

    private fun Long.formatForTwoSymbols() : String{
        val str = this.toString()
        return if (str.length == 2) str else "0$str"
    }

    private fun getDuration(path: String) : Long{
        val uri: Uri = Uri.parse(path)
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(context, uri)
        val durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        return durationStr?.toLong() ?: 0
    }

}
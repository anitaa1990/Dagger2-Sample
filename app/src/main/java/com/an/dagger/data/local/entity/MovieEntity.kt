package com.an.dagger.data.local.entity

import android.arch.persistence.room.Entity
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


@Entity(primaryKeys = ["id"])
data class MovieEntity(
        @SerializedName("id")
        val id: Long,

        @SerializedName(value = "header", alternate = ["title", "name"])
        val header: String,

        @SerializedName("poster_path")
        var posterPath: String?,

        @SerializedName(value = "description", alternate = ["overview", "synopsis"])
        var description: String?,

        @SerializedName("release_date")
        var releaseDate: String?,

        @SerializedName("runtime")
        var runTime: Long,
        var status: String?
) : Parcelable {


    fun getFormattedPosterPath(): String? {
        if (posterPath != null && !posterPath!!.startsWith("http")) {
            posterPath = String.format("https://image.tmdb.org/t/p/w500%s", posterPath)
        }
        return posterPath
    }

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readLong(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(header)
        writeString(posterPath)
        writeString(description)
        writeString(releaseDate)
        writeLong(runTime)
        writeString(status)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MovieEntity> = object : Parcelable.Creator<MovieEntity> {
            override fun createFromParcel(source: Parcel): MovieEntity = MovieEntity(source)
            override fun newArray(size: Int): Array<MovieEntity?> = arrayOfNulls(size)
        }
    }
}

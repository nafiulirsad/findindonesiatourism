package com.fitdev.findindonesiatourism.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "placeId")
    var placeId: String? = null,
    @ColumnInfo(name = "placeName")
    var placeName: String? = null,
    @ColumnInfo(name = "placeRatingsTotal")
    var placeRatingsTotal: Int = 0,
    @ColumnInfo(name = "placeRating")
    var placeRating: String? = null,
    @ColumnInfo(name = "placePhotos")
    var placePhotos: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(placeId)
        parcel.writeString(placeName)
        parcel.writeInt(placeRatingsTotal)
        parcel.writeString(placeRating)
        parcel.writeString(placePhotos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Favorite> {
        override fun createFromParcel(parcel: Parcel): Favorite {
            return Favorite(parcel)
        }

        override fun newArray(size: Int): Array<Favorite?> {
            return arrayOfNulls(size)
        }
    }
}

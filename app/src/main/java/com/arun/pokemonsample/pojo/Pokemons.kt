package com.arun.pokemonsample.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Pokemons(@SerializedName("count") val count:Long?,
                    @SerializedName("next") val next:String?,
                    @SerializedName("previous") val previous:String?,
                    @SerializedName("results") val results:ArrayList<PokemonUrl>?)

data class PokemonUrl(@SerializedName("name") val name:String?,
                      @SerializedName("url") val url:String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokemonUrl> {
        override fun createFromParcel(parcel: Parcel): PokemonUrl {
            return PokemonUrl(parcel)
        }

        override fun newArray(size: Int): Array<PokemonUrl?> {
            return arrayOfNulls(size)
        }
    }
}
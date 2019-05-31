package com.arun.pokemonsample.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Ability(@SerializedName("name") val name: String?) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ability> {
        override fun createFromParcel(parcel: Parcel): Ability {
            return Ability(parcel)
        }

        override fun newArray(size: Int): Array<Ability?> {
            return arrayOfNulls(size)
        }
    }
}

data class AbilityInfo(
    @SerializedName("ability") val ability: Ability?,
    @SerializedName("is_hidden") val isHidden: Boolean?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Ability::class.java.classLoader),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(ability, flags)
        parcel.writeValue(isHidden)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AbilityInfo> {
        override fun createFromParcel(parcel: Parcel): AbilityInfo {
            return AbilityInfo(parcel)
        }

        override fun newArray(size: Int): Array<AbilityInfo?> {
            return arrayOfNulls(size)
        }
    }
}

data class Sprites(
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("front_female") val frontFemale: String?,
    @SerializedName("front_shiny") val frontShiny: String?,
    @SerializedName("front_shiny_female") val frontShinyFemale: String?,
    @SerializedName("back_default") val backDefault: String?,
    @SerializedName("back_female") val backFemale: String?,
    @SerializedName("back_shiny") val backShiny: String?,
    @SerializedName("back_shiny_female") val backShinyFemale: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(backDefault)
        parcel.writeString(backFemale)
        parcel.writeString(backShiny)
        parcel.writeString(backShinyFemale)
        parcel.writeString(frontDefault)
        parcel.writeString(frontFemale)
        parcel.writeString(frontShiny)
        parcel.writeString(frontShinyFemale)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sprites> {
        override fun createFromParcel(parcel: Parcel): Sprites {
            return Sprites(parcel)
        }

        override fun newArray(size: Int): Array<Sprites?> {
            return arrayOfNulls(size)
        }
    }
}

data class MoveData(@SerializedName("move") val  move: Move?)

data class Move(@SerializedName("name") val name: String?) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Move> {
        override fun createFromParcel(parcel: Parcel): Move {
            return Move(parcel)
        }

        override fun newArray(size: Int): Array<Move?> {
            return arrayOfNulls(size)
        }
    }
}

data class PokemonData(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("base_experience") val baseExperience: Int?,
    @SerializedName("height") val height: Int?,
    @SerializedName("weight") val weight: Int?,
    @SerializedName("abilities") val abilities: ArrayList<AbilityInfo>?,
    @SerializedName("sprites") val sprites: Sprites?,
    @SerializedName("moves") val moves: ArrayList<MoveData>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        arrayListOf<AbilityInfo>().apply {
            parcel.readList(this, AbilityInfo::class.java.classLoader)
        },
        parcel.readParcelable(Sprites::class.java.classLoader),
        arrayListOf<MoveData>().apply {
            parcel.readList(this, MoveData::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeValue(baseExperience)
        parcel.writeValue(height)
        parcel.writeValue(weight)
        parcel.writeParcelable(sprites, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokemonData> {
        override fun createFromParcel(parcel: Parcel): PokemonData {
            return PokemonData(parcel)
        }

        override fun newArray(size: Int): Array<PokemonData?> {
            return arrayOfNulls(size)
        }
    }

}

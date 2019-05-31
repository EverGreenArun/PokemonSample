package com.arun.pokemonsample.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arun.pokemonsample.base.BaseViewModel
import com.arun.pokemonsample.common.CommonConstants
import com.arun.pokemonsample.pojo.PokemonData


class PokemonDetailFragmentViewModel(application: Application) : BaseViewModel(application) {
    val images: ArrayList<String> = ArrayList()
    lateinit var pokemonData: PokemonData

    companion object {
        const val HEIGHT = "Height"
        const val WEIGHT = "Weight"
        const val EXPERIENCE = "Experience"
    }

    fun setData(pokemonData: PokemonData) {
        this.pokemonData = pokemonData
        pokemonData.sprites?.frontDefault?.let { images.add(it) }
        pokemonData.sprites?.backDefault?.let { images.add(it) }
        pokemonData.sprites?.frontFemale?.let { images.add(it) }
        pokemonData.sprites?.backFemale?.let { images.add(it) }
        pokemonData.sprites?.frontShiny?.let { images.add(it) }
        pokemonData.sprites?.backShiny?.let { images.add(it) }
        pokemonData.sprites?.frontShinyFemale?.let { images.add(it) }
        pokemonData.sprites?.backShinyFemale?.let { images.add(it) }
    }

    fun getHeight():String{
       return HEIGHT + CommonConstants.SINGLE_SPACE + CommonConstants.SEMI_COLON+CommonConstants.SINGLE_SPACE+pokemonData.height
    }

    fun getWeight():String{
        return WEIGHT + CommonConstants.SINGLE_SPACE + CommonConstants.SEMI_COLON+CommonConstants.SINGLE_SPACE+pokemonData.weight
    }

    fun getExperience():String{
        return EXPERIENCE + CommonConstants.SINGLE_SPACE + CommonConstants.SEMI_COLON+CommonConstants.SINGLE_SPACE+pokemonData.baseExperience
    }

    class PokemonDetailFragmentVMFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Application::class.java).newInstance(application)
        }
    }
}
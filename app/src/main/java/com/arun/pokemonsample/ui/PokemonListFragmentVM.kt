package com.arun.pokemonsample.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arun.pokemonsample.base.ApiStatus
import com.arun.pokemonsample.base.BaseViewModel
import com.arun.pokemonsample.pojo.PokemonData
import com.arun.pokemonsample.pojo.PokemonUrl
import com.arun.pokemonsample.pojo.Pokemons
import com.arun.pokemonsample.repo.PokemonsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonListFragmentViewModel(application: Application) : BaseViewModel(application) {
    val liveData by lazy {
        MutableLiveData<Triple<ApiStatus, PokemonApiType, Any>>()
    }
    val pokemonUrls = ArrayList<PokemonUrl>()
    private var nextPokemonListUrl: String? = "https://pokeapi.co/api/v2/pokemon/"
    var isLast = false

    fun getPokemons() {
        if (!isLast) {
            uiScope.launch {
                val pokemons = nextPokemonListUrl?.let { getPokemonsFromRemote(it) } ?: getPokemonsFromRemote()
                liveData.value = pokemons?.let { pokemonsResult ->
                    pokemonsResult.results?.let {
                        pokemonUrls.addAll(it)
                    }
                    nextPokemonListUrl = pokemonsResult.next
                    isLast = pokemonsResult.next.isNullOrEmpty()
                    Triple(ApiStatus.SUCCESS,PokemonApiType.POKEMON_LIST_TYPE,"")
                } ?: Triple(ApiStatus.FAILURE,PokemonApiType.POKEMON_LIST_TYPE,"")
            }
        }
    }

    fun getPokemonData(url: String) {
        if (!isLast) {
            uiScope.launch {
                val data = getPokemonDataFromRemote(url)
                liveData.value = data?.let {
                    Triple(ApiStatus.SUCCESS,PokemonApiType.POKEMON_DATA_TYPE,it)
                } ?: Triple(ApiStatus.FAILURE,PokemonApiType.POKEMON_DATA_TYPE,"")
            }
        }
    }

    private suspend fun getPokemonsFromRemote(url: String = ""): Pokemons? = withContext(Dispatchers.IO) {
        return@withContext PokemonsRepo.getPokemons(url)
    }

    private suspend fun getPokemonDataFromRemote(url: String): PokemonData? = withContext(Dispatchers.IO) {
        return@withContext PokemonsRepo.getPokemonData(url)
    }

    class PokemonListFragmentVMFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Application::class.java).newInstance(application)
        }
    }
}

enum class PokemonApiType{
    POKEMON_LIST_TYPE, POKEMON_DATA_TYPE
}
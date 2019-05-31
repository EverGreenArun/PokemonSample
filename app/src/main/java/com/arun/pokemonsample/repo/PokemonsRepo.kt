package com.arun.pokemonsample.repo

import com.arun.pokemonsample.base.BaseRepository
import com.arun.pokemonsample.network.ApiFactory
import com.arun.pokemonsample.pojo.PokemonData
import com.arun.pokemonsample.pojo.Pokemons

object PokemonsRepo: BaseRepository() {
    private val api = ApiFactory.makeRetrofitService()

    suspend fun getPokemons(url : String) : Pokemons?{
        return safeApiCall(
            call = { api.getPokemonsAsync(url).await()},
            errorMessage = "Error Fetching Popular Movies")
    }

    suspend fun getPokemonData(url : String) : PokemonData?{
        return safeApiCall(
            call = { api.getPokemonDataAsync(url).await()},
            errorMessage = "Error Fetching Popular Movies")
    }
}
package com.arun.pokemonsample.network

import com.arun.pokemonsample.pojo.PokemonData
import com.arun.pokemonsample.pojo.Pokemons
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {
    @GET
    fun getPokemonsAsync(@Url url:String): Deferred<Response<Pokemons>>

    @GET
    fun getPokemonDataAsync(@Url url:String): Deferred<Response<PokemonData>>
}
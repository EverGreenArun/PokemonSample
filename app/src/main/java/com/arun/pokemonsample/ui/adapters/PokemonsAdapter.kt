package com.arun.pokemonsample.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arun.pokemonsample.base.BaseViewHolder
import com.arun.pokemonsample.databinding.ViewHolderPokemonBinding
import com.arun.pokemonsample.pojo.PokemonUrl
import com.arun.pokemonsample.utility.UiUtility.setRainbowAnimation


class PokemonsAdapter(
    private val pokemons: ArrayList<PokemonUrl>,
    private val onPokemonClickListener: OnPokemonClickListener
) : RecyclerView.Adapter<PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            ViewHolderPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onPokemonClickListener
        )
    }

    override fun getItemCount() = pokemons.size

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.onBind(pokemons[position])
    }

    override fun onViewDetachedFromWindow(holder: PokemonViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unBind()
    }
}

class PokemonViewHolder(
    private val binding: ViewHolderPokemonBinding,
    private val onPokemonClickListener: OnPokemonClickListener
) : BaseViewHolder(binding) {
    fun onBind(url: PokemonUrl) {
        binding.pokemon = url
        setRainbowAnimation(binding.tvTitle, url.name)
        binding.root.setOnClickListener { onPokemonClickListener.onPokemonClick(url) }
        binding.executePendingBindings()
    }
}

interface OnPokemonClickListener {
    fun onPokemonClick(url: PokemonUrl)
}
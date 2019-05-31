package com.arun.pokemonsample.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arun.pokemonsample.base.BaseViewHolder
import com.arun.pokemonsample.databinding.ViewHolderPokemonImageBinding
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder


class PokemonImagesAdapter(private val images: ArrayList<String>) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return PokemonImagesViewHolder(
            ViewHolderPokemonImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is PokemonImagesViewHolder) {
            holder.onBind(images[position])
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unBind()
    }
}


class PokemonImagesViewHolder(private val binding: ViewHolderPokemonImageBinding) : BaseViewHolder(binding) {
    fun onBind(url: String) {
        val imageRequest: ImageRequest = ImageRequestBuilder
            .newBuilderWithSource(Uri.parse(url))
            .build()

        Fresco.getImagePipeline().prefetchToBitmapCache(imageRequest, binding.sdvPokemon.context)

        binding.sdvPokemon.controller = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(binding.sdvPokemon.controller)
            .build()
    }
}
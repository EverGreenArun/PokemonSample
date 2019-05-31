package com.arun.pokemonsample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.arun.pokemonsample.base.BaseActivity
import com.arun.pokemonsample.base.BaseFragment
import com.arun.pokemonsample.base.BaseViewModel
import com.arun.pokemonsample.databinding.FragmentPokemonDetailBinding
import com.arun.pokemonsample.pojo.PokemonData
import com.arun.pokemonsample.ui.adapters.PokemonImagesAdapter
import com.arun.pokemonsample.utility.UiUtility
import com.facebook.shimmer.Shimmer
import android.widget.TextView
import com.arun.pokemonsample.R
import com.google.android.flexbox.FlexboxLayout


class PokemonDetailFragment : BaseFragment<FragmentPokemonDetailBinding, PokemonDetailFragmentViewModel>() {

    companion object {
        const val POKEMON_DATA = "data"
        fun newInstance(pokemonData: PokemonData): PokemonDetailFragment {
            val bundle = Bundle()
            bundle.putParcelable(POKEMON_DATA, pokemonData)
            val fragment = PokemonDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val columnWidth = 100f
    private lateinit var dataBinding: FragmentPokemonDetailBinding
    private lateinit var pokemonImagesAdapter: PokemonImagesAdapter
    private val layoutManager by lazy {
        GridLayoutManager(activity, UiUtility.calculateNoOfColumns(activity?.applicationContext, columnWidth))
    }

    private val flowLayoutParam by lazy {
        val lparams = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT
        )
        lparams.setMargins(
            UiUtility.dpToPx(activity, 0),
            UiUtility.dpToPx(activity, 5),
            UiUtility.dpToPx(activity, 10),
            UiUtility.dpToPx(activity, 5)
        )
        lparams
    }

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            activity?.application?.let { PokemonDetailFragmentViewModel.PokemonDetailFragmentVMFactory(it) })
            .get(PokemonDetailFragmentViewModel::class.java)
    }

    override fun setActionBarTitle() {
        viewModel.pokemonData.name?.let { (activity as BaseActivity<*>).updateTitle(it) }
        (activity as BaseActivity<*>).showActionBarBackButton(true)
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun getDataBinding(inflater: LayoutInflater, container: ViewGroup?): ViewDataBinding {
        dataBinding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return dataBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<PokemonData>(POKEMON_DATA)?.let { viewModel.setData(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.pokemonVM = viewModel
        setImagesView()
        setAbilitiesView()
        setMovesView()
    }

    private fun setImagesView() {
        dataBinding.recyclerPokemon.layoutManager = layoutManager
        pokemonImagesAdapter = PokemonImagesAdapter(viewModel.images)
        dataBinding.recyclerPokemon.adapter = pokemonImagesAdapter
        dataBinding.shimmerContainer.setShimmer(
            Shimmer.AlphaHighlightBuilder().setBaseAlpha(1f).setHighlightAlpha(0.1f).build()
        )
    }

    private fun setAbilitiesView() {
        viewModel.pokemonData.abilities?.let { abilities ->
            dataBinding.abilitiesLayout.visibility = if (abilities.size > 0) View.VISIBLE else View.GONE
            for (ability in abilities) {
                val tv = layoutInflater.inflate(R.layout.text_view_ability, null) as TextView
                tv.layoutParams = flowLayoutParam
                tv.text = ability.ability?.name
                ability.isHidden?.let { tv.isEnabled = it }
                dataBinding.abilitiesLayout.addView(tv)
            }
        }
    }

    private fun setMovesView() {
        viewModel.pokemonData.moves?.let { moves ->
            dataBinding.movesLayout.visibility = if (moves.size > 0) View.VISIBLE else View.GONE
            for (move in moves) {
                val tv = layoutInflater.inflate(R.layout.text_view_ability, null) as TextView
                tv.layoutParams = flowLayoutParam
                UiUtility.setRainbowAnimation(tv, move.move?.name)
                dataBinding.movesLayout.addView(tv)
            }
        }
    }

    fun onOrientationChanged() {
        layoutManager.spanCount = UiUtility.calculateNoOfColumns(activity?.applicationContext, columnWidth)
    }
}
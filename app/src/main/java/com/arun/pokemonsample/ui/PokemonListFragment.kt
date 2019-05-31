package com.arun.pokemonsample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arun.pokemonsample.R
import com.arun.pokemonsample.base.ApiStatus
import com.arun.pokemonsample.base.BaseActivity
import com.arun.pokemonsample.base.BaseFragment
import com.arun.pokemonsample.base.BaseViewModel
import com.arun.pokemonsample.databinding.FragmentPokemonListBinding
import com.arun.pokemonsample.pojo.PokemonData
import com.arun.pokemonsample.pojo.PokemonUrl
import com.arun.pokemonsample.ui.adapters.OnPokemonClickListener
import com.arun.pokemonsample.ui.adapters.PokemonsAdapter
import com.arun.pokemonsample.utility.UiUtility
import com.facebook.shimmer.Shimmer

class PokemonListFragment : BaseFragment<FragmentPokemonListBinding, PokemonListFragmentViewModel>() {

    companion object {
        fun newInstance() = PokemonListFragment()
    }

    private val columnWidth = 400f
    private var isLoading: Boolean = false
    private lateinit var dataBinding: FragmentPokemonListBinding
    private lateinit var pokemonImagesAdapter: PokemonsAdapter
    private val layoutManager by lazy {
        GridLayoutManager(activity, UiUtility.calculateNoOfColumns(activity?.applicationContext, columnWidth))
    }

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            activity?.application?.let { PokemonListFragmentViewModel.PokemonListFragmentVMFactory(it) })
            .get(PokemonListFragmentViewModel::class.java)
    }
    var onPokemonSelectedCallBack: OnPokemonSelectedCallBack? = null

    override fun setActionBarTitle() {
        (activity as BaseActivity<*>).updateTitle(getString(R.string.app_name))
        (activity as BaseActivity<*>).showActionBarBackButton(false)
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun getDataBinding(inflater: LayoutInflater, container: ViewGroup?): ViewDataBinding {
        dataBinding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return dataBinding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.recyclerPokemon.layoutManager = layoutManager
        pokemonImagesAdapter = PokemonsAdapter(viewModel.pokemonUrls, onPokemonClickListener)
        dataBinding.recyclerPokemon.adapter = pokemonImagesAdapter
        dataBinding.container.setShimmer(
            Shimmer.AlphaHighlightBuilder()
                .setBaseAlpha(1f).setHighlightAlpha(0.1f).setAutoStart(false).build()
        )
        setLoadMoreListener()
        loadData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLiveDataObserver()
    }


    private fun loadData() {
        if (isNetworkConnected()) {
            isLoading = true
            dataBinding.container.startShimmer()
            viewModel.getPokemons()
        }
    }

    private fun setLoadMoreListener() {
        dataBinding.recyclerPokemon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val visibleThreshold = 5
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPOs = layoutManager.findLastVisibleItemPosition()
                if (!viewModel.isLast && !isLoading && totalItemCount <= lastVisibleItemPOs + visibleThreshold) {
                    loadData()
                }
            }
        })
    }

    private fun initLiveDataObserver() {
        val nameObserver = Observer<Triple<ApiStatus, PokemonApiType, Any>> { triple ->
            triple?.let {
                when (it.second) {

                    PokemonApiType.POKEMON_LIST_TYPE -> when (it.first) {
                        ApiStatus.SUCCESS, ApiStatus.FAILURE -> {
                            dataBinding.container.stopShimmer()
                            isLoading = false
                            pokemonImagesAdapter.notifyDataSetChanged()
                        }
                    }
                    PokemonApiType.POKEMON_DATA_TYPE -> when (it.first) {
                        ApiStatus.SUCCESS -> {
                            dataBinding.container.stopShimmer()
                            val pokemonData = it.third
                            if (pokemonData is PokemonData) {
                                onPokemonSelectedCallBack?.onPokemonSelected(pokemonData)
                            }
                        }
                        ApiStatus.FAILURE -> {
                            dataBinding.container.stopShimmer()
                        }
                    }
                }
            }
        }
        viewModel.liveData.observe(this, nameObserver)
    }

    fun onOrientationChanged() {
        layoutManager.spanCount = UiUtility.calculateNoOfColumns(activity?.applicationContext, columnWidth)
    }

    private val onPokemonClickListener = object : OnPokemonClickListener {
        override fun onPokemonClick(url: PokemonUrl) {
            url.url?.let {
                if (isNetworkConnected()) {
                    dataBinding.container.startShimmer()
                    viewModel.getPokemonData(it)
                }
            }
        }
    }
}

interface OnPokemonSelectedCallBack {
    fun onPokemonSelected(pokemonData: PokemonData)
}
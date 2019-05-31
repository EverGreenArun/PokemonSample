package com.arun.pokemonsample.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.arun.pokemonsample.R
import com.arun.pokemonsample.base.BaseActivity
import com.arun.pokemonsample.pojo.PokemonData


class HomeScreenActivity : BaseActivity<ViewDataBinding>(), FragmentManager.OnBackStackChangedListener {
    private val pokemonListFragment by lazy { PokemonListFragment.newInstance() }

    private val onPokemonSelectedCallBack = object : OnPokemonSelectedCallBack {
        override fun onPokemonSelected(pokemonData: PokemonData) {
            pokemonDetailFragment = PokemonDetailFragment.newInstance(pokemonData)
            pokemonDetailFragment?.let {
                addFragment(R.id.container,it , PokemonDetailFragment::class.java.name)
            }
        }
    }

    private var pokemonDetailFragment : PokemonDetailFragment? = null

    override fun getContentView(): Int = R.layout.activity_home_screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(R.id.container, pokemonListFragment, PokemonListFragment::class.java.name)
        pokemonListFragment.onPokemonSelectedCallBack = onPokemonSelectedCallBack
        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            pokemonListFragment.onOrientationChanged()
            pokemonDetailFragment?.onOrientationChanged()
        }
    }

    override fun onBackStackChanged() {
        val fragment = supportFragmentManager?.findFragmentById(R.id.container)
        if (fragment is PokemonListFragment)
            fragment.setActionBarTitle()

    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager?.findFragmentById(R.id.container)
        if (fragment is PokemonListFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
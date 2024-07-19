package com.rodcollab.mymarvelcomics.features.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rodcollab.mymarvelcomics.core.domain.di.CharacterDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val domain: CharacterDomain) :
    ViewModel() {

    private var _data = domain.characters().cachedIn(viewModelScope)
    val data = _data

    fun refresh() {
        _data = domain.characters().cachedIn(viewModelScope)
    }


}
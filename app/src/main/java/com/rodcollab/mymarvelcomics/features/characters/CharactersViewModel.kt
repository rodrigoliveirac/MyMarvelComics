package com.rodcollab.mymarvelcomics.features.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rodcollab.mymarvelcomics.core.domain.di.CharacterDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(domain: CharacterDomain) :
    ViewModel() {

    val data = domain.characters().cachedIn(viewModelScope)

}
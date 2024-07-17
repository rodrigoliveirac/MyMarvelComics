package com.rodcollab.mymarvelcomics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rodcollab.mymarvelcomics.domain.CharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(characters: CharactersUseCase) :
    ViewModel() {

        val data = characters().cachedIn(viewModelScope)


}
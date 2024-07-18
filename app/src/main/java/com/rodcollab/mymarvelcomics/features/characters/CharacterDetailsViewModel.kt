package com.rodcollab.mymarvelcomics.features.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rodcollab.mymarvelcomics.core.domain.di.CharacterDomain
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.ui.UiState
import com.rodcollab.mymarvelcomics.core.utils.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val domain: CharacterDomain,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])

    var comicsPaging = domain.comics(characterId).cachedIn(viewModelScope)
    private val _uiState = MutableStateFlow<UiState<CharacterExternal>>(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            domain.character(characterId) { resultOf ->
                when (resultOf) {
                    is ResultOf.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, model = resultOf.value)
                        }
                    }

                    is ResultOf.Failure -> {
                        _uiState.update {
                            it.copy(errorMsg = resultOf.message, isLoading = false)
                        }
                    }
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            comicsPaging = domain.comics(characterId).cachedIn(viewModelScope)
        }
    }

}
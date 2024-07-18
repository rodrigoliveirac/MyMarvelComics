package com.rodcollab.mymarvelcomics.features.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rodcollab.mymarvelcomics.core.domain.di.CharacterDomain
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.utils.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharacterDetailsUiState(
    val isLoading: Boolean = false,
    val character: CharacterExternal? = null,
    val errorMsg: String? = null,
)

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    domain: CharacterDomain,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])

    val comicsPaging = domain.comics(characterId).cachedIn(viewModelScope)
    private val _uiState = MutableStateFlow<CharacterDetailsUiState>(CharacterDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            domain.character(characterId) { resultOf ->
                when (resultOf) {
                    is ResultOf.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, character = resultOf.value)
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

}
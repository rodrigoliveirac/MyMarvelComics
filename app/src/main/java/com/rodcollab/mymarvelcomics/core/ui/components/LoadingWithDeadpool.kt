package com.rodcollab.mymarvelcomics.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rodcollab.mymarvelcomics.R

@Composable
fun LoadingWithDeadPool(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = R.drawable.img_deadpool_waiting),
            contentDescription = null
        )
        LinearProgressIndicator(
            modifier = modifier
                .heightIn(max = 2.dp)
        )
    }
}
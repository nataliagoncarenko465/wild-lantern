package com.Razxoxo5.Toqsam.presentation.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Razxoxo5.Toqsam.R
import com.Razxoxo5.Toqsam.core.di.ServiceLocator
import com.Razxoxo5.Toqsam.core.theme.BackgroundBase
import com.Razxoxo5.Toqsam.core.theme.LanternGold
import com.Razxoxo5.Toqsam.core.theme.SurfaceElevated
import com.Razxoxo5.Toqsam.core.theme.SurfaceNight
import com.Razxoxo5.Toqsam.core.theme.TextPrimary
import com.Razxoxo5.Toqsam.core.theme.TextSecondary
import com.Razxoxo5.Toqsam.core.ui.BadgePill
import com.Razxoxo5.Toqsam.core.ui.ScreenBackground
import com.Razxoxo5.Toqsam.core.ui.SecondaryButton
import com.Razxoxo5.Toqsam.core.ui.TopBarRow
import com.Razxoxo5.Toqsam.presentation.dialog.SceneDialog

@Composable
fun CollectionRoute(onMenu: () -> Unit) {
    val collectionViewModel: CollectionViewModel =
        viewModel(factory = ServiceLocator.viewModelFactory)
    val state by collectionViewModel.state.collectAsStateWithLifecycle()

    CollectionScreen(
        state = state,
        onSceneSelected = collectionViewModel::onSceneSelected,
        onMenu = onMenu
    )

    val selected = state.selected
    if (selected != null) {
        SceneDialog(
            sceneName = selected.name,
            condition = selected.condition,
            onDismiss = collectionViewModel::onSceneDismissed
        )
    }
}

@Composable
private fun CollectionScreen(
    state: CollectionUiState,
    onSceneSelected: (String) -> Unit,
    onMenu: () -> Unit
) {
    val lockedLabel = stringResource(R.string.state_locked)
    val unlockedLabel = stringResource(R.string.state_unlocked)
    val lockedTitle = stringResource(R.string.locked)

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(
                SurfaceNight.copy(alpha = 0.88f),
                BackgroundBase.copy(alpha = 0.93f)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            TopBarRow(
                title = stringResource(R.string.collection),
                onBack = onMenu,
                trailing = {
                    BadgePill(
                        iconRes = R.drawable.sprite_3,
                        text = state.unlocked.toString() + SEPARATOR + state.total,
                        iconDescription = stringResource(R.string.cd_badge)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 12.dp),
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) {
                items(items = state.scenes, key = { scene -> scene.id }) { scene ->
                    SceneTile(
                        scene = scene,
                        lockedTitle = lockedTitle,
                        stateLabel = if (scene.unlocked) unlockedLabel else lockedLabel,
                        onClick = { onSceneSelected(scene.id) }
                    )
                }
            }

            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                iconRes = R.drawable.ic_back,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SceneTile(
    scene: SceneCard,
    lockedTitle: String,
    stateLabel: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceNight.copy(alpha = 0.9f),
        border = BorderStroke(
            1.dp,
            if (scene.unlocked) LanternGold.copy(alpha = 0.6f) else TextSecondary.copy(alpha = 0.2f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .semantics { stateDescription = stateLabel }
            .clickable(enabled = scene.unlocked, onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(SurfaceElevated, SurfaceNight))
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(if (scene.unlocked) 1f else 0.35f)
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_2),
                    contentDescription = if (scene.unlocked) scene.name else null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (scene.unlocked) scene.name else lockedTitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (scene.unlocked) TextPrimary else TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
            if (!scene.unlocked) {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp).size(26.dp)
                )
            }
        }
    }
}

private const val SEPARATOR = " / "

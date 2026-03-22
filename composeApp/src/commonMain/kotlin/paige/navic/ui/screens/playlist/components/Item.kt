package paige.navic.ui.screens.playlist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.zt64.subsonic.api.model.Playlist
import navic.composeapp.generated.resources.Res
import navic.composeapp.generated.resources.action_delete
import navic.composeapp.generated.resources.action_share
import navic.composeapp.generated.resources.count_songs
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import paige.navic.LocalCtx
import paige.navic.LocalNavStack
import paige.navic.data.models.Screen
import paige.navic.icons.Icons
import paige.navic.icons.outlined.PlaylistRemove
import paige.navic.icons.outlined.Share
import paige.navic.ui.components.common.Dropdown
import paige.navic.ui.components.common.DropdownItem
import paige.navic.ui.components.layouts.ArtGridItem
import paige.navic.ui.screens.playlist.viewmodels.PlaylistListViewModel

@Composable
fun PlaylistListScreenItem(
	modifier: Modifier = Modifier,
	playlist: Playlist,
	tab: String,
	viewModel: PlaylistListViewModel,
	onSetShareId: (String) -> Unit,
	onSetDeletionId: (String) -> Unit
) {
	val ctx = LocalCtx.current
	val backStack = LocalNavStack.current
	val selection by viewModel.selectedPlaylist.collectAsState()
	Box(modifier) {
		ArtGridItem(
			onClick = {
				ctx.clickSound()
				backStack.add(Screen.Tracks(playlist, "playlists"))
			},
			onLongClick = { viewModel.selectPlaylist(playlist) },
			coverArtId = playlist.coverArtId,
			title = playlist.name,
			subtitle = buildString {
				append(
					pluralStringResource(
						Res.plurals.count_songs,
						playlist.songCount,
						playlist.songCount
					)
				)
				playlist.comment?.let {
					append("\n${playlist.comment}\n")
				}
			},
			id = playlist.id,
			tab = tab
		)
		Dropdown(
			expanded = selection == playlist,
			onDismissRequest = {
				viewModel.clearSelection()
			}
		) {
			DropdownItem(
				text = { Text(stringResource(Res.string.action_share)) },
				leadingIcon = { Icon(Icons.Outlined.Share, null) },
				onClick = {
					onSetShareId(playlist.id)
					viewModel.clearSelection()
				},
			)
			DropdownItem(
				text = { Text(stringResource(Res.string.action_delete)) },
				leadingIcon = { Icon(Icons.Outlined.PlaylistRemove, null) },
				onClick = {
					onSetDeletionId(playlist.id)
					viewModel.clearSelection()
				}
			)
		}
	}
}

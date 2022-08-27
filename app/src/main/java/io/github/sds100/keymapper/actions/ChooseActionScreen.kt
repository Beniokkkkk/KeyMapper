package io.github.sds100.keymapper.actions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import io.github.sds100.keymapper.R
import io.github.sds100.keymapper.actions.sound.ChooseSoundResult
import io.github.sds100.keymapper.actions.tapscreen.PickCoordinateResult
import io.github.sds100.keymapper.destinations.*
import io.github.sds100.keymapper.system.apps.ChooseAppShortcutResult
import io.github.sds100.keymapper.system.camera.CameraLens
import io.github.sds100.keymapper.system.display.Orientation
import io.github.sds100.keymapper.system.inputmethod.ImeInfo
import io.github.sds100.keymapper.system.intents.ConfigIntentResult
import io.github.sds100.keymapper.system.volume.DndMode
import io.github.sds100.keymapper.system.volume.RingerMode
import io.github.sds100.keymapper.system.volume.VolumeStream
import io.github.sds100.keymapper.system.volume.VolumeStreamUtils
import io.github.sds100.keymapper.util.ui.*

@Destination
@Composable
fun ChooseActionScreen(
    viewModel: ChooseActionViewModel2,
    navigator: DestinationsNavigator,
    resultBackNavigator: ResultBackNavigator<ActionData>,
    keyCodeResultRecipient: ResultRecipient<ChooseKeyCodeScreenDestination, Int>,
    appResultRecipient: ResultRecipient<ChooseAppScreenDestination, String>,
    appShortcutResultRecipient: ResultRecipient<ChooseAppShortcutScreenDestination, ChooseAppShortcutResult>,
    tapScreenActionResultRecipient: ResultRecipient<CreateTapScreenActionScreenDestination, PickCoordinateResult>,
    chooseSoundResultRecipient: ResultRecipient<ChooseSoundScreenDestination, ChooseSoundResult>,
    configKeyEventResultRecipient: ResultRecipient<ConfigKeyEventScreenDestination, ActionData.InputKeyEvent>,
    configIntentResultRecipient: ResultRecipient<ConfigIntentScreenDestination, ConfigIntentResult>
) {
    keyCodeResultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                viewModel.dismissConfiguringAction()
            }

            is NavResult.Value -> {
                viewModel.onChooseKeyCode(result.value)
            }
        }
    }

    appResultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                viewModel.dismissConfiguringAction()
            }

            is NavResult.Value -> {
                viewModel.onChooseApp(result.value)
            }
        }
    }

    appShortcutResultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                viewModel.dismissConfiguringAction()
            }

            is NavResult.Value -> {
                viewModel.onChooseAppShortcut(result.value)
            }
        }
    }

    tapScreenActionResultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                viewModel.dismissConfiguringAction()
            }

            is NavResult.Value -> {
                viewModel.onCreateTapScreenAction(result.value)
            }
        }
    }

    chooseSoundResultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                viewModel.dismissConfiguringAction()
            }

            is NavResult.Value -> {
                viewModel.onChooseSound(result.value)
            }
        }
    }

    configKeyEventResultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                viewModel.dismissConfiguringAction()
            }

            is NavResult.Value -> {
                viewModel.onConfigKeyEvent(result.value)
            }
        }
    }

    configIntentResultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {
                viewModel.dismissConfiguringAction()
            }

            is NavResult.Value -> {
                viewModel.onConfigIntent(result.value)
            }
        }
    }

    ChooseActionScreen(
        viewModel = viewModel,
        navigateBack = { result ->
            if (result == null) {
                resultBackNavigator.navigateBack()
            } else {
                resultBackNavigator.navigateBack(result)
            }
        },
        navigateToChooseKeyCode = {
            viewModel.onNavigateToConfigScreen()
            navigator.navigate(ChooseKeyCodeScreenDestination)
        },
        navigateToChooseApp = {
            viewModel.onNavigateToConfigScreen()
            navigator.navigate(ChooseAppScreenDestination(allowHiddenApps = true))
        },
        navigateToChooseAppShortcut = {
            viewModel.onNavigateToConfigScreen()
            navigator.navigate(ChooseAppShortcutScreenDestination)
        },
        navigateToCreateTapScreenAction = {
            viewModel.onNavigateToConfigScreen()
            navigator.navigate(CreateTapScreenActionScreenDestination)
        },
        navigateToChooseSound = {
            viewModel.onNavigateToConfigScreen()
            navigator.navigate(ChooseSoundScreenDestination)
        },
        navigateToConfigKeyEvent = {
            viewModel.onNavigateToConfigScreen()
            navigator.navigate(ConfigKeyEventScreenDestination)
        },
        navigateToConfigIntent = {
            viewModel.onNavigateToConfigScreen()
            navigator.navigate(ConfigIntentScreenDestination)
        }
    )
}

/**
 * Created by sds100 on 12/07/2022.
 */

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
private fun ChooseActionScreenPreview() {
    MaterialTheme {
        ChooseActionScreen(
            actions = listOf(
                ChooseActionListItem.Header("Keyboard"),
                ChooseActionListItem.Action(
                    ActionId.SWITCH_KEYBOARD,
                    "Switch keyboard",
                    KMIcon.ImageVector(Icons.Outlined.Keyboard),
                    error = null,
                    isEnabled = true
                ),
                ChooseActionListItem.Action(
                    ActionId.SHOW_KEYBOARD,
                    "Show keyboard",
                    KMIcon.ImageVector(Icons.Outlined.Keyboard),
                    error = "Requires Android 11",
                    isEnabled = false
                )
            ),
            searchState = SearchState.Idle
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
private fun ChooseActionScreenPreview_Searching() {
    MaterialTheme {
        ChooseActionScreen(
            actions = listOf(
                ChooseActionListItem.Header("Keyboard"),
                ChooseActionListItem.Action(
                    ActionId.SWITCH_KEYBOARD,
                    "Switch keyboard",
                    KMIcon.ImageVector(Icons.Outlined.Keyboard),
                    error = null,
                    isEnabled = true
                )
            ),
            searchState = SearchState.Searching("Search")
        )
    }
}

@Composable
private fun ChooseActionScreen(
    modifier: Modifier = Modifier,
    viewModel: ChooseActionViewModel2,
    navigateBack: (result: ActionData?) -> Unit,
    navigateToChooseKeyCode: () -> Unit,
    navigateToChooseApp: () -> Unit,
    navigateToChooseAppShortcut: () -> Unit,
    navigateToCreateTapScreenAction: () -> Unit,
    navigateToChooseSound: () -> Unit,
    navigateToConfigKeyEvent: () -> Unit,
    navigateToConfigIntent: () -> Unit,
) {
    val searchState by viewModel.searchState.collectAsState()
    val configActionState = viewModel.configActionState

    if (configActionState is ConfigActionState.Finished) {
        if (configActionState.warning != null) {
            AlertDialog(onDismissRequest = viewModel::dismissConfiguringAction,
                icon = { Icon(Icons.Outlined.Warning, contentDescription = null) },
                title = { Text(stringResource(R.string.choose_action_warning_title)) },
                text = { Text(configActionState.warning) },
                confirmButton = {
                    TextButton(onClick = {
                        navigateBack(configActionState.action)
                    }) {
                        Text(stringResource(R.string.pos_ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::dismissConfiguringAction) {
                        Text(stringResource(R.string.neg_cancel))
                    }
                })
        } else {
            navigateBack(configActionState.action)
        }
    }

    if (configActionState is ConfigActionState.Screen) {
        when (configActionState) {
            is ConfigActionState.Screen.Navigated -> {}
            ConfigActionState.Screen.ChooseKeycode -> navigateToChooseKeyCode()
            is ConfigActionState.Screen.ChooseApp -> navigateToChooseApp()
            ConfigActionState.Screen.ChooseAppShortcut -> navigateToChooseAppShortcut()
            ConfigActionState.Screen.CreateTapScreenAction -> navigateToCreateTapScreenAction()
            ConfigActionState.Screen.ChooseSound -> navigateToChooseSound()
            ConfigActionState.Screen.ConfigKeyEvent -> navigateToConfigKeyEvent()
            ConfigActionState.Screen.ConfigIntent -> navigateToConfigIntent()
        }
    }

    val dialogState = if (configActionState is ConfigActionState.Dialog) {
        configActionState
    } else {
        ConfigActionState.Dialog.Hidden
    }

    ChooseActionScreen(
        modifier = modifier,
        actions = viewModel.actionListItems,
        searchState = searchState,
        setSearchState = viewModel::setSearchState,
        dialogState = dialogState,
        onDismissConfiguringAction = viewModel::dismissConfiguringAction,
        onActionClick = viewModel::onActionClick,
        onBack = { navigateBack(null) },
        onChooseInputMethod = viewModel::onChooseInputMethod,
        onCreateTextAction = viewModel::onCreateTextAction,
        onCreateUrlAction = viewModel::onCreateUrlAction,
        onCreatePhoneCallAction = viewModel::onCreatePhoneCallAction,
        onConfigCycleRotationsAction = viewModel::onConfigCycleRotations,
        onConfigVolumeAction = viewModel::onConfigVolumeAction,
        onChooseRingerMode = viewModel::onChooseRingerMode,
        onChooseDoNotDisturbMode = viewModel::onChooseDoNotDisturbMode,
        onChooseFlashlight = viewModel::onChooseFlashlight
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChooseActionScreen(
    modifier: Modifier = Modifier,
    actions: List<ChooseActionListItem>,
    searchState: SearchState,
    setSearchState: (SearchState) -> Unit = {},
    dialogState: ConfigActionState.Dialog = ConfigActionState.Dialog.Hidden,
    onDismissConfiguringAction: () -> Unit = {},
    onActionClick: (ActionId) -> Unit = {},
    onBack: () -> Unit = {},
    onChooseInputMethod: (ImeInfo) -> Unit = {},
    onCreateTextAction: (String) -> Unit = {},
    onCreateUrlAction: (String) -> Unit = {},
    onCreatePhoneCallAction: (String) -> Unit = {},
    onConfigCycleRotationsAction: (List<Orientation>) -> Unit = {},
    onConfigVolumeAction: (Boolean, VolumeStream) -> Unit = { _, _ -> },
    onChooseRingerMode: (RingerMode) -> Unit = {},
    onChooseDoNotDisturbMode: (DndMode) -> Unit = {},
    onChooseFlashlight: (CameraLens) -> Unit = {}
) {
    Scaffold(modifier, bottomBar = {
        SearchAppBar(onBack, searchState, setSearchState) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.choose_action_back_content_description)
                )
            }
        }
    }) { padding ->
        when (dialogState) {
            ConfigActionState.Dialog.Hidden -> {}
            is ConfigActionState.Dialog.ChooseKeyboard ->
                ChooseInputMethodDialog(
                    inputMethods = dialogState.inputMethods,
                    onDismissRequest = onDismissConfiguringAction,
                    onConfirmClick = onChooseInputMethod
                )
            ConfigActionState.Dialog.Text -> {
                var text by rememberSaveable { mutableStateOf("") }
                val emptyErrorString = stringResource(R.string.choose_action_text_empty_error)
                val error by remember {
                    derivedStateOf {
                        when {
                            text.isEmpty() -> emptyErrorString
                            else -> null
                        }
                    }
                }

                TextFieldDialog(
                    text = text,
                    title = stringResource(R.string.choose_action_text_action_title),
                    label = stringResource(R.string.choose_action_text_action_label),
                    error = error,
                    onTextChange = { text = it },
                    onConfirm = onCreateTextAction,
                    onDismiss = onDismissConfiguringAction
                )
            }
            ConfigActionState.Dialog.Url -> {
                var text by rememberSaveable { mutableStateOf("") }
                val emptyErrorString = stringResource(R.string.choose_action_url_empty_error)
                val error by remember {
                    derivedStateOf {
                        when {
                            text.isEmpty() -> emptyErrorString
                            else -> null
                        }
                    }
                }

                TextFieldDialog(
                    text = text,
                    title = stringResource(R.string.choose_action_url_action_title),
                    label = stringResource(R.string.choose_action_url_action_label),
                    error = error,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    onTextChange = { text = it },
                    onConfirm = onCreateUrlAction,
                    onDismiss = onDismissConfiguringAction
                )
            }
            ConfigActionState.Dialog.PhoneCall -> {
                var text by rememberSaveable { mutableStateOf("") }
                val emptyErrorString = stringResource(R.string.choose_action_phone_empty_error)
                val error by remember {
                    derivedStateOf {
                        when {
                            text.isEmpty() -> emptyErrorString
                            else -> null
                        }
                    }
                }

                TextFieldDialog(
                    text = text,
                    title = stringResource(R.string.choose_action_phone_action_title),
                    label = stringResource(R.string.choose_action_phone_action_label),
                    error = error,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    onTextChange = { text = it },
                    onConfirm = onCreatePhoneCallAction,
                    onDismiss = onDismissConfiguringAction
                )
            }
            ConfigActionState.Dialog.CycleRotations -> {
                ConfigCycleRotationDialog(
                    onDismissRequest = onDismissConfiguringAction,
                    onConfirmClick = onConfigCycleRotationsAction
                )
            }
            is ConfigActionState.Dialog.Volume -> {
                val titleRes = when (dialogState) {
                    ConfigActionState.Dialog.Volume.Down -> R.string.choose_action_config_volume_down_action_dialog_title
                    ConfigActionState.Dialog.Volume.Mute -> R.string.choose_action_config_volume_mute_action_dialog_title
                    ConfigActionState.Dialog.Volume.ToggleMute -> R.string.choose_action_config_volume_toggle_mute_action_dialog_title
                    ConfigActionState.Dialog.Volume.Unmute -> R.string.choose_action_config_volume_unmute_action_dialog_title
                    ConfigActionState.Dialog.Volume.Up -> R.string.choose_action_config_volume_up_action_dialog_title
                }

                ConfigVolumeActionDialog(
                    title = stringResource(titleRes),
                    onConfirm = onConfigVolumeAction,
                    onDismiss = onDismissConfiguringAction
                )
            }
            ConfigActionState.Dialog.RingerMode -> {
                ChooseRingerModeDialog(
                    onConfirm = onChooseRingerMode,
                    onDismiss = onDismissConfiguringAction
                )
            }
            is ConfigActionState.Dialog.DoNotDisturb -> {
                val titleRes = when (dialogState) {
                    ConfigActionState.Dialog.DoNotDisturb.Enable -> R.string.choose_action_choose_enable_dnd_mode_dialog_title
                    ConfigActionState.Dialog.DoNotDisturb.Toggle -> R.string.choose_action_choose_toggle_dnd_mode_dialog_title
                }

                ChooseDoNotDisturbModeDialog(
                    title = stringResource(titleRes),
                    onConfirm = onChooseDoNotDisturbMode,
                    onDismiss = onDismissConfiguringAction
                )
            }

            is ConfigActionState.Dialog.Flashlight -> {
                val titleRes = when (dialogState) {
                    ConfigActionState.Dialog.Flashlight.Disable -> R.string.choose_action_choose_disable_flashlight_dialog_title
                    ConfigActionState.Dialog.Flashlight.Enable -> R.string.choose_action_choose_enable_flashlight_dialog_title
                    ConfigActionState.Dialog.Flashlight.Toggle -> R.string.choose_action_choose_toggle_flashlight_dialog_title
                }

                ChooseFlashlightDialog(
                    title = stringResource(titleRes),
                    onConfirm = onChooseFlashlight,
                    onDismiss = onDismissConfiguringAction
                )
            }
        }

        ChooseActionList(
            Modifier
                .padding(padding)
                .fillMaxSize(),
            actions,
            onActionClick
        )
    }
}

@Composable
private fun ChooseActionList(
    modifier: Modifier = Modifier,
    listItems: List<ChooseActionListItem>,
    onActionClick: (ActionId) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            listItems,
            key = { listItem ->
                when (listItem) {
                    is ChooseActionListItem.Action -> listItem.id
                    is ChooseActionListItem.Header -> listItem.header
                }
            },
            contentType = { listItem ->
                when (listItem) {
                    is ChooseActionListItem.Action -> 0
                    is ChooseActionListItem.Header -> 1
                }
            }
        ) { listItem ->
            when (listItem) {
                is ChooseActionListItem.Action ->
                    ActionListItem(
                        modifier = Modifier.fillMaxWidth(),
                        listItem = listItem,
                        onClick = { onActionClick(listItem.id) })

                is ChooseActionListItem.Header ->
                    HeaderListItem(
                        modifier = Modifier.fillMaxWidth(),
                        text = listItem.header
                    )
            }
        }
    }
}

@Composable
private fun ActionListItem(modifier: Modifier, listItem: ChooseActionListItem.Action, onClick: () -> Unit) {
    if (listItem.error == null) {
        SimpleListItem(
            modifier = modifier,
            icon = listItem.icon,
            title = listItem.title,
            onClick = onClick
        )
    } else {
        SimpleListItem(
            modifier = modifier,
            icon = {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                    icon = listItem.icon
                )

                Spacer(Modifier.width(16.dp))
            },
            title = {
                Text(listItem.title, style = MaterialTheme.typography.bodyMedium)
            },
            subtitle = {
                Text(listItem.error, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            },
            enabled = listItem.isEnabled,
            onClick = onClick
        )
    }
}

@Composable
private fun ConfigCycleRotationDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmClick: (List<Orientation>) -> Unit = {},
) {
    var orientations by rememberSaveable { mutableStateOf(emptySet<Orientation>()) }

    CustomDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(R.string.choose_action_cycle_orientations_dialog_title),
        confirmButton = {
            TextButton(
                onClick = { onConfirmClick(orientations.toList()) }, enabled = orientations.isNotEmpty()
            ) {
                Text(stringResource(R.string.pos_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.neg_cancel))
            }
        }
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            CheckBoxWithText(
                modifier = Modifier.fillMaxWidth(),
                isChecked = orientations.contains(Orientation.ORIENTATION_0),
                text = { Text(stringResource(R.string.orientation_0)) },
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        orientations = orientations + Orientation.ORIENTATION_0
                    } else {
                        orientations = orientations - Orientation.ORIENTATION_0
                    }
                }
            )
            CheckBoxWithText(
                modifier = Modifier.fillMaxWidth(),
                isChecked = orientations.contains(Orientation.ORIENTATION_90),
                text = { Text(stringResource(R.string.orientation_90)) },
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        orientations = orientations + Orientation.ORIENTATION_90
                    } else {
                        orientations = orientations - Orientation.ORIENTATION_90
                    }
                }
            )
            CheckBoxWithText(
                modifier = Modifier.fillMaxWidth(),
                isChecked = orientations.contains(Orientation.ORIENTATION_180),
                text = { Text(stringResource(R.string.orientation_180)) },
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        orientations = orientations + Orientation.ORIENTATION_180
                    } else {
                        orientations = orientations - Orientation.ORIENTATION_180
                    }
                }
            )
            CheckBoxWithText(
                modifier = Modifier.fillMaxWidth(),
                isChecked = orientations.contains(Orientation.ORIENTATION_270),
                text = { Text(stringResource(R.string.orientation_270)) },
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        orientations = orientations + Orientation.ORIENTATION_270
                    } else {
                        orientations = orientations - Orientation.ORIENTATION_270
                    }
                }
            )
        }
    }
}

@Composable
private fun ChooseInputMethodDialog(
    inputMethods: List<ImeInfo>,
    onDismissRequest: () -> Unit = {},
    onConfirmClick: (ImeInfo) -> Unit = {},
) {
    var selectedIme: ImeInfo? by remember { mutableStateOf(null) }

    CustomDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(R.string.choose_action_choose_keyboard_dialog_title),
        confirmButton = {
            TextButton(
                onClick = { onConfirmClick(selectedIme!!) }, enabled = selectedIme != null
            ) {
                Text(stringResource(R.string.pos_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.neg_cancel))
            }
        }
    ) {
        LazyColumn {
            items(inputMethods) { imeInfo ->
                RadioButtonWithText(
                    modifier = Modifier.fillMaxWidth(),
                    isSelected = selectedIme == imeInfo,
                    text = imeInfo.label,
                    onClick = { selectedIme = imeInfo }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChooseInputMethodDialogPreview() {
    MaterialTheme {
        ChooseInputMethodDialog(
            inputMethods = listOf(
                ImeInfo("id0", "package0", "Gboard", isEnabled = true, isChosen = true),
                ImeInfo("id1", "package1", "Key Mapper GUI Keyboard", isEnabled = true, isChosen = true),
            )
        )
    }
}

@Composable
private fun ConfigVolumeActionDialog(
    title: String,
    onConfirm: (showVolumeDialog: Boolean, stream: VolumeStream) -> Unit = { _, _ -> },
    onDismiss: () -> Unit = {}
) {
    var showVolumeDialog: Boolean by rememberSaveable { mutableStateOf(true) }
    var selectedStream: VolumeStream by rememberSaveable { mutableStateOf(VolumeStream.DEFAULT) }

    CustomDialog(
        title = title,
        confirmButton = {
            TextButton(onClick = { onConfirm(showVolumeDialog, selectedStream) }) {
                Text(stringResource(R.string.pos_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.neg_cancel))
            }
        }
    ) {
        Column {
            CheckBoxWithText(
                isChecked = showVolumeDialog,
                text = { Text(stringResource(R.string.choose_action_config_show_volume_ui_checkbox)) },
                onCheckedChange = { showVolumeDialog = it }
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.choose_action_config_volume_stream_header),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(Modifier.fillMaxWidth()) {
                items(VolumeStream.values()) { stream ->
                    RadioButtonWithText(
                        modifier = Modifier.fillMaxWidth(),
                        isSelected = selectedStream == stream,
                        text = stringResource(VolumeStreamUtils.getLabel(stream)),
                        onClick = { selectedStream = stream }
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 600, heightDp = 400)
@Composable
private fun ConfigVolumeActionDialogPreview() {
    MaterialTheme {
        ConfigVolumeActionDialog(
            title = stringResource(R.string.choose_action_config_volume_down_action_dialog_title)
        )
    }
}

@Composable
private fun ChooseRingerModeDialog(
    onConfirm: (RingerMode) -> Unit = { _ -> },
    onDismiss: () -> Unit = {}
) {
    var selectedRingerMode: RingerMode? by rememberSaveable { mutableStateOf(null) }

    CustomDialog(
        title = stringResource(R.string.choose_action_choose_ringer_mode_dialog_title),
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedRingerMode!!) }, enabled = selectedRingerMode != null) {
                Text(stringResource(R.string.pos_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.neg_cancel))
            }
        }
    ) {
        Column(Modifier.fillMaxWidth()) {
            RadioButtonWithText(
                modifier = Modifier.fillMaxWidth(),
                isSelected = selectedRingerMode == RingerMode.NORMAL,
                text = stringResource(R.string.ringer_mode_normal),
                onClick = { selectedRingerMode = RingerMode.NORMAL }
            )
            RadioButtonWithText(
                modifier = Modifier.fillMaxWidth(),
                isSelected = selectedRingerMode == RingerMode.VIBRATE,
                text = stringResource(R.string.ringer_mode_vibrate),
                onClick = { selectedRingerMode = RingerMode.VIBRATE }
            )
            RadioButtonWithText(
                modifier = Modifier.fillMaxWidth(),
                isSelected = selectedRingerMode == RingerMode.SILENT,
                text = stringResource(R.string.ringer_mode_silent),
                onClick = { selectedRingerMode = RingerMode.SILENT }
            )
        }
    }
}

@Preview
@Composable
private fun ChooseRingerModeDialogPreview() {
    MaterialTheme {
        ChooseRingerModeDialog()
    }
}

@Composable
private fun ChooseDoNotDisturbModeDialog(
    title: String,
    onConfirm: (DndMode) -> Unit = { _ -> },
    onDismiss: () -> Unit = {}
) {
    var selectedMode: DndMode? by rememberSaveable { mutableStateOf(null) }

    CustomDialog(
        title = title,
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedMode!!) }, enabled = selectedMode != null) {
                Text(stringResource(R.string.pos_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.neg_cancel))
            }
        }
    ) {
        Column(Modifier.fillMaxWidth()) {
            RadioButtonWithText(
                modifier = Modifier.fillMaxWidth(),
                isSelected = selectedMode == DndMode.ALARMS,
                text = stringResource(R.string.dnd_mode_alarms),
                onClick = { selectedMode = DndMode.ALARMS }
            )
            RadioButtonWithText(
                modifier = Modifier.fillMaxWidth(),
                isSelected = selectedMode == DndMode.NONE,
                text = stringResource(R.string.dnd_mode_none),
                onClick = { selectedMode = DndMode.NONE }
            )
            RadioButtonWithText(
                modifier = Modifier.fillMaxWidth(),
                isSelected = selectedMode == DndMode.PRIORITY,
                text = stringResource(R.string.dnd_mode_priority),
                onClick = { selectedMode = DndMode.PRIORITY }
            )
        }
    }
}

@Composable
private fun ChooseFlashlightDialog(
    title: String,
    onConfirm: (CameraLens) -> Unit = { _ -> },
    onDismiss: () -> Unit = {}
) {
    var selectedFlashlight: CameraLens by rememberSaveable { mutableStateOf(CameraLens.BACK) }

    CustomDialog(
        title = title,
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedFlashlight) }) {
                Text(stringResource(R.string.pos_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.neg_cancel))
            }
        }
    ) {
        Column(Modifier.fillMaxWidth()) {
            RadioButtonWithText(
                modifier = Modifier.fillMaxWidth(),
                isSelected = selectedFlashlight == CameraLens.BACK,
                text = stringResource(R.string.lens_back),
                onClick = { selectedFlashlight = CameraLens.BACK }
            )
            RadioButtonWithText(
                modifier = Modifier.fillMaxWidth(),
                isSelected = selectedFlashlight == CameraLens.FRONT,
                text = stringResource(R.string.lens_front),
                onClick = { selectedFlashlight = CameraLens.FRONT }
            )
        }
    }
}
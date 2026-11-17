package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.CityCatalog
import com.example.data.LocalAppStrings
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTargetDialog(
  onConfirmEpochMillis: (Long) -> Unit,
  onDismiss: () -> Unit,
) {
  val strings = LocalAppStrings.current
  val defaultMillis = remember {
    CityCatalog.GTA_VI_LAUNCH_LOCAL.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
  }
  val datePickerState = rememberDatePickerState(initialSelectedDateMillis = defaultMillis)
  val timePickerState = rememberTimePickerState(initialHour = 0, initialMinute = 0, is24Hour = true)
  var showTimeStep by remember { mutableStateOf(false) }

  if (!showTimeStep) {
    DatePickerDialog(
      onDismissRequest = onDismiss,
      confirmButton = {
        TextButton(
          onClick = { showTimeStep = true },
          modifier = Modifier.testTag("custom_date_next_button"),
        ) {
          Text(strings.nextTimeButton)
        }
      },
      dismissButton = {
        TextButton(onClick = onDismiss) {
          Text(strings.cancelButton)
        }
      },
    ) {
      DatePicker(state = datePickerState)
    }
  } else {
    AlertDialog(
      onDismissRequest = onDismiss,
      containerColor = MaterialTheme.colorScheme.surface,
      title = { Text(strings.customTimeTitle) },
      text = {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          TimePicker(state = timePickerState)
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val selectedDateMillis = datePickerState.selectedDateMillis ?: defaultMillis
            val pickedDate: LocalDate = Instant.ofEpochMilli(selectedDateMillis)
              .atZone(ZoneOffset.UTC)
              .toLocalDate()
            val pickedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
            val epoch = pickedDate.atTime(pickedTime).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            onConfirmEpochMillis(epoch)
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          modifier = Modifier.testTag("custom_target_confirm_button"),
        ) {
          Text(strings.startCountdownButton)
        }
      },
      dismissButton = {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedButton(onClick = { showTimeStep = false }) {
            Text(strings.backButton)
          }
          TextButton(onClick = onDismiss) {
            Text(strings.cancelButton)
          }
        }
      },
    )
  }
}

package com.abi.abifinal.domain.model

import android.os.Parcelable
import com.abi.abifinal.presentation.utils.TimesOfDay
import java.util.Date
import kotlinx.parcelize.Parcelize

@Parcelize
data class Medication(
    val id: Long?,
    val name: String,
    val dosage: Int,
    val recurrence: String,
    val endDate: Date,
    val timesOfDay: List<TimesOfDay>,
    val medicationTaken: Boolean,
    val date: Date
) : Parcelable

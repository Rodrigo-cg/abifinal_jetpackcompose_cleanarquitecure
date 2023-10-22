package com.abi.abifinal.presentation.screens.medicamentos

import com.abi.abifinal.domain.model.Medication


data class MedicamentosState(
    val greeting: String = "",
    val userName: String = "",
    val medications: List<Medication> = emptyList()
)

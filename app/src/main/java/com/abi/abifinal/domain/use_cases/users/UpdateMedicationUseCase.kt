package com.abi.abifinal.domain.use_cases.users

import com.abi.abifinal.domain.model.Medication
import com.abi.abifinal.domain.repository.MedicationRepository

import javax.inject.Inject

class UpdateMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {

    suspend fun updateMedication(medication: Medication) {
        return repository.updateMedication(medication)
    }
}

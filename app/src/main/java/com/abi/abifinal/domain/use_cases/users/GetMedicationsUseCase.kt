package com.abi.abifinal.domain.use_cases.users

import com.abi.abifinal.domain.model.Medication
import com.abi.abifinal.domain.repository.MedicationRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMedicationsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {

    suspend fun getMedications(): Flow<List<Medication>> {
        return repository.getAllMedications()
    }
}

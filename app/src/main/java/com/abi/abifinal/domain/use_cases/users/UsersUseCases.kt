package com.abi.abifinal.domain.use_cases.users

class UsersUseCases (
    val create: Create,
    val getUserById: GetUserById,
    val saveImage : SaveImage,
    val update: Update,
    val getParametersBle: GetParametersBle,
    val getCurrentUserLocationUseCase: GetCurrentUserLocationUseCase
    )

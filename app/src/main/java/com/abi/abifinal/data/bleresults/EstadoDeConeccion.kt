package com.abi.abifinal.data.bleresults

sealed interface EstadoDeConeccion{
    object Connected: EstadoDeConeccion
    object Disconnected: EstadoDeConeccion
    object Uninitialized: EstadoDeConeccion
    object CurrentlyInitializing: EstadoDeConeccion
}
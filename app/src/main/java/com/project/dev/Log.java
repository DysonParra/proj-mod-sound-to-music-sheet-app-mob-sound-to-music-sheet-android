/*
 * @fileoverview {Log} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {Log} fue realizada el 31/07/2022.
 * @Dev - La primera version de {Log} fue escrita por Dyson A. Parra T.
 */
package com.project.dev;

/**
 * TODO: Definición de {@code Log}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class Log {

    /**
     * TODO: Definición de {@code d}.
     *
     * @param Type
     * @param message
     */
    public static void d(String Type, String message) {
        //System.out.printf("%s\n", message);
        android.util.Log.d("%s\n", message);
    }
}

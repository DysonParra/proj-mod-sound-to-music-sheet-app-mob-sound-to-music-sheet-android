/*
 * @fileoverview    {Log} se encarga de realizar tareas específicas.
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementación realizada.
 * @version 2.0     Documentación agregada.
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

/*
 * @fileoverview    {Log}
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementation done.
 * @version 2.0     Documentation added.
 */
package com.project.dev;

/**
 * TODO: Definición de {@code Log}.
 *
 * @author Dyson Parra
 * @since 11
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

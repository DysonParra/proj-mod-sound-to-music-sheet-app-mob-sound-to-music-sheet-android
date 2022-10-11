/*
 * @fileoverview {MusicSymbol} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {MusicSymbol} fue realizada el 31/07/2022.
 * @Dev - La primera version de {MusicSymbol} fue escrita por Dyson A. Parra T.
 */
package com.project.dev.soundtomusicsheet.struct.symbol;

import java.io.Serializable;
import lombok.Data;

/**
 * TODO: Definición de {@code MusicSymbol}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public abstract class MusicSymbol implements Serializable {

    private int number;                                     // Número del nodo en la lista ligada.
    private MusicSymbol next;                               // siguiente nodo.
    private MusicSymbol prev;                               // anterior nodo.

    /**
     * TODO: Definición de {@code addToXmlFile}.
     *
     * @param source
     * @param divisions
     */
    public abstract void addToXmlFile(String source, int divisions);

    /**
     * TODO: Definición de {@code printMusicSymbol}.
     *
     */
    public abstract void printMusicSymbol();
}

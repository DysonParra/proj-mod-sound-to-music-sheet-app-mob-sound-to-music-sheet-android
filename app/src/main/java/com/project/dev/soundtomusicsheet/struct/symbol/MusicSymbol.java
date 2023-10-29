/*
 * @fileoverview    {MusicSymbol}
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
package com.project.dev.soundtomusicsheet.struct.symbol;

import java.io.Serializable;
import lombok.Data;

/**
 * TODO: Description of {@code MusicSymbol}.
 *
 * @author Dyson Parra
 * @since 11
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
     * TODO: Description of {@code addToXmlFile}.
     *
     * @param source
     * @param divisions
     */
    public abstract void addToXmlFile(String source, int divisions);

    /**
     * TODO: Description of {@code printMusicSymbol}.
     *
     */
    public abstract void printMusicSymbol();
}

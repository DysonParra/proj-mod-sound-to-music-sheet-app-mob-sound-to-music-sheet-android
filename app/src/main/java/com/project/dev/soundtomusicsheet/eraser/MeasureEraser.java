/*
 * @fileoverview    {MeasureEraser}
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
package com.project.dev.soundtomusicsheet.eraser;

import com.project.dev.soundtomusicsheet.struct.MusicSheet;
import com.project.dev.soundtomusicsheet.struct.symbol.Measure;
import com.project.dev.soundtomusicsheet.struct.symbol.MusicSymbol;

/**
 * TODO: Definición de {@code MeasureEraser}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class MeasureEraser {

    /**
     * FIXME: Definición de {@code deleteSeparationMeasures}. Busca los compases de separación en un
     * objeto de tipo partitura y los borra.
     *
     * @param sheet El objeto de tipo partitura al que se le borrarán los compases de separación.
     */
    public void deleteSeparationMeasures(MusicSheet sheet) {
        MusicSymbol aux = sheet.getFirst();                                     // Crea objeto musicSymbol apuntando al primer nodo de la partitura.
        Measure compas;                                                         // Crea apuntador a cada nodo tipo compás.

        do {                                                                    // Recorre la partitura.
            if ("Measure".equals(aux.getClass().getSimpleName())) {              // Si encuentra un compás.
                compas = (Measure) aux;                                         // Almacena en compás el nodo actual.

                if (!compas.isEditMeasureInfo()) {                              // Si el compás no edita la información de la partitura (es de separación).
                    //System.out.printf("Del #%4d  Measure\n", aux.getNumber());
                    aux = aux.getPrev();                                        // Pasa al anterior nodo.
                    sheet.deleteSymbol(compas);                                 // Borra compás.
                }
            }

            aux = aux.getNext();                                                // Pasa al siguiente nodo.
        } while (aux != sheet.getFirst());                                      // Mientras no llegue al primer nodo.
    }
}

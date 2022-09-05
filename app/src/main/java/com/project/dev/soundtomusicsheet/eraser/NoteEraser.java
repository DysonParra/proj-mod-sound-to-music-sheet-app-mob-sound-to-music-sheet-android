/*
 * @fileoverview {FileName} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {FileName} fue realizada el 31/07/2022.
 * @Dev - La primera version de {FileName} fue escrita por Dyson A. Parra T.
 */
package com.project.dev.soundtomusicsheet.eraser;

import com.project.dev.soundtomusicsheet.struct.MusicSheet;
import com.project.dev.soundtomusicsheet.struct.symbol.MusicSymbol;
import com.project.dev.soundtomusicsheet.struct.symbol.Note;

/**
 * TODO: Definición de {@code NoteEraser}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class NoteEraser {

    /**
     * FIXME: Definición de {@code deleteRestsAtStart}. Busca en un objeto de tipo partitura si las
     * primeras notas son silencios y las borra.
     *
     * @param sheet El objeto de tipo partitura al que se le borrarán los silencios al inicio.
     */
    public void deleteRestsAtStart(MusicSheet sheet) {
        MusicSymbol aux = sheet.getFirst();                                     // Crea objeto musicSymbol apuntando al primer nodo de la partitura.
        Note nota;                                                              // Crea apuntador cada nodo tipo nota.

        while (!"Note".equals(aux.getClass().getSimpleName()))                  // Mientras no encuentre un nodo tipo nota.
            aux = aux.getNext();                                                // Avance al siguiente nodo.

        while (aux != sheet.getFirst()) {                                        // Mientras no reccora toda la partitura.
            if ("Note".equals(aux.getClass().getSimpleName())) {                // Si el nodo actual es una nota.
                nota = (Note) aux;                                              // Almacena en aux la nota.

                if ("0".equals(nota.getStep())) {                                // Si aux es un descanso.
                    //System.out.printf("Del #%4d  type is Rest %4dbt \n", aux.getNumber(), nota.getBeats());
                    aux = aux.getPrev();                                        // Actualiza aux como el anterior nodo.
                    sheet.deleteSymbol(aux.getNext());                          // Borra el nodo aux.
                } else {                                                          // Si no es un descanso.
                    //System.out.printf("End #%4d  type is %s %4dbt\n", aux.getNumber(), nota.getClass().getSimpleName(), nota.getBeats());
                    break;                                                      // Sale del ciclo.
                }
            } else {                                                              // Si el nodo actual no es una nota.
                //System.out.printf("Del #%4d  type is %s\n", aux.getNumber(), aux.getClass().getSimpleName());
                aux = aux.getPrev();                                            // Actualiza aux como el anterior nodo.
                sheet.deleteSymbol(aux.getNext());                              // Borra el nodo siguiente de aux.
            }

            aux = aux.getNext();                                                // Pasa al siguiente nodo.
        }
    }

    /**
     * FIXME: Definición de {@code deleteRestsAtEnd}. Busca en un objeto de tipo partitura si las
     * últimas notas son silencios y las borra.
     *
     * @param sheet El objeto de tipo partitura al que se le borrarán los silencios al final.
     */
    public void deleteRestsAtEnd(MusicSheet sheet) {
        MusicSymbol aux = sheet.getLast();                                      // Crea objeto musicSymbol apuntando al último nodo de la partitura.
        Note nota;                                                              // Crea apuntador cada nodo tipo nota.

        while (!"Note".equals(aux.getClass().getSimpleName()))                  // Mientras no encuentre un nodo tipo nota.
            aux = aux.getPrev();                                                // Avance al anterior nodo.

        while (aux != sheet.getFirst()) {                                        // Mientras no reccora toda la partitura.
            if ("Note".equals(aux.getClass().getSimpleName())) {                // Si el nodo actual es una nota.
                nota = (Note) aux;                                              // Almacena en aux la nota.

                if ("0".equals(nota.getStep())) {                                // Si aux es un descanso.
                    //System.out.printf("Del #%4d  type is Rest %4dbt \n", aux.getNumber(), nota.getBeats());
                    aux = aux.getNext();                                        // Actualiza aux como el siguiente nodo.
                    sheet.deleteSymbol(aux.getPrev());                          // Borra el anterior nodo de aux.
                } else {                                                          // Si no es un descanso.
                    //System.out.printf("End #%4d  type is %s %4dbt\n", aux.getNumber(), nota.getClass().getSimpleName(), nota.getBeats());
                    break;                                                      // Sale del ciclo.
                }
            } else {                                                              // Si el nodo actual no es una nota.
                //System.out.printf("Del #%4d  type is %s\n", aux.getNumber(), aux.getClass().getSimpleName());
                aux = aux.getNext();                                            // Actualiza aux como el anterior nodo.
                sheet.deleteSymbol(aux.getPrev());                              // Borra el nodo anterior de aux.
            }

            aux = aux.getPrev();                                                // Pasa al anterior nodo.
        }
    }
}

/*
 * @fileoverview    {TempoCreator}
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
package com.project.dev.soundtomusicsheet.creator;

import com.project.dev.soundtomusicsheet.struct.symbol.Tempo;

/**
 * TODO: Definición de {@code TempoCreator}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class TempoCreator {

    /**
     * FIXME: Definición de {@code createTempoFromActivity}. Crea un objeto tipo tempo basado en los
     * campos del activity de editar información o el de nueva partitura.
     *
     * @param spnTempoId Es el indice del Spinner con la figura del tempo.
     * @param tempoValue Es la cantidad de pulsos que tendrá el tempo.
     * @return El tempo creado en base a los datos obtenidos.
     */
    public Tempo createTempoFromActivity(long spnTempoId, short tempoValue) {
        String type = "";                                            // Indicará el tipo de tempo (negra, blanca o corchea).

        switch ((int) spnTempoId) {                                  // Evalúa cual fue el tipo de nota seleccionada.
            case 0:                                                 // Blanca.
                type = "half";                                      // Almacena el tipo de nota en type.
                break;                                              // Sale del ciclo.
            case 1:                                                 // Negra.
                type = "quarter";                                   // Almacena el tipo de nota en type.
                break;                                              // Sale del ciclo.
            case 2:                                                 // Corchea.
                type = "eighth";                                    // Almacena el tipo de nota en type.
                break;                                              // Sale del ciclo.
        }

        return new Tempo(type, tempoValue);                          // Devuelve un nuevo tempo con la información obtenida.
    }
}

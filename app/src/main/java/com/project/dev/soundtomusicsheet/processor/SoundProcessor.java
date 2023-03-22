/*
 * @fileoverview    {SoundProcessor}
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
package com.project.dev.soundtomusicsheet.processor;

import com.project.dev.soundtomusicsheet.struct.MusicSheet;
import com.project.dev.soundtomusicsheet.struct.symbol.MusicSymbol;
import com.project.dev.soundtomusicsheet.struct.symbol.Note;
import com.project.dev.soundtomusicsheet.struct.symbol.Sound;
import com.project.dev.soundtomusicsheet.struct.symbol.Tempo;

/**
 * TODO: Definición de {@code SoundProcessor}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class SoundProcessor {

    /**
     * FIXME: Definición de {@code makeFrequencyMatrix}. Calcula las frecuencias de todas las notas
     * usado como base la afinación de A4.
     *
     * @param A4 Es el valor de la frecuencia de A4.
     * @return Una matriz con las 88 frecuencias.
     */
    private double[] makeFrequencyMatrix(double A4) {
        double[] frecuencias = new double[88];                                  // Crea un Array de doubles en el cual se almacenarán las frecuencias de cada una de las notas musicales (en Hertz).
        double form = Math.pow(2, 1.0 / 12.0);                                  // Crea un double "form" que tendrá el valor por el cual se multiplica para hallar cada una de las frecuencias (2∧(1/12)) ó (1.059463)
        double aux = A4 / 16.0;                                                 // Se divide entre 16 el valor de A4 para hallar A0 (entre 8 para A1, entre 4 para A2, entre 2 para A3, multiplica por 2 para A5, etc...).

        // Llena la matriz de frecuencias.
        for (byte i = 0; i < 88; i++) {
            frecuencias[i] = aux;                                               // A la matriz en la posición i le lleva el valor de aux.
            aux *= form;                                                   // Para hallar la frecuencia de la siguiente nota, se multiplica la frecuencia actual por "form" (2∧(1/12)).
        }
        return frecuencias;                                                     // Devuelve la matriz con las frecuencias.
    }

    /**
     * FIXME: Definición de {@code makeIntervalMatrix}. Crea una matriz con los intervalos de cada
     * nota basados en la matriz de frecuencias. Dado que las frecuencias de las notas son números
     * exactos como G4# = 415,305Hz, A4 = 440Hz, A4# = 466,164Hz, si tenemos por ejemplo una
     * frecuencia de 438Hz, y la comparamos con los valores de la matriz de frecuencias, nos dará
     * como resultado que no existe tal nota, lo que se propones es: A4 no es 440Hz, sino desde
     * (G4#+A4)/2 hasta (A4+A4#)/2 es decir desde 377,6525 hasta 453,082. (Desde el punto medio
     * entre la anterior nota y dicha nota, hasta el punto medio entre dicha nota y la siguiente).
     * Así la frecuencia que antes "no existía" ya es A4, y se hace lo mismo con cada una de las
     * notas; sumando con la nota anterior y la siguiente, dividiendo entre 2, y obteniendo así en
     * lugar de un número un intervalo para ser dicha nota.
     *
     * @param A4 Es el valor de la frecuencia de A4.
     * @return Una matriz con los 88 intervalos de frecuencias de cada nota.
     */
    private double[][] makeIntervalMatrix(double A4) {
        // Matriz que tendrá los intervalos.
        double[][] interval = new double[88][2];

        // Crea una matriz ferquency con las frecuencias exactas de cada nota.
        double frequency[] = makeFrequencyMatrix(A4);

        // Se llena la matriz de intervalos basado en la matriz de frecuencias.
        for (byte i = 0; i < 88; i++) {
            // Aquí se define el componene "desde" del intervalo de cada nota.
            /*
             * Si está en la primer nota (nota 1, A0), se procede a hallar la que sería la anterior
             * nota, se suma A0 con esta nueva frecuencia y se divide entre 2. Caso contrario solo
             * es sumar la nota actual con la anterior y dividir entre 2.
             */
            if (i == 0)
                interval[i][0] = ((frequency[i] / 1.059463) + frequency[i]) / 2;
            else
                interval[i][0] = interval[i - 1][1];

            // Aquí se define el componene "hasta" del intervalo de cada nota.
            /*
             * Si está en la última nota (nota 88, C8), se procede a hallar la que sería la
             * siguiente nota, se suma C8 con esta nueva frecuencia y se divide entre 2. Caso
             * contrario solo es sumar la nota actual con la siguiente y dividir entre 2.
             */
            if (i == 87)
                interval[i][1] = (frequency[i] + (frequency[i] * 1.059463)) / 2;
            else
                interval[i][1] = (frequency[i] + frequency[i + 1]) / 2;
        }

        // Devuelve la matriz con los intervalos de cada nota.
        return interval;
    }

    /**
     * FIXME: Definición de {@code calculateNote}. Obtiene una nota basado en un objeto de tipo
     * sonido, una matriz de intervalos, un tempo (en negra), el volumén mínimo y las divisiones de
     * la partitura.
     *
     * @param sound     El objeto al que se le hallará la nota equivalente.
     * @param intervals Es la matriz con los intervalos de cada nota.
     * @param tempo     Es el tempo basado en el cual se obtendrá la duración de la nota (en
     *                  pulsos).
     * @param minVolume Es el volumén mínimo (intensidad) para que la nota no sea detectada como
     *                  silencio.
     * @param divisions Son las divisiones de la partitura.
     * @return La nota correspondiente al objeto de tipo sonido.
     */
    private Note calculateNote(Sound sound, double[][] intervals, float tempo, float minVolume, int divisions) {
        Note nota = new Note();

        // Se crea un entero que tendrá el número de la nota en la matriz de intervalos.
        int noteNumber = -1;

        // Busca la nota en la matriz de intervalos y asigna indice donde la encontró a noteNumber.
        for (byte i = 0; i < 88; i++)
            if (sound.getFrequency() >= intervals[i][0] && sound.getFrequency() < intervals[i][1]) {
                noteNumber = i;
                break;
            }

        // Si la intensidad de la nota es menor a la mínima requerida se pone como silencio.
        if (sound.getIntensity() < minVolume)
            noteNumber = -1;

        // Si la nota corresponde a alguna frecuencia del intervalo entra aquí, si no, quiere decir que es un decanso.
        if (noteNumber != -1) {
            // Crea un entero "octava" y le lleva la octava en la cual está la nota con una fórmula de direccionamiento.
            int octava = (noteNumber + 9) / 12;

            // Almacena la octava en sound.
            nota.setOctave(String.valueOf(octava));

            // Crea un entero "note" que tendrá un número entre 0 y 12 para definir que nota es.
            int note = noteNumber % 12;

            // Evalúa el valor de "note" y dependiendo de este excribe la nota en el objeto sound.
            switch (note) {
                case (3):
                    nota.setStep("C");
                    nota.setAlter("0");
                    break;
                case (4):
                    nota.setStep("C");
                    nota.setAlter("1");
                    break;
                case (5):
                    nota.setStep("D");
                    nota.setAlter("0");
                    break;
                case (6):
                    nota.setStep("C");
                    nota.setAlter("1");
                    break;
                case (7):
                    nota.setStep("E");
                    nota.setAlter("0");
                    break;
                case (8):
                    nota.setStep("F");
                    nota.setAlter("0");
                    break;
                case (9):
                    nota.setStep("F");
                    nota.setAlter("1");
                    break;
                case (10):
                    nota.setStep("G");
                    nota.setAlter("0");
                    break;
                case (11):
                    nota.setStep("G");
                    nota.setAlter("1");
                    break;
                case (0):
                    nota.setStep("A");
                    nota.setAlter("0");
                    break;
                case (1):
                    nota.setStep("A");
                    nota.setAlter("1");
                    break;
                case (2):
                    nota.setStep("B");
                    nota.setAlter("0");
                    break;
            }
        } // Si no está en la matriz de frecuencias (es un silencio).
        else
            nota.setStep("0");

        // Calcula la cantidad de pulsos a los que corresponde la duración de la nota.
        // (tempo/60.0) = cantidad de negras que caben en un segundo.
        // (tempo/60.0)*divisions = cantidad de pulsos con divisios = 32 que caben en un segundo.
        double beats = (tempo / 60.0) * divisions * sound.getDuration();
        nota.setBeats((int) beats);

        // Devuelve la nota asociada al objeto de tipo sonido.
        return nota;
    }

    /**
     * FIXME: Definición de {@code convertSoundsToNotes}. Convierte los sonidos de un objeto de tipo
     * partitura en su nota equivalente.
     *
     * @param sheet El objeto de tipo partitura al que se le convertirán sus nodos tipo sonido.
     */
    public void convertSoundsToNotes(MusicSheet sheet) {
        double[][] intervalos = makeIntervalMatrix(sheet.getA4Affination());    // Crea la matriz de intervalos basado en el valor de A4 de la partitura.

        MusicSymbol aux = sheet.getFirst();                                     // Crea un objeto musicSymbol apuntando al primer nodo de la lista.
        float tempoValue = 0;                                                   // Almacena los cambios en el tempo encontrados.
        Tempo tempo;                                                            // Indica tempo encontrado.
        Sound sound;                                                            // Indica cada sonido encontrado.
        Note nota;                                                              // Indica cada nota calculada.

        do {                                                                    // Mientras no haya recorrido toda la lista.
            if ("Tempo".equals(aux.getClass().getSimpleName())) {               // Si se especifíca cambio de tempo.
                tempo = (Tempo) aux;                                            // "tempo" ahora es una copia de "aux".

                switch (tempo.getType()) {                                      // Evalúa el tipo de nota base para calcular el tempo.
                    case "half":                                                // Blanca.
                        tempoValue = tempo.getValue() * 2;                      // Asigna a tempoValue el valor del tempo (pasado a duración en negra).
                        break;                                                  // Sale del ciclo.
                    case "quarter":                                             // Negra.
                        tempoValue = tempo.getValue();                          // Asigna a tempoValue el valor del tempo.
                        break;                                                  // Sale del ciclo.
                    case "eighth":                                              // Corchea.
                        tempoValue = tempo.getValue() / 2.0f;                   // Asigna a tempoValue el valor del tempo (pasado a duración en negra).
                        break;                                                  // Sale del ciclo.
                }
            } else if ("Sound".equals(aux.getClass().getSimpleName())) {        // Si se encontró un sonido.
                sound = (Sound) aux;                                            // "sound" es ahora una copia de "aux".
                nota = calculateNote(sound, intervalos, tempoValue,
                        sheet.getMinVolume(), sheet.getDivisions());            // Asigna a "nota" la nota asociada a sound.

                sheet.addSymbol(nota, aux, (true));                             // Agrega nota después de sound.
                sheet.deleteSymbol(aux);                                        // Borra sound de la llista.
                aux = nota;                                                     // "aux" ahora apunta a nota.
            }
            aux = aux.getNext();                                                // Avanza al siguiente nodo.
        } while (aux != sheet.getFirst());
    }
}

/*
 * @fileoverview    {MeasureCreator}
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
package com.project.dev.soundtomusicsheet.creator;

import com.project.dev.soundtomusicsheet.struct.MusicSheet;
import com.project.dev.soundtomusicsheet.struct.symbol.Measure;
import com.project.dev.soundtomusicsheet.struct.symbol.MusicSymbol;
import com.project.dev.soundtomusicsheet.struct.symbol.Note;

/**
 * TODO: Description of {@code MeasureCreator}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class MeasureCreator {

    /**
     * FIXME: Description of {@code createMeasureFromActivity}. Crea un objeto tipo compás basado en
     * los campos del activity de editar información o el de nueva partitura.
     *
     * @param strNoteQuantity Es el String con la cantidad de notas que cabrán en el compas.
     * @param strNoteType     Es el String con el tipo de notas que cabrán en cada compás.
     * @param spnKeyId        Es la posición del Spinner con la clave de la partitura.
     * @param spnKeyLineId    Es el la posición del Spinner con la línea de clave de la partitura.
     * @param strTranspose    Es el String con el valor de transporte de la clave por octavas.
     * @param spnSignatureId  Es la posición del Spinner con la armadura de la partitura.
     * @return El compás creado en base a los datos obtenidos.
     */
    public Measure createMeasureFromActivity(String strNoteQuantity, String strNoteType, long spnKeyId, long spnKeyLineId, String strTranspose, long spnSignatureId) {
        byte quantity;                              // Indica la cantidad de notas que cabrá en cada compás de la partitura.
        byte noteType;                              // Indica el tipo de notas que cabrá en cada compás.
        String key = "";                            // Indica la clave de la partitura.
        byte keyLine;                               // Indica la linea de clave de la partitura.
        byte clefOctave;                            // Indica cuantas octavas se transportará la clave de la partitura.
        byte keySignature;                          // Indica la nueva armadura de clave de la partitura.

        // Almacena el indicador de compás en quantity y noteType.
        quantity = Byte.valueOf(strNoteQuantity);
        noteType = Byte.valueOf(strNoteType);

        // Almacena la clave del compás en key.
        switch ((int) spnKeyId) {
            case 0:
                key = "G";
                break;
            case 1:
                key = "F";
                break;
            case 2:
                key = "C";
                break;
        }

        // Almacena la línea de clave en keyline.
        keyLine = (byte) (spnKeyLineId + 1);

        // Almacena el valor del transporte por octavas de la clave.
        clefOctave = Byte.valueOf(strTranspose);

        // Si el indice de la armadura es 0, no hay armadura, si es entre 1 y 7 es de bemoles, y si es entre 8 y 14 es de sostenidos.
        if (spnSignatureId == 0)
            keySignature = 0;
        else if (spnSignatureId < 8)
            keySignature = (byte) (spnSignatureId - (spnSignatureId * 2));
        else
            keySignature = (byte) (spnSignatureId - 7);

        // Devuelve el nodo de tipo compás con la información seleccionada por el usuario.
        return new Measure(1, quantity, noteType, key, keyLine, clefOctave, keySignature);
    }

    /**
     * FIXME: Description of {@code addSeparationMeasures}. Dada una lista con notas subdivididas,
     * agrega nodos de tipo compás cada que comience uno.
     *
     * @param sheet El objeto de tipo partitura al que se le agregarán los compases de separación.
     */
    public void addSeparationMeasures(MusicSheet sheet) {
        MusicSymbol aux = sheet.getFirst();                                     // Crea un objeto musicSymbol apuntando al primer nodo de la partitura.
        int beats = 0;                                                          // Cantidad de pulsos que caben en cada compás.
        int remainingBeats = 0;                                                 // Cantidad de pulsos restantes para terminar el compás actual.
        int measureNumber = 1;                                                  // Número de compás de la partitura.
        Measure measure;                                                        // Referencia a cada nodo tipo compás de la partitura.
        Note nota;                                                              // Referencia a cada nodo tipo nota de la partitura.

        do {                                                                    // Mientras no haya recorrido toda la lista.
            if ("Measure".equals(aux.getClass().getSimpleName())) {             // Si encuentra un compás.
                measure = (Measure) aux;                                        // Almacena en measure el compás actual.
                measure.setMeasureNumber(measureNumber);                        // Al compás actual le lleva el número de compás en la partitura.
                measureNumber++;                                                // Aumenta el número de compás en la partitura.

                if (measure.getNoteType() != 0)                                 // Si no es un compás de separación.
                    beats = measure.calculateBeats(sheet.getDivisions());       // A beats le lleva cuantos pulsos caben en acada compás a partir de ahora.

                remainingBeats = beats;                                         // Reinicia la cantidad de pulsos faltantes para acabar el compás.
            } else if ("Note".equals(aux.getClass().getSimpleName())) {         // Si se encontró una nota.
                nota = (Note) aux;                                              // Almacena en nota la nota actual.
                remainingBeats -= nota.getBeats();                              // Resta la duración de la nota a la cantidad de pulsos restantes para terminar el compás actual.

                // Si al restar la duración se lleno el compás actual, y el siguiente nodo no es tipo compás.
                if (remainingBeats == 0 && !"Measure".equals(aux.getNext().getClass().getSimpleName())) {
                    remainingBeats = beats;                                     // Reinicia la cantidad de pulsos faltantes para terminar el compás.
                    sheet.addSymbol(new Measure(measureNumber), aux, (true));   // Crea un nuevo nodo tipo compás.
                }
            }
            aux = aux.getNext();                                                // Avanza al siguiente nodo.
        } while (aux != sheet.getFirst());                                      // Mientras no recorra toda la lista.
    }
}

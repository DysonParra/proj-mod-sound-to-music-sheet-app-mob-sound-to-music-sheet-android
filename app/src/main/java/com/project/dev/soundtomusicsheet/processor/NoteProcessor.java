/*
 * @fileoverview    {NoteProcessor}
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
import com.project.dev.soundtomusicsheet.struct.symbol.Measure;
import com.project.dev.soundtomusicsheet.struct.symbol.MusicSymbol;
import com.project.dev.soundtomusicsheet.struct.symbol.Note;

/**
 * TODO: Definición de {@code NoteProcessor}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class NoteProcessor {

    /**
     * FIXME: Definición de {@code convertDivisionsToThirtyTwo}. Convierte un objeto de partitura
     * con un valor de divisions diferente a 32 a uno con 32.
     *
     * @param sheet El objeto de tipo partitura al que se le cambiarán las divisiones a 32.
     */
    public void convertDivisionsToThirtyTwo(MusicSheet sheet) {
        if (sheet.getDivisions() != 32) {                                               // Si el número de divisiones no es igual a 32.
            MusicSymbol aux = sheet.getFirst();                                         // Crea apuntador al primer nodo de la partitura.
            Note nota;                                                                  // Crea apuntador a cada nodo tipo nota.
            double beatsAux;                                                            // Indica los pulsos de cada nota con divisions = 1.

            do {                                                                        // Recorre la partitura.
                if ("Note".equals(aux.getClass().getSimpleName())) {                    // Si el nodo actual es un nodo tipo nota.
                    nota = (Note) aux;                                                  // Almacena en nota el nodo actual.
                    beatsAux = nota.getBeats() / (double) sheet.getDivisions();         // Almacena en beatsAux la duración de la nota con divisions = 1.
                    nota.setBeats((int) (beatsAux * 32.0));                             // Actualiza los pulsos e la nota actual con su equivalente a divisions = 32.
                }
                aux = aux.getNext();                                                    // Pasa al siguiente nodo.
            } while (aux != sheet.getFirst());                                          // Mientras no llegeue al prmer nodo.

            sheet.setDivisions(32);                                                     // Actualiza las divisiones de la partitura.
        }
    }

    /**
     * FIXME: Definición de {@code uniteTies}. Une las notas con ligadora en una misma nota.
     *
     * @param sheet El objeto de tipo partitura al que se le borrarán las liguduras.
     */
    public void uniteTies(MusicSheet sheet) {

        MusicSymbol aux = sheet.getFirst();                                         // Crea objeto musicSymbol apuntando al primer nodo de la partitura.
        Note nota1, nota2;                                                          // Crea apuntadores a un nodo tipo nota y al siguiente.

        do {                                                                        // Recorre la partitura.
            if ("Note".equals(aux.getClass().getSimpleName())) {                     // Si encuentra un nodo tipo nota.
                nota1 = (Note) aux;                                                 // A nota le lleva el nodo actual.

                if (nota1.getDot() != 0) {                                            // Si encuentra una nota con puntillo.
                    nota1.setDot(0);                                                // Borra el puntillo (la duración de la nota incluido puntillo se pone en beats).
                    System.out.printf("Dot Pos is %d\n", aux.getNumber());
                }

                while (nota1.getTie() == 1) {                                          // Si encuentra una nota con ligadura.
                    if ("Note".equals(aux.getNext().getClass().getSimpleName())) {   // Si el siguiente nodo es una nota.
                        System.out.printf("Tie Pos is %d\n", aux.getNumber());
                        nota2 = (Note) aux.getNext();                               // A nota2 le lleva el siguiente nodo.

                        if (nota2.getTie() == 3)                                     // Si en la siguiente nota termina la ligadura.
                            nota1.setTie(0);                                        // Elimina la ligadura en la nota actual.

                        nota1.setBeats(nota1.getBeats() + nota2.getBeats());        // A la nota actual le lleva la suma de sus pulsos y la siguiente.
                        sheet.deleteSymbol(nota2);                                  // Borra la siguiente nota.
                    } else {                                                          // Si el siguente nodo no es una nota (notas separada entre compases o tempo).
                        sheet.swapSymbols(aux, aux.getNext());                      // Intercambia las posiciones de la siguiente nota.
                    }
                }
            }

            aux = aux.getNext();                                                    // Pasa al siguiente nodo.
        } while (aux != sheet.getFirst());                                          // Mientras no llegue al primer nodo de la partitura.
    }

    /**
     * FIXME: Definición de {@code iqualNotes}. Verifica si dos notas tienen la misma altura.
     *
     * @param nota1 La primera nota.
     * @param nota2 La segunda nota.
     * @return Si las notas tienen la misma altura.
     */
    private boolean iqualNotes(Note nota1, Note nota2) {
        return (nota1.getStep().endsWith(nota2.getStep())
                && nota1.getAlter().endsWith(nota2.getAlter())
                && nota1.getOctave().endsWith(nota2.getOctave()));
    }

    /**
     * FIXME: Definición de {@code uniteNotes}. Une las notas iguales y adyacentes en una misma
     * nota.
     *
     * @param sheet El objeto de tipo partitura al que se unirán las notas iguales.
     */
    public void uniteNotes(MusicSheet sheet) {

        MusicSymbol aux = sheet.getFirst();                                         // Crea objeto musicSymbol apuntando al primer nodo de la partitura.
        Note nota1, nota2;                                                          // Crea apuntadores a un nodo tipo nota y al siguiente.

        do {                                                                        // Recorre la partitura.
            if ("Note".equals(aux.getClass().getSimpleName())) {                     // Si encuentra un nodo tipo nota.
                nota1 = (Note) aux;                                                 // A nota le lleva el nodo actual.

                while (true) {                                                       // Ciclo infinito.
                    if ("Note".equals(aux.getNext().getClass().getSimpleName())) {   // Si el siguiente nodo es una nota.
                        nota2 = (Note) aux.getNext();                               // A nota2 le lleva el siguiente nodo.

                        if (iqualNotes(nota1, nota2)) {                              // Si las notas tienen la misma altura.
                            //System.out.printf("Iqual Pos is %d\n", aux.getNumber());
                            nota1.setBeats(nota1.getBeats() + nota2.getBeats());    // A la nota actual le lleva la suma de sus pulsos y la siguiente.
                            sheet.deleteSymbol(nota2);                              // Borra la siguiente nota.
                        } else                                                        // Si no son la misma nota.
                            break;                                                  // Sale del ciclo.
                    } else                                                            // Si no tienen la misma altura.
                        break;                                                      // Sale del ciclo.
                }
            }

            aux = aux.getNext();                                                    // Pasa al siguiente nodo.
        } while (aux != sheet.getFirst());                                          // Mientras no llegue al primer nodo de la partitura.
    }

    /**
     * FIXME: Definición de {@code obtainNoteDivisions}. Divide una cantidad de pulsos dada, en
     * n-notas conocidas. Si la cantidad de pulsos es exactamente igual a una nota existente
     * (blanca, negra, etc...), se almacena un solo elemento en el vector.
     *
     * @param beats       Es la cantidad de pulsos recibida.
     * @param dividedNote Es el vector donde se guardarán las divisiones de la nota parámetro.
     * @param maxBeats    Indica la duración máxima de la nota que cabe en este compás.
     */
    private void obtainNoteDivisions(int beats, int[] dividedNote, int maxBeats) {
        int i = 0;
        // Mientras la cantidad de pulsos no sea 0.
        while (beats != 0) {
            /*
             * Evalúa si la duración es mayor o igual que la de una redonda, de ser así resta una
             * redonda a la cantidad total de pulsos.
             */
            if (beats >= 128 && 128 <= maxBeats) {
                dividedNote[i] = 128;
                beats -= 128;
                i++;
            } /*
             * Caso contrario evalúa si la duración es mayor o igual que la de una blanca, de ser
             * así resta una blanca a la cantidad total de pulsos.
             */ else if (beats >= 64 && 64 <= maxBeats) {
                dividedNote[i] = 64;
                beats -= 64;
                i++;
            } /*
             * Caso contrario evalúa si la duración es mayor o igual que la de una negra, de ser así
             * resta una negra a la cantidad total de pulsos.
             */ else if (beats >= 32 && 32 <= maxBeats) {
                dividedNote[i] = 32;
                beats -= 32;
                i++;
            } /*
             * Caso contrario evalúa si la duración es mayor o igual que la de una corchea, de ser
             * así resta una corchea a la cantidad total de pulsos.
             */ else if (beats >= 16 && 16 <= maxBeats) {
                dividedNote[i] = 16;
                beats -= 16;
                i++;
            } /*
             * Caso contrario evalúa si la duración es mayor o igual que la de una semicorchea, de
             * ser así resta una semicorchea a la cantidad total de pulsos.
             */ else if (beats >= 8 && 8 <= maxBeats) {
                dividedNote[i] = 8;
                beats -= 8;
                i++;
            } /*
             * Caso contrario evalúa si la duración es mayor o igual que la de una fusa, de ser así
             * resta una fusa a la cantidad total de pulsos.
             */ else if (beats >= 4 && 4 <= maxBeats) {
                dividedNote[i] = 4;
                beats -= 4;
                i++;
            } /*
             * Caso contrario evalúa si la duración es mayor o igual que la de una semifusa, de ser
             * así resta una semifusa a la cantidad total de pulsos.
             */ else if (beats >= 2 && 2 <= maxBeats) {
                dividedNote[i] = 2;
                beats -= 2;
                i++;
            } /*
             * Caso contrario evalúa si la duración es mayor o igual que la de una garrapatea, de
             * ser así resta una garrapateadeja y deja en cero la cantidad total de pulsos.
             */ else if (beats >= 1) {
                dividedNote[i] = 1;
                beats = 0;
                i++;
            }
        }
    }

    /**
     * FIXME: Definición de {@code swap}. Intercambia las posiciones de un nodo y su nodo anterior
     * si es un nodo tipo compás, o el anterior y el anterior a ese si son un nodo tipo tempo y un
     * nodo tipo compás respectivamente.
     *
     * @param sheet La partitura a la que se le intercambiaran su nodos.
     * @param aux   El nodo al que se le evaluarán si se debe intercambiar con los anteriores.
     */
    private void swap(MusicSheet sheet, MusicSymbol aux) {
        /*
         * Evalúa si antes de esta nota había un indicador de cambio de información en compás, y si
         * ese cambio apareció en medio un compás, en cuyo caso los cambios se efectuarían al
         * comenzar el próximo compás.
         */
        if ("Measure".equals(aux.getPrev().getClass().getSimpleName())) {
            //System.out.printf("Swap 1 %2dnote\n", aux.getNumber());
            sheet.swapSymbols(aux, aux.getPrev());                      // Intercambia la subnota creada con el nodo de compás.
            aux = aux.getNext();                                        // Aux ahora apunta al nodo tipo compás.
        } /*
         * Evalúa si antes de esta nota había un indicador de cambio de tempo, y antes del cambio de
         * tempo había un nodo de cambio de información en compás, y si ese cambio apareció en medio
         * un compás, en cuyo caso los cambios se efectuarían al comenzar el próximo compás.
         */ else if ("Tempo".equals(aux.getPrev().getClass().getSimpleName()) && "Measure".equals(aux.getPrev().getPrev().getClass().getSimpleName()) && sheet.getFirst() != aux.getPrev().getPrev()) {
            //System.out.printf("Swap 2 %2dnote\n", aux.getNumber());
            sheet.swapSymbols(aux.getPrev(), aux.getPrev().getPrev());  // Intercambia el nodo compás y el nodo tempo.
            sheet.swapSymbols(aux, aux.getPrev());                      // Intercambia la nota con el nodo de compás.
            aux = aux.getNext();                                        // Aux ahora apunta al nodo tipo compás.
        }

        /*
         * Si luego de intercambiar la nota y un nodo tipo compás quedaron dos nodos tipo compás
         * adyacentes borra el compás que acaba de intercambiar, (el primero de los dos adyacentes).
         */
        if ("Measure".equals(aux.getClass().getSimpleName()) && "Measure".equals(aux.getNext().getClass().getSimpleName()) && aux != sheet.getLast()) {
            //System.out.printf("Del 1 %2dnote\n", aux.getNumber());
            aux = aux.getPrev();                                        // a aux le lleva el nodo anterior (la nota que intercambió con el compás).
            sheet.deleteSymbol(aux.getNext());                          // Borra el compás.
        }
    }

    /**
     * FIXME: Definición de {@code divideNotes}. Divide las notas de una lista en n-notas con
     * ligadura dependiendo de la duración de la nota y si cabe en el compás actual.
     *
     * @param sheet La partitura a la que se le dividirán las notas en n-notas con ligadura.
     */
    public void divideNotes(MusicSheet sheet) {
        MusicSymbol aux = sheet.getFirst();                                             // Crea un objeto musicSymbol apuntando al primer nodo de la partitura.
        int beats = 0;                                                                  // Indica cuantos pulsos caben en el compás actual.
        int remainingBeats = 0;                                                         // Indica cuantos pulsos restan para terminar el compás actual.

        Measure compas;                                                                 // Crea objeto de tipo compás.
        Note nota;                                                                      // Crea objeto de tipo nota.

        do {                                                                            // Mientras no haya recorrido toda la lista.
            switch (aux.getClass().getSimpleName()) {                                    // Evalúa el tipo de nodo.
                case ("Measure"): {                                                      // Si encontró un nodo compás.
                    compas = (Measure) aux;
                    //System.out.printf("0p %2dnote Reman %d\n", compas.getNumber(), remainingBeats);

                    if (compas.isEditMeasureInfo())                                     // Si este nodo cambia el indicador de compás.
                        if (remainingBeats == beats || aux.getNumber() == 1) {           // Si el compás actual es el inicio de un compás ó es el primer compás de la partitura.
                            beats = compas.calculateBeats(sheet.getDivisions());        // A beats le asigna la cantidad de pulsos que caben en el compás.
                            remainingBeats = beats;                                     // Actualiza el valor de remainingBeats con el del nuevo compás.
                            //System.out.printf("0.1p %2dnote Reman %d\n", compas.getNumber(), remainingBeats);
                        }
                    break;
                }

                case ("Note"): {                                                         // Si encontró un nodo nota.
                    nota = (Note) aux;                                                  // Almacena en nota el nodo actual.
                    int[] dividedNote = new int[20];                                    // Crea un vector llamado dividedNote.
                    obtainNoteDivisions(nota.getBeats(), dividedNote, beats);           // A dividedNote le asigna las subdivisiones de aux.

                    // Evalúa si la Nota no tiene una duración especial (Al dividirla dio una sola nota) y dicha nota cabe en el compás actual.
                    if (dividedNote[1] == 0 && remainingBeats - nota.getBeats() >= 0) {
                        //System.out.printf("1p %2dnote %dbt Reman %d\n", nota.getNumber(), nota.getBeats(), remainingBeats);

                        if (remainingBeats != beats)                                     // Si la nota actual no está al comienzo de un compás.
                            swap(sheet, aux);                                           // Evalúa si hay que hacer swap.

                        remainingBeats -= nota.getBeats();                              // Resta la duración de la nota a la cantidad de pulsos que restan para terminar el compás.

                        if (remainingBeats == 0) {                                       // Si al restar dicha nota terminó el compás actual.
                            remainingBeats = beats;                                     // Reinicia remaininBeats.
                            //System.out.printf("1 New measure, last note is %d\n", aux.getNumber());
                        }

                        //System.out.printf("1p %2dnote %dbt Reman %d\n", nota.getNumber(), nota.getBeats(), remainingBeats);
                        //sheet.printMusicSheet();
                    } // Evalúa si la Nota si tiene una duración especial (Al dividirla no dio una sola nota) y dicha nota cabe en el compás actual.
                    else if (remainingBeats - nota.getBeats() >= 0 && dividedNote[1] != 0) {
                        //System.out.printf("2p %2dnote %dbt Reman %d\n", nota.getNumber(), nota.getBeats(), remainingBeats);

                        // imprime la nota.
                        /*
                         * -
                         * for (int k=0; k<dividedNote.length;k++) if (dividedNote[k]!= 0)
                         * System.out.printf("2p %2dnote k is %d %dbt\n", aux.getNumber(), k,
                         * dividedNote[k]);
                         */
                        for (int k = 0;; k++) {                                             // Recorre la matriz de subnotas.
                            if (dividedNote[k] == 0)                                     // Si se acabaron las subdivisiones.
                                break;                                                  // Sale del ciclo.

                            else if (k == 0) {                                             // Si es la primera subdivisión.
                                if (remainingBeats != beats)                             // Si la nota actual no está al comienzo de un compás.
                                    swap(sheet, aux);                                   // Evalúa si hay que hacer swap.

                                remainingBeats -= nota.getBeats();                      // Resta la duración de la nota a la cantidad de pulsos que restan para terminar el compás.

                                nota.setBeats(dividedNote[k]);                          // Pone al nodo la cantidad de pulsos de la subnota.
                                nota.setTie(1);                                         // Indica que desde allí inician las notas ligads.

                            } else {                                                      // Si no es la primera subnota.
                                // Si está en la última nota, entonces la nota actual está ligada con la anterior pero no con la siguiente, por tanto tie = 3.
                                if (dividedNote[k + 1] == 0)
                                    sheet.addSymbol(new Note(nota.getStep(), nota.getAlter(), nota.getOctave(), dividedNote[k], 3, 0, false), aux, true);
                                // Si está en cualquier nota que no sea la última, entonces la nota actual está ligada con la anterior y la siguiente, por tanto tie = 2.
                                else
                                    sheet.addSymbol(new Note(nota.getStep(), nota.getAlter(), nota.getOctave(), dividedNote[k], 2, 0, false), aux, true);

                                aux = aux.getNext();                                    // aux es ahora el nodo añadido a la lista.
                            }
                        }

                        // Si al restar la duración total de la nota (con sus ligaduras) terminó el compás reinicia remaininBeats.
                        if (remainingBeats == 0) {
                            remainingBeats = beats;
                            //System.out.printf("2 New measure, last note is %d\n", aux.getNumber());
                        }

                        //nota = (Note) aux;                                              // A nota le lleva aux (Posición de la última sub-nota).
                        //System.out.printf("2p %2dnote %dbt Reman %d\n", nota.getNumber(), nota.getBeats(), remainingBeats);
                        //sheet.printMusicSheet();
                    } // Si la nota no cabe en el compás actual.
                    else {
                        //System.out.printf("3p %2dnote %dbt Reman %d\n", nota.getNumber(), nota.getBeats(), remainingBeats);

                        int p1 = remainingBeats;                                        // Indica los pulsos que restan para que acabe el compás.
                        int p2 = nota.getBeats() - remainingBeats;                      // Indica los pulsos que quedan luego de restar la nota actual con lo que le queda al compás.

                        //System.out.printf("Note is %d P1 is %d p2 is %d\n", aux.getNumber(), p1, p2);
                        dividedNote = new int[20];                                      // Reinicia el vector dividedNote.
                        obtainNoteDivisions(p1, dividedNote, beats);                    // A dividedNote le lleva la subdivisión de lo que le resta al compás actual para terminar (p1).

                        for (int k = 0;; k++) {                                         // Recorre la matriz de subnotas.
                            if (dividedNote[k] == 0)                                    // Si se acabaron las subdivisiones.
                                break;                                                  // Sale del ciclo.

                            else if (k == 0) {                                          // Si es la primera subdivisión.
                                if (remainingBeats != beats)                            // Si la nota actual no está al comienzo de un compás.
                                    swap(sheet, aux);                                   // Evalúa si hay que hacer swap.

                                remainingBeats -= p1;                                   // Resta la cantidad de pulsos que rfaltan para terminar el compás a remainingBeats.

                                nota.setBeats(dividedNote[k]);                          // Pone al nodo la cantidad de pulsos de la subnota.
                                nota.setTie(1);                                         // Indica que desde allí inician las notas ligads.
                            } else {                                                    // Si no es la primera subnota (Ligadura intermedia).
                                sheet.addSymbol(new Note(nota.getStep(), nota.getAlter(), nota.getOctave(), dividedNote[k], 2, 0, false), aux, true);
                                aux = aux.getNext();                                    // aux es ahora el nodo añadido a la lista.
                            }
                        }

                        // Si luego de agregar las sub-notas de p1 sigue un nodo tipo compás (se hizo swap).
                        if ("Measure".equals(aux.getNext().getClass().getSimpleName()) && sheet.getFirst() != nota.getNext()) {
                            aux = aux.getNext();                                        // aux apunta al nodo tipo compás para agregar notas luego de este.
                            compas = (Measure) aux;                                     // A compas le lleva aux.

                            if (compas.isEditMeasureInfo())                             // Si este nodo cambia el indicador de compás.
                                beats = compas.calculateBeats(sheet.getDivisions());    // A beats le asigna la cantidad de pulsos que caben en el compás.
                        }

                        remainingBeats = beats;                                         // Actualiza el valor de remainingBeats con el de beats.

                        dividedNote = new int[20];                                      // Reinicia el vector dividedNote.
                        obtainNoteDivisions(p2, dividedNote, beats);                    // A dividedNote le lleva la subdivisión de lo que le resta al compás actual para terminar (p1).

                        for (int k = 0;; k++) {                                             // Recorre la matriz de subnotas.
                            if (dividedNote[k] == 0)                                     // Si se acabaron las subdivisiones.
                                break;                                                  // Sale del ciclo.
                            else {                                                      // Si no se terminaros subdivisiones.
                                // Si está en la última nota, entonces la nota actual está ligada con la anterior pero no con la siguiente, por tanto tie = 3.
                                if (dividedNote[k + 1] == 0)
                                    sheet.addSymbol(new Note(nota.getStep(), nota.getAlter(), nota.getOctave(), dividedNote[k], 3, 0, false), aux, true);
                                // Si está en cualquier nota que no sea la última, entonces la nota actual está ligada con la anterior y la siguiente, por tanto tie = 2.
                                else
                                    sheet.addSymbol(new Note(nota.getStep(), nota.getAlter(), nota.getOctave(), dividedNote[k], 2, 0, false), aux, true);

                                aux = aux.getNext();                                    // aux es ahora el nodo añadido a la lista.
                                remainingBeats -= dividedNote[k];                       // AremainingBeats le resta la duración de la subdivisión actual.

                                if (remainingBeats == 0)                                 // Si terminó otro compás.
                                    remainingBeats = beats;                             // Reinicia remainingBeats.
                            }
                        }

                        //System.out.printf("3p %2dnote %dbt Reman %d\n", nota.getNumber(), nota.getBeats(), remainingBeats);
                        //sheet.printMusicSheet();
                    }
                    break;
                }
            }
            aux = aux.getNext();                                                        // Avanza al siguiente nodo.
        } while (aux != sheet.getFirst());

        // Si teminó la lista en medio de un compás agrega silencios hasta terminarlo.
        if (remainingBeats != 0 && remainingBeats != beats) {
            int[] dividedNote = new int[10];
            obtainNoteDivisions(remainingBeats, dividedNote, beats);

            // Recorre la matriz de subdivisiones de los silencios que se agregarán al final.
            for (int k = 0;; k++) {
                // Si terminó de recorrer la matriz de subdivisiones sale del ciclo.
                if (dividedNote[k] == 0)
                    break;
                // Caso contrario agrega el silencio al final de la lista.
                else {
                    // Si el último nodo no es un compás (compás de edición aplazado).
                    if (!"Measure".equals(sheet.getLast().getClass().getSimpleName()))
                        sheet.addSymbol(new Note("0", "0", "0", dividedNote[k], 0, 0, false), sheet.getLast(), true);
                    // Si si es un compás.
                    else
                        sheet.addSymbol(new Note("0", "0", "0", dividedNote[k], 0, 0, false), sheet.getLast(), false);
                }
            }
        }

        // Si al final de la partitura hay un indicador de cambio en información de compás crea un compás de silencios.
        if ("Measure".equals(sheet.getLast().getClass().getSimpleName())) {
            int[] dividedNote = new int[10];                                    // Crea matriz para almacenar subdivivsiones del compás.
            compas = (Measure) sheet.getLast();                                 // Almacena el último nodo en compas.
            beats = compas.calculateBeats(sheet.getDivisions());                // A beats le lleva la duración del último nodo.
            obtainNoteDivisions(beats, dividedNote, beats);                     // Obtiene las sub-notas con duración igual a un compás.

            // Recorre la matriz de subdivisiones de los silencios que se agregarán al final.
            for (int k = 0;; k++) {
                // Si terminó de recorrer la matriz de subdivisiones sale del ciclo.
                if (dividedNote[k] == 0)
                    break;
                // Caso contrario agrega el silencio al final de la lista.
                else
                    sheet.addSymbol(new Note("0", "0", "0", dividedNote[k], 0, 0, false), sheet.getLast(), true);
            }
        }
    }
}

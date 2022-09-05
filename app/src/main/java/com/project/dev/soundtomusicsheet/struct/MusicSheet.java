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
package com.project.dev.soundtomusicsheet.struct;

import com.project.dev.Log;
import com.project.dev.soundtomusicsheet.struct.symbol.MusicSymbol;
import java.io.Serializable;
import lombok.Data;

/**
 * TODO: Definición de {@code MusicSheet}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class MusicSheet implements Serializable {

    private String source;                                          // Almacena la ruta de la partitura.
    private String number;                                          // Indica el número del archivo de partitura.
    private float A4Affination;                                     // Indica la afinación de A4 para dicha partitura.
    private int divisions;                                          // Indica las divisions de la partitura.
    private float minVolume;                                        // Indica el mínimo volue necesario para que una nota no se considere silencio.
    private MusicSymbol first;                                      // Referencia que indica el inicio de la lista.
    private MusicSymbol last;                                       // Referencia que indica el final de la lista o el ultimo nodo.
    private int size;                                               // Cantidad de elementos de la lista.

    /*
     * En una partitura un tipo de nota (redonda, blanca, negra, etc...) tienen asociadas una
     * cantidad de pulsos así: redonda = 4, blanca = 2, negra = 1, corchea = 0.5, semicorchea =
     * 0.25, fusa = 0.125, semifusa = 0.0625 y garrapatea = 0.03125. (potencias de 2). A la hora de
     * escribir una nota en el archivo xml se pone entonces la duración (en pulsos) de cada nota,
     * pero las partitura xml solo admiten números enteros en las duraciones, por tanto, por ejemplo
     * una corchea no podría escribirse en la partitura. Para solucionar esto, las partituras xml
     * incluyen algo llamado "divisions" que redefine la duración de cada una de las notas. Por
     * ejemplo divisions = 1 define que las notas duran los pulsos por defecto, divisions = 2 define
     * que cada nota dura 2-veces la cantidad de pulsos que normalmente duraría. Así por ejemplo con
     * divisions = 8, redonda = 32, blanca = 16, negra = 8, corchea = 4, semicorchea = 2, Fusa = 1,
     * semifusa = 0.5, garrapatea = 0.25. (no podrían escribirse semifusas ni garrapateas). Para que
     * se pueda incluir cualquier nota se necesita divisioons = 32, entonces a la hora de crear las
     * partituras, para este programa se usará ese número, pero las partituras creadas en otros
     * programas calculan el valor de divisions intentando que en la partitura haya espacio para la
     * nota más pequeña. Por ejemplo si la nota más pequeña es una negra, se pondrá divisions = 1.
     * Es necesario entonces tener presente el valor de divisions para pasar la duración de todas la
     * notas a divisions = 32 cuando se cargue una partitura.
     */
    public MusicSheet(float A4Affination) {
        this.source = null;
        this.number = "001";
        this.A4Affination = A4Affination;
        this.divisions = 32;
        this.minVolume = 0.0f;
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    /**
     * FIXME: Definición de {@code printMusicSheet}. Muestra en pantalla los elementos de la lista.
     */
    public void printMusicSheet() {
        // Muestra la cantidad de nodos.
        Log.d("printList", "----------");
        Log.d("printList", "----------");
        Log.d("printList", "A4 is: " + A4Affination);
        Log.d("printList", "Divisions is: " + divisions);
        Log.d("printList", "Min Volume is: " + minVolume);
        Log.d("printList", "Size is: " + size);

        // aux apunta al primer nodo de la lista.
        MusicSymbol aux = this.getFirst();

        // Si la lista está vacía.
        if (this.getFirst() == null) {
            Log.d("printList", "La lista está vacía");
        } // Si la lista no está vacía.
        else {
            // Recorre la lista hasta llegar al incio.
            do {
                aux.printMusicSymbol();             // Imprime el simbolo musical.
                aux = aux.getNext();                // Avanza al siguiente nodo.
            } while (aux != this.getFirst());
        }

        Log.d("printList", "----------");
        Log.d("printList", "----------");
    }

    /**
     * FIXME: Definición de {@code addSymbol}. Agrega un simbolo musical a lista ligada Circular.
     *
     * @param symbol    Es el simbolo musical que se insertará.
     * @param reference Es un simbolo musical base para insertar este nuevo nodo, ya sea antes o
     *                  después.
     * @param after     Si el nuevo nodo se agregará en la lista antes (false) ó después (true) de
     *                  symbol.
     */
    public void addSymbol(MusicSymbol symbol, MusicSymbol reference, boolean after) {
        // Evalúa si la lista está vacia.
        if (size == 0) {
            symbol.setNext(symbol);                             // Indica que después del nueva nodo va si mismo.
            symbol.setPrev(symbol);                             // Indica que antes del nueva nodo va si mismo.
            first = symbol;                                     // El nodo nuevo ahora será first.
            last = symbol;                                      // El nodo nuevo ahora será last.
            first.setNumber(1);                                 // Indica el número del primer nodo.
        } // Si se agregará después.
        else if (after) {
            MusicSymbol next = reference.getNext();             // Crea copia del siguiente nodo al nodo parámetro.

            next.setPrev(symbol);                               // Indica que antes de next va el nuevo nodo.
            reference.setNext(symbol);                          // Indica que después del nodo parámetro va el nuevo nodo.

            symbol.setNext(next);                               // Indica que después del nuevo nodo va next.
            symbol.setPrev(reference);                          // Indica que antes del nuevo nodo va symbol.

            // Evalúa si se agregó después del último nodo (al final de la lista).
            if (reference == last) {
                last = symbol;                                  // Indica el nuevo nodo es ahora el último nodo.
                last.setNumber(size + 1);                         // Indica el número del último nodo.
            } // Si no se agregó al final.
            else
                symbol.setNumber(reference.getNumber() + 1);      // Indica que el número del nodo es igual al del anterior+1.

        } // Si se agregará antes.
        else {
            MusicSymbol prev = reference.getPrev();             // Crea copia del anterior nodo al nodo parámetro.

            prev.setNext(symbol);                               // Indica que después de prev va el nuevo nodo.
            reference.setPrev(symbol);                          // Indica que antes del nodo parámetro va el nuevo nodo.

            symbol.setPrev(prev);                               // Indica que antes del nuevo nodo va prev.
            symbol.setNext(reference);                          // Indica que después del nuevo nodo va symbol.

            // Evalúa si se agregó antes del primer nodo.
            if (reference == first) {
                first.setNumber(2);                             // Indica que el primer nodo ahora será el segundo.
                first = symbol;                                 // Indica que el nuevo nodo ahora es el primero.
                first.setNumber(1);                             // Actualiza el número del primer nodo.
            } // Si no se agregó antes del primer nodo.
            else {
                symbol.setNumber(reference.getNumber());        // Indica que el número del nodo nuevo es el del nodo parámatro.
                reference.setNumber(reference.getNumber() + 1);   // Indica que el número del nodo parámetro es el siguiente al nuevo nodo.
            }
        }
        size++;                                                // Actualiza la cantidad de elementos de la lista.
    }

    /**
     * FIXME: Definición de {@code deleteSymbol}. Quita un nodo de una lista ligada Circular.
     *
     * @param nodo Indica el nodo que se borrará de la lista.
     */
    public void deleteSymbol(MusicSymbol nodo) {
        MusicSymbol prev = nodo.getPrev();              // Crea copia del anterior nodo.
        MusicSymbol next = nodo.getNext();              // Crea copia del siguiente nodo.
        prev.setNext(next);                             // Pone prev como el anterior nodo de next.
        next.setPrev(prev);                             // Pone next como el anterior nodo de prev.
        nodo.setPrev(null);                             // Asigna su siguiente nodo como null.
        nodo.setNext(null);                             // Asigna su anterior nodo como null.

        if (nodo == last)                                // Si borró el último nodo de la lista.
            last = prev;                                // Actualiza el valor del último nodo.
        else if (nodo == first) {                        // Si borró el primer nodo de la lista.
            first = next;                               // next es ahora el primer nodo.
            first.setNumber(1);                         // Asigna el número de nodo a next.
        }
        size--;                                         // Reduce la cantidad de elementos de la lista.
    }

    /**
     * FIXME: Definición de {@code swapSymbols}. Intercambia la posición de dos nodos en una lista
     * ligada circular.
     *
     * @param nodo1 El primer nodo.
     * @param nodo2 El segundo nodo.
     */
    public void swapSymbols(MusicSymbol nodo1, MusicSymbol nodo2) {
        // Evalúa si después del segundo nodo sigue el primero.
        if (nodo1.getPrev() == nodo2) {
            MusicSymbol prev = nodo2.getPrev();             // Copia el anterior nodo al segundo.
            MusicSymbol next = nodo1.getNext();             // Copia el siguiente nodo al primero.
            prev.setNext(nodo1);                            // Indica que luego de prev va el primer nodo.
            next.setPrev(nodo2);                            // Indica que antes de next va el segundo nodo.
            nodo1.setPrev(prev);                            // Indica que antes del nodo1 va prev.
            nodo1.setNext(nodo2);                           // Indica que después del nodo1 va el segundo.
            nodo2.setPrev(nodo1);                           // Indica que antes del nodo2 va el primero.
            nodo2.setNext(next);                            // Indica que después del nodo2 va next.

            int numberAux = nodo1.getNumber();              // Copia el número del primer nodo.
            nodo1.setNumber(nodo2.getNumber());             // Pone el número del segundo nodo en el primero.
            nodo2.setNumber(numberAux);                     // Pone el número del primer nodo en el segundo.

            // Si el nodo1 era el último nodo indica que ahora el último nodo es el nodo2.
            if (last == nodo1)
                last = nodo2;
            // Si el nodo2 era el último nodo indica que ahora el último nodo es el nodo1.
            else if (last == nodo2)
                last = nodo1;
            // Si el nodo1 era el primer nodo, indica que ahora el primer nodo es el nodo2.
            else if (first == nodo1)
                first = nodo2;
            // Si el nodo2 era el primer nodo, indica que ahora el primer nodo es el nodo1.
            else if (first == nodo2)
                first = nodo1;

        } // Evalúa si depués del primer nodo sigue el segundo.
        else if (nodo1.getNext() == nodo2) {
            MusicSymbol prev = nodo1.getPrev();             // Copia el anterior nodo al primero.
            MusicSymbol next = nodo2.getNext();             // Copia el siguiente nodo al segundo.
            prev.setNext(nodo2);                            // Indica que luego de prev va el segundo nodo.
            next.setPrev(nodo1);                            // Indica que antes de next va el primer nodo.
            nodo2.setPrev(prev);                            // Indica que antes del nodo2 va prev.
            nodo2.setNext(nodo1);                           // Indica que después del nodo2 va el primero.
            nodo1.setPrev(nodo2);                           // Indica que antes del nodo2 va el segundo.
            nodo1.setNext(next);                            // Indica que después del nodo1 va next.

            int numberAux = nodo1.getNumber();              // Copia el número del primer nodo.
            nodo1.setNumber(nodo2.getNumber());             // Pone el número del segundo nodo en el primero.
            nodo2.setNumber(numberAux);                     // Pone el número del primer nodo en el segundo.

            // Si el nodo1 era el último nodo indica que ahora el último nodo es el nodo2.
            if (last == nodo1)
                last = nodo2;
            // Si el nodo2 era el último nodo indica que ahora el último nodo es el nodo1.
            else if (last == nodo2)
                last = nodo1;
            // Si el nodo1 era el primer nodo, indica que ahora el primer nodo es el nodo2.
            else if (first == nodo1)
                first = nodo2;
            // Si el nodo2 era el primer nodo, indica que ahora el primer nodo es el nodo1.
            else if (first == nodo2)
                first = nodo1;
        } // Si los nodos no son adyacentes y no son el mismo nodo.
        else if (nodo1 != nodo2) {
            MusicSymbol prev1 = nodo1.getPrev();            // Copia el anterior nodo al primero.
            MusicSymbol next1 = nodo1.getNext();            // Copia el siguiente nodo al primero.
            MusicSymbol prev2 = nodo2.getPrev();            // Copia el anterior nodo al segundo.
            MusicSymbol next2 = nodo2.getNext();            // Copia el siguiente nodo al segundo.

            prev1.setNext(nodo2);                           // Indica que luego de prev1 va el segundo nodo.
            next1.setPrev(nodo2);                           // Indica que antes de next1 va el segundo nodo.
            prev2.setNext(nodo1);                           // Indica que luego de prev2 va el primer nodo.
            next2.setPrev(nodo1);                           // Indica que luego de next2 va el primer nodo.

            nodo1.setPrev(prev2);                           // Indica que antes de nodo1 va prev2.
            nodo1.setNext(next2);                           // Indica que luego de nodo1 va next2.
            nodo2.setPrev(prev1);                           // Indica que antes de nodo2 va prev1.
            nodo2.setNext(next1);                           // Indica que luego de nodo2 va next1.

            int numberAux = nodo1.getNumber();              // Copia el número del primer nodo.
            nodo1.setNumber(nodo2.getNumber());             // Pone el número del segundo nodo en el primero.
            nodo2.setNumber(numberAux);                     // Pone el número del primer nodo en el segundo.

            // Si el nodo1 era el último nodo indica que ahora el último nodo es el nodo2.
            if (last == nodo1)
                last = nodo2;
            // Si el nodo2 era el último nodo indica que ahora el último nodo es el nodo1.
            else if (last == nodo2)
                last = nodo1;
            // Si el nodo1 era el primer nodo, indica que ahora el primer nodo es el nodo2.
            else if (first == nodo1)
                first = nodo2;
            // Si el nodo2 era el primer nodo, indica que ahora el primer nodo es el nodo1.
            else if (first == nodo2)
                first = nodo1;
        }
    }

    /**
     * FIXME: Definición de {@code refreshListNumbers}. Actualiza los números de cada simbolo
     * musical de la lista.
     */
    public void refreshListNumbers() {
        MusicSymbol aux = this.getFirst();
        short i = 1;

        do {
            aux.setNumber(i);
            i++;
            aux = aux.getNext();
        } while (aux != this.getFirst());
    }

    /**
     * FIXME: Definición de {@code addMusicSheet}. Liga la partitura con una partitura parámetro
     * ligando el último nodo con el primero de la partitura parámetro. Como resultado se obtiene
     * que ambas partituras ahora comparten los mismos nodos, pero no los demás atributos.
     *
     * @param sheet La partitura que se ligará al final.
     */
    public void addMusicSheet(MusicSheet sheet) {
        MusicSymbol first1 = first;                 // Crea una copia del primer nodo de la lista.
        MusicSymbol first2 = sheet.getFirst();      // Crea una copia del primer nodo de la lista que se ligará al final.
        MusicSymbol last1 = last;                   // Crea una copia del último nodo de la lista.
        MusicSymbol last2 = sheet.getLast();        // Crea una copia del último nodo de la lista que se ligará al final.

        last1.setNext(first2);                      // Indica que después del último nodo de la lista va el primer nodo de la lista parámetro.
        first2.setPrev(last1);                      // Indica que antes del primer nodo de la lista parámetro va el último nodo de la lista.
        last2.setNext(first1);                      // Indica que después del último nodo de la lista parámetro va el primer nodo de la lista.
        first1.setPrev(last2);                      // Indica que antes del primer nodo de la lista va el último nodo de la lista parámetro.

        last = last2;                               // Indica que el último nodo de la lista es ahora el último de la lista parámetro.
        sheet.setFirst(first);                      // Indica que el primer nodo de la lista parámetro es ahora el primer nodo de la lista.

        size += sheet.getSize();                    // Indica que el tamaño de la lista es ahora la suma de los tamaños de ambas listas.
        sheet.setSize(size);                        // Indica que la segunda lista tiene igual tamaño que la primera.
    }
}

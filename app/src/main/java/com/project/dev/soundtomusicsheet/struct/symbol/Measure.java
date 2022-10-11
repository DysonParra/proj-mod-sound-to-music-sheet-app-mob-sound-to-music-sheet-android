/*
 * @fileoverview {Measure} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {Measure} fue realizada el 31/07/2022.
 * @Dev - La primera version de {Measure} fue escrita por Dyson A. Parra T.
 */
package com.project.dev.soundtomusicsheet.struct.symbol;

import com.project.dev.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import lombok.Data;

/**
 * TODO: Definición de {@code Measure}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class Measure extends MusicSymbol {

    private int measureNumber;                              // Indica el número de compás de la partitura.
    private int noteQuantity;                               // Define la cantidad de notas que cabrán en los próximos compases.
    private int noteType;                                   // Define el tipo de notas que cabrán en los próximos compases.
    private String key;                                     // Define la nueva clave de la partitura.
    private int keyLine;                                    // Define la nueva línea de la clave en la partitura.
    private int clefOctave;                                 // Indica cuantas octavas se transportará la clave de la partitura.
    private int keySignature;                               // Define la nueva armadura de clave de la partitura.
    private boolean editMeasureInfo;                        // Si el objeto tipo compás edita información el la partitura (true), o es solo un compás de separación (false).

    /**
     * TODO: Definición de {@code Measure}.
     *
     * @param measureNumber
     */
    public Measure(int measureNumber) {
        this.measureNumber = measureNumber;                 // Inicializa el número de compás.
        this.noteQuantity = 0;                              // Incializa quantity en 0.
        this.noteType = 0;                                  // Incializa noteType en 0.
        this.key = "0";                                     // Incializa key en 0.
        this.keyLine = 0;                                   // Incializa keyLine en 0.
        this.keySignature = 0;                              // Inicializa por defecto sin armadura de clave.
        this.clefOctave = 0;                                // Inicializa sin transporte por octavas.
        this.editMeasureInfo = false;                       // Indica que es un compás de separación.
    }

    /**
     * TODO: Definición de {@code Measure}.
     *
     * @param measureNumber
     * @param quantity
     * @param keySignature
     * @param clefOctave
     * @param noteType
     * @param keyLine
     * @param key
     */
    public Measure(int measureNumber, int quantity, int noteType, String key, int keyLine, int clefOctave, int keySignature) {
        this.measureNumber = measureNumber;                 // Asigna valor al número de compás.
        this.noteQuantity = quantity;                       // Asigna valor a quantity.
        this.noteType = noteType;                           // Asigna valor a noteType.
        this.key = key;                                     // Asigna valor a la clave.
        this.keyLine = keyLine;                             // Asigna valor a la linea de clave.
        this.keySignature = keySignature;                   // Asigna valor a la armadura de clave.
        this.clefOctave = clefOctave;                       // Asigna valor al transporte de la clave.
        this.editMeasureInfo = true;                        // Indica que es un compás que edita información en la partitura.
    }

    /**
     * FIXME: Definición de {@code calculateBeats}. Obtiene cuantos pulsos caben en el compás actual
     * basado en las divisiones de la partitura.
     *
     * @param divisions Indica las divisiones de la partitura.
     * @return La cantidad de pulsos del que debe tener el compás.
     */
    public int calculateBeats(int divisions) {
        // Crea un entero que tendrá la catidad de pulsos del compás.
        float beats;

        // Evalúa el tipo de notas del compás y asigna la cantidad de pulsos de dicha nota a beats.
        switch (this.getNoteType()) {
            case 1:             // Redonda.
                beats = 4;
                break;
            case 2:             // Blanca.
                beats = 2;
                break;
            case 4:             // Negra.
                beats = 1;
                break;
            case 8:             // Corchea.
                beats = 0.5f;
                break;
            case 16:            // Semicorchea.
                beats = 0.25f;
                break;
            case 32:            // Fusa.
                beats = 0.125f;
                break;
            case 64:            // Semifusa.
                beats = 0.0625f;
                break;
            case 128:           // Garrapatea.
                beats = 0.03125f;
                break;
            default:
                beats = 0;
                System.out.printf("Error converting beats to note\n");
                break;
        }

        /*
         * A beats le lleva la duración de la nota multiplicada por la cantidad de veces que cabe
         * dicha note en el compás, por la cantidad de divisiones de la partitura.
         */
        beats = this.getNoteQuantity() * beats * divisions;

        // Devuelve la cantidad de pulsos del compás.
        return (int) beats;
    }

    /**
     * FIXME: Definición de {@code addToXmlFile}. Agrega un compás a la partitura.
     *
     * @param source    Es la ruta del archivo de partitura.
     * @param divisions Indica las divisiones de la partitura.
     */
    @Override
    public void addToXmlFile(String source, int divisions) {
        // Se crea un objeto File que se encarga de crear o abrir acceso a un archivo.
        File archivo = new File(source);

        // Intenta abrir el archivo para su edición.
        try ( FileWriter xml = new FileWriter(archivo, true)) {
            // Escribe la marca de cierre de compás.
            if (this.getMeasureNumber() != 1)
                xml.write("      </measure>" + "\r\n");

            // Se escribe sobre el archivo el número del compás.
            xml.write("    <measure number=\"" + this.getMeasureNumber() + "\">" + "\r\n");

            // Si el compás se añadió porque se encontró un nodo de tipo compás se agregan los cambios.
            if (this.isEditMeasureInfo()) {
                // Escribe las divisiones de la partitura y la armadura.
                xml.write("      <attributes>" + "\r\n"
                        + "        <divisions>" + divisions + "</divisions>" + "\r\n"
                        + "        <key>" + "\r\n"
                        + "          <fifths>" + this.getKeySignature() + "</fifths>" + "\r\n"
                        + "          </key>" + "\r\n"
                );

                // Si el nodo cambia el indicador, agrega el cambio a la partitura.
                if (this.getNoteQuantity() != 0) {
                    xml.write("        <time>" + "\r\n"
                            + "          <beats>" + this.getNoteQuantity() + "</beats>" + "\r\n"
                            + "          <beat-type>" + this.getNoteType() + "</beat-type>" + "\r\n"
                            + "          </time>" + "\r\n"
                    );
                }

                // Si el nodo cambia la clave, agrega el cambio a la partitura.
                if (!"0".equals(this.getKey())) {
                    xml.write("        <clef>" + "\r\n"
                            + "          <sign>" + this.getKey() + "</sign>" + "\r\n"
                            + "          <line>" + this.getKeyLine() + "</line>" + "\r\n"
                            + "          <clef-octave-change>" + this.getClefOctave() + "</clef-octave-change>" + "\r\n"
                            + "          </clef>" + "\r\n"
                    );
                }

                // Cierra el cambio en los atributos.
                xml.write("        </attributes>" + "\r\n");
            }

            // Se guarda lo escrito.
            xml.close();
        } // Si existe un problema al escribir el compás entra aquí.
        catch (Exception e) {
            System.out.println("Error making measure");
        }
    }

    /**
     * FIXME: Definición de {@code printMusicSymbol}. Imprime el simbolo musical en pantalla.
     */
    @Override
    public void printMusicSymbol() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();         // Crea objeto de tipo ByteArrayOutputStream.
        PrintStream printer = new PrintStream(os);                      // Crea objeto de tipo PrintStream.
        String output;                                                  // Crea String.

        printer.printf("#%3d (next #%3d) (prev #%3d)  Measure info is  Nro%3d %2d/%d (%s%d%3d) Sign %2d  Edit info %5b",
                this.getNumber(), this.getNext().getNumber(), this.getPrev().getNumber(),
                this.getMeasureNumber(),
                this.getNoteQuantity(), this.getNoteType(),
                this.getKey(), this.getKeyLine(), this.getClefOctave(),
                this.getKeySignature(), this.isEditMeasureInfo());

        /*
         * -
         * printer.printf("sheet.addSymbol(new Measure(%3d, %d, %d, \"%2s\", %d, %d, %2d),
         * sheet.getLast(), true);", this.getMeasureNumber(), this.getNoteQuantity(),
         * this.getNoteType(), this.getKey(), this.getKeyLine(), this.getClefOctave(),
         * this.getKeySignature());
         */
        output = os.toString();                                                 // Asigna a output el buffer os.
        Log.d("printList", output);                                        // Muestra en consola output.
    }
}

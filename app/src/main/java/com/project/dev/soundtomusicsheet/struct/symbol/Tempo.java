/*
 * @fileoverview    {Tempo}
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

import com.project.dev.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import lombok.Data;

/**
 * TODO: Definición de {@code Tempo}.
 *
 * @author Dyson Parra
 * @since 11
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class Tempo extends MusicSymbol {

    private String type;                                    // Tipo de nota del tempo (blanca, negra o corchea).
    private int value;                                      // Cantidad de pulsos del tempo.

    /**
     * TODO: Definición de {@code Tempo}.
     *
     */
    public Tempo() {
        this.type = "";                                     // Inicializa por defecto sin tipo.
        this.value = 0;                                     // Inicializa por defecto en cero.
    }

    /**
     * TODO: Definición de {@code Tempo}.
     *
     * @param type
     * @param tempo
     */
    public Tempo(String type, int tempo) {
        this.type = type;                                   // Asigna valor al tipo.
        this.value = tempo;                                 // Asigna valor al tempo.
    }

    /**
     * FIXME: Definición de {@code addToXmlFile}. Agrega un cambio en el tempo de la partitura.
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
            // Agrega el tempo a la partitura.
            xml.write("      <direction placement=\"above\">" + "\r\n"
                    + "        <direction-type>" + "\r\n"
                    + "          <metronome parentheses=\"no\">" + "\r\n"
                    + "            <beat-unit>" + this.getType() + "</beat-unit>" + "\r\n"
                    + "            <per-minute>" + this.getValue() + "</per-minute>" + "\r\n"
                    + "            </metronome>" + "\r\n"
                    + "          </direction-type>" + "\r\n"
                    + "        <staff>1</staff>" + "\r\n"
                    + "        <sound tempo=\"" + this.getValue() + "\"/>" + "\r\n"
                    + "        </direction>" + "\r\n"
            );

            // Se guarda lo escrito.
            xml.close();
        } // Si existe un problema al escribir el compás entra aquí.
        catch (Exception e) {
            System.out.println("Error adding tempo");
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

        printer.printf("#%3d (next #%3d) (prev #%3d)  Tempo is %3d (%s)",
                this.getNumber(), this.getNext().getNumber(), this.getPrev().getNumber(),
                this.getValue(), this.getType());

        /*
         * -
         * printer.printf("sheet.addSymbol(new Tempo(%3d), sheet.getLast(), true);",
         * this.getTempo());
         */
        output = os.toString();                                         // Asigna a output el buffer os.
        Log.d("printList", output);                                     // Muestra en consola output.
    }
}

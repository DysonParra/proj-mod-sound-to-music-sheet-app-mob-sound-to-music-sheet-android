/*
 * @fileoverview    {XmlFile}
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
package com.project.dev.soundtomusicsheet.struct;

import com.project.dev.soundtomusicsheet.struct.MusicSheet;
import com.project.dev.soundtomusicsheet.struct.symbol.Measure;
import com.project.dev.soundtomusicsheet.struct.symbol.Note;
import com.project.dev.soundtomusicsheet.struct.symbol.Tempo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import lombok.Data;

/**
 * TODO: Definición de {@code XmlFile}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class XmlFile implements Serializable {

    private String source;                              // Indica la ruta del archivo de partitura Xml.
    private boolean ignoreRestsAtEnd;                   // Si se borrarán los descansos al final del archivo de partitura xml.

    /**
     * TODO: Definición de {@code XmlFile}.
     *
     */
    public XmlFile() {
        this.source = "";
        this.ignoreRestsAtEnd = false;
    }

    /**
     * TODO: Definición de {@code XmlFile}.
     *
     * @param source
     * @param ignoreRestsAtEnd
     */
    public XmlFile(String source, boolean ignoreRestsAtEnd) {
        this.source = source;
        this.ignoreRestsAtEnd = ignoreRestsAtEnd;
    }

    /**
     * FIXME: Definición de {@code validateXml}. Verifica si el objeto de partitura xml se puede
     * usar para agregar notas al final.
     *
     * @return true si se puede usar (Un solo pentagrama y una solo nota a la vez), false en caso
     *         contrario.
     */
    public int validateXml() {
        int valid = -5;                                         // Si la partitura es válida (tiene un solo pentagrama y una sola voz).

        try {
            File archivo = new File(source);                    // Se crea un objeto File que se encarga de crear o abrir acceso a un archivo.
            FileReader xml = new FileReader(archivo);           // Crea objeto FileWriter que ayuda a leer el archivo.
            BufferedReader br = new BufferedReader(xml);        // Crea objeto encargado de leer un archivo.
            String line;                                        // Crea un Strin para almacenar cada línea del xml.
            // Ciclo infinito.
            for (int i = 1;; i++) {
                // A línea le lleva la linea actual.
                line = br.readLine();

                // Si terminó el archivo sale del ciclo.
                if (line == null)
                    break;
                // Si aún no termina el archivo.
                else {
                    // Le quita espacios al final y al principio a line.
                    line = line.trim();

                    switch (line) {
                        // Si encuentra un instrumento indica que es valido el xml.
                        case ("<score-part id=\"P1\">"):
                            valid = 1;
                            break;
                        // Si encuentra más de un instrumento.
                        case ("<score-part id=\"P2\">"):
                        case ("<score-part id=\"P3\">"):
                        case ("<score-part id=\"P4\">"):
                        case ("<score-part id=\"P5\">"):
                        case ("<score-part id=\"P6\">"):
                        case ("<score-part id=\"P7\">"):
                        case ("<score-part id=\"P8\">"):
                        case ("<score-part id=\"P9\">"):
                            valid = -1;
                            break;
                        // Si encuentra que la partitura es para piano (2 pentagramas unidos con corchete).
                        case ("<staves>2</staves>"):
                            valid = -2;
                            break;
                        // Si encuentra más de dos voces en la partitura (No es instrumento melódico).
                        case ("<voice>2</voice>"):
                        case ("<voice>3</voice>"):
                        case ("<voice>4</voice>"):
                        case ("<voice>5</voice>"):
                        case ("<voice>6</voice>"):
                        case ("<voice>7</voice>"):
                        case ("<voice>8</voice>"):
                        case ("<voice>9</voice>"):
                            valid = -3;
                            break;
                        // Si encuentra más de dos notas tocadas simultaneamente en una misma voz (No es melódico).
                        case ("<chord/>"):
                            valid = -4;
                            break;
                    }
                }
            }
        } catch (Exception e) {
            valid = -5;
        }

        return valid;
    }

    /**
     * FIXME: Definición de {@code atoi}. Obtiene los números de un string.
     *
     * @param str String al que se le hallarán los números que tiene.
     * @return los números del String como entero.
     */
    private int atoi(String str) {
        String strAux = "";

        char charActChar;
        String charActStr;

        for (int i = 0; i <= str.length() - 1; i++) {                                   // Recorre cada uno de los caracteres del String.
            charActChar = str.charAt(i);                                        // A charActChar le asigna el caracter actual en char.

            if (charActChar > 47 && charActChar < 58) {
                charActStr = String.valueOf(charActChar);
                strAux = strAux.concat(charActStr);
            }
        }

        int number = 0;

        if (!(strAux.isEmpty()))
            number = Integer.parseInt(strAux);

        return number;
    }

    /**
     * FIXME: Definición de {@code obtainValue}. Obtiene un valor de un string encerrado en ">?<"
     *
     * @param str String al que se le hallará edicho valor.
     * @return el valor de el String encerrado en ><.
     */
    private String obtainValue(String str) {
        String strAux = "";
        boolean found = false;
        char charActChar;

        for (int i = 0; i <= str.length() - 1; i++) {                                   // Recorre cada uno de los caracteres del String.
            charActChar = str.charAt(i);                                        // A charActChar le asigna el caracter actual en char.

            if (charActChar == 62)       //>
                found = true;
            else if (charActChar == 60)  //<
                found = false;
            else if (found)
                strAux = strAux.concat(String.valueOf(charActChar));
        }

        return strAux;
    }

    /**
     * FIXME: Definición de {@code convertToMusicSheet}. Convierte un archivo de partitura xml en un
     * objeto de tipo partitura.
     *
     * @return Un objeto de tipo partitura.
     */
    public MusicSheet convertToMusicSheet() {
        try {
            File archivo = new File(source);                                                        // Se crea un objeto File que se encarga de crear o abrir acceso a un archivo.
            FileReader xml = new FileReader(archivo);                                               // Crea objeto FileWriter que ayuda a leer el archivo.
            BufferedReader br = new BufferedReader(xml);                                            // Crea objeto encargado de leer un archivo.
            String line;                                                                            // Crea un String para almacenar cada línea del xml.

            MusicSheet sheet = new MusicSheet((440f));                                              // Crea un objeto de tipo partitura.
            int meaureNumber = 0;                                                                   // Número de compás en la partitura.

            // Almacena la linea actual del archivo xml en el String line.
            line = br.readLine();

            // Ciclo infinito.
            for (int i = 1;; i++) {
                // Si terminó de recorrer el archivo sale del ciclo.
                if (line == null)
                    break;
                // Si encuentra una linea que indica inicio d eun compás.
                else if (line.contains("<measure")) {
                    meaureNumber++;                                                                 // Aumenta measureNumber.
                    Measure measure = new Measure(meaureNumber);                                    // Crea un objeto de tipo compás con el número de measureNumber.
                    sheet.addSymbol(measure, sheet.getLast(), true);                                // Agrega measure a la lista ligada.
                    line = br.readLine().trim();                                                    // Pasa a la siguiente línea del Xml.

                    // Mientras no encuentre el fin del compás.
                    while (!line.contains("/measure")) {
                        // Si encuentra línea que indica saltos de línea o página entre compases.
                        if (line.contains("<print"))
                            while (!line.contains("/print"))                                        // Mientras no encuentre la línea que indica el cierre de print.
                                line = br.readLine().trim();                                        // Pasa a la siguiente línea del Xml.

                        // Si encuentra cambio en atributos de los compases.
                        if (line.contains("<attributes>")) {
                            measure.setEditMeasureInfo(true);
                            // Mientras no encuentre el indicador de cierre en los atributos.
                            while (!line.contains("</attributes>")) {
                                line = br.readLine().trim();                                        // Pasa a la siguiente línea del Xml.

                                // Si encuentra la cantidad de divisiones de la partitura.
                                if (line.contains("divisions"))
                                    sheet.setDivisions(atoi(line));                                 // Almacena la cantidad de divisiones.

                                // Si encuentra cambio en indicador de compás.
                                else if (line.contains("<beats>"))
                                    measure.setNoteQuantity(atoi(line));                            // Almacena la cantidad de notas que cabrán en los compases desde ahora.
                                else if (line.contains("<beat-type>"))
                                    measure.setNoteType(atoi(line));                                // Almacena el tipo de notas que cabrán en los compases desde ahora.

                                // Si encuentra cambio en la clave.
                                else if (line.contains("<sign>"))
                                    measure.setKey(obtainValue(line));                              // Almacena la nueva clave.
                                else if (line.contains("<line>"))
                                    measure.setKeyLine(atoi(line));                                 // Almacena la línea de clave.

                                // Si encuentra armadura de clave.
                                else if (line.contains("<fifths>"))
                                    measure.setKeySignature(Byte.valueOf(obtainValue(line)));       // Almacena la nueva armadura de clave.
                            }
                        } // Si encuentra cambio en el tempo.
                        else if (line.contains("<direction placement")) {
                            Tempo tempo = new Tempo();                                              // Crea un objeto tipo tempo.

                            // Mientras no encuentre el indicador de cierre de la información del tempo.
                            while (!line.contains("</direction>")) {
                                line = br.readLine().trim();                                        // Pasa a la siguiente línea del Xml.

                                // Si encuentra el valor del tempo.
                                if (line.contains("sound tempo="))
                                    tempo.setValue(atoi(line));                                     // Almacena el nuevo tempo encontrado.
                                // Si encuentra el tipo de nota del tempo.
                                else if (line.contains("<beat-unit>"))
                                    tempo.setType(obtainValue(line));                               // Almacena el tipo de tempo.
                            }

                            sheet.addSymbol(tempo, sheet.getLast(), true);                      // Agrega un nuevo nodo de tipo tempo con el tempo encontrado.
                        } // Si encuentra una nota.
                        else if (line.contains("<note")) {
                            Note nota = new Note();                                                 // Crea un objeto tipo nota.

                            // Mientras no encuentre el indicador de cierre de nota.
                            while (!line.contains("</note>")) {
                                line = br.readLine().trim();                                        // Pasa a la siguiente línea del Xml.

                                // Si encuentra un descanso.
                                if (line.contains("<rest/>"))
                                    nota.setStep("0");                                              // Almacene cero en la altura.

                                // Si encuentra la altura de la nota.
                                else if (line.contains("<step>"))
                                    nota.setStep(obtainValue(line));                                // Almacena la nota encontrada.

                                // Si encuentra una alteración.
                                if (line.contains("<alter>"))
                                    nota.setAlter(obtainValue(line));                               // Almacena la alteración.

                                // Si encuentra una octava.
                                else if (line.contains("<octave>"))
                                    nota.setOctave(obtainValue(line));                              // Almacena la octava encontrada.

                                // Si encuentra una duración en pulsos.
                                else if (line.contains("<duration>"))
                                    nota.setBeats(atoi(line));                                      // Almacena la duración en pulsos.

                                // Si encuentra una ligadura.
                                else if (line.contains("tie type=")) {
                                    // Si encuentra una ligadura de inicio.
                                    if (line.contains("start"))
                                        nota.setTie(1);

                                    // Si encuentra una ligadura con marca "stop" significa que la ligadura es intermedia o al final.
                                    else if (line.contains("stop")) {
                                        line = br.readLine().trim();                                // Pasa a la siguiente línea del Xml.

                                        // Si encuentra un "start" significa que es ligadura intermedia.
                                        if (line.contains("start")) {
                                            nota.setTie(2);                                         // Almacena la ligadura.
                                            line = br.readLine();                                   // Lee la siguiente linea.
                                        } // Si no encuentra "start" significa que era ligadura al final.
                                        else
                                            nota.setTie(3);                                         // Almacena la ligadura.
                                    }
                                } // Si encuentra un puntillo.
                                else if (line.contains("<dot/>"))
                                    nota.setDot(nota.getDot() + 1);                                 // Aumenta el número de puntillos de la nota.

                                // Si encuentra un staccato.
                                else if (line.contains("<staccato/>"))
                                    nota.setStaccato(true);                                         // Indica que la nota lleva staccato.
                            }

                            sheet.addSymbol(nota, sheet.getLast(), (true));                         // Agrega la nota a la lista ligada.
                        }

                        line = br.readLine().trim();                                                // Pasa a la siguiente línea del Xml.
                    }
                } // Si la línea en el xml no indica inicio de un compás.
                else
                    line = br.readLine();                                                           // Pasa a la siguiente línea del Xml.
            }

            // Devuelve el objeto de tipo partitura.
            return sheet;
        } catch (IOException | NumberFormatException e) {
            return null;
        }
    }
}

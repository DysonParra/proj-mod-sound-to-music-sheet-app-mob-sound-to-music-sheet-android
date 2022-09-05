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
package com.project.dev.soundtomusicsheet.writer;

import com.project.dev.soundtomusicsheet.struct.MusicSheet;
import com.project.dev.soundtomusicsheet.struct.symbol.MusicSymbol;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import android.os.Environment;

/**
 * TODO: Definición de {@code MusicSheetFileWriter}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class MusicSheetFileWriter {

    /**
     * FIXME: Definición de {@code CreateXMLFile}. Crea un archivo xml vacío.
     *
     * @param musicSheet
     */
    private void CreateXMLFile(MusicSheet musicSheet) {
        // Crea directorio "sound-to-music-sheet".
        File folder = new File(Environment.getExternalStorageDirectory().getPath(), ("sound-to-music-sheet")); // En android (memoria interna).
        //File folder = new File("C:\\Users\\Guindos\\Desktop\\sound-to-music-sheet");                         // En java.
        folder.mkdir();

        // Crea directorio "Sheets" dentro del directorio "sound-to-music-sheet".
        folder = new File(folder.getAbsolutePath(), "Sheets");
        folder.mkdir();

        musicSheet.setSource(folder.getAbsolutePath().concat("/Sheet_"));                                   // Sintaxis que tendrá el nombre de cada archivo xml (en este caso será "Sheet_###.xml").

        int index = 1;                                                                                      // Sub-indice del nombre del archivo xml.
        String sourceAux;                                                                                   // Nombre y ruta del archivo creado.
        String numberStr;                                                                                   // Sub-indice en String.

        // Ciclo infinito.
        while (true) {
            // Agrega un indice al nombre del archivo.
            if (index < 10)
                numberStr = "00".concat(String.valueOf(index));
            else if (index < 100)
                numberStr = "0".concat(String.valueOf(index));
            else
                numberStr = String.valueOf(index);

            // Agrega el formato al archivo.
            sourceAux = musicSheet.getSource().concat(numberStr).concat(".xml");

            // Intenta abrir el archivo para ver si existe.
            try ( FileReader ignored = new FileReader(sourceAux)) {
                index++;                                                                                    // Aumenta el sub-indice para intentar guardar de nuevo.
            } // Si no se pudo abrir el archivo (no existe y entonces se puede guardar con ese nombre).
            catch (Exception e) {
                // Se crea un objeto File que se encarga de crear acceso a un archivo.
                File archivo = new File(sourceAux);

                // Crea objeto FileWriter que ayuda a escribir sobre el archivo.
                try ( FileWriter xml = new FileWriter(archivo, true)) {
                    xml.close();                                                                            // Se cierra el archivo.
                    System.out.printf("Sheet created in %s\n", sourceAux);                                  // Indica que se pudo guardar el archivo y en donde.
                    break;                                                                                  // Sale del ciclo infinito.
                } //Si existe un problema al crear el archivo xml entra aquí.
                catch (Exception f) {
                    System.out.println("Error creating Music Sheet");
                }
            }
        }

        // Actualiza el valor de la ruta de la partitura.
        musicSheet.setSource(sourceAux);

        // Almacena en number el número del archivo de partitura.
        musicSheet.setNumber(numberStr);
    }

    /**
     * FIXME: Definición de {@code WriteBasicInfoToXmlFile}. Escribe en un archivo xml la
     * información básica que debe tener una partitura.
     *
     * @param musicSheet
     */
    private void WriteBasicInfoToXmlFile(MusicSheet musicSheet) {
        String name = "Sheet ";                                                 // Crea String con el titulo de la partitura.
        String composer = "sound to music sheet";                               // Crea String con el compositor de la partitura.
        File archivo = new File(musicSheet.getSource());                        // Se crea un objeto File que se encarga de crear o abrir acceso a un archivo.

        // Intenta abrir el archivo para su edición.
        try ( FileWriter xml = new FileWriter(archivo, true)) {
            //Se escribe sobre el archivo. (Lo escrito solo es código xml necesario para que funcione cualquier partitura).
            xml.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\r\n"
                    + "<!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 3.0 Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\">" + "\r\n"
                    + "<score-partwise>" + "\r\n"
                    + "  <identification>" + "\r\n"
                    + "    <encoding>" + "\r\n"
                    + "      <software>MuseScore 2.0.2</software>" + "\r\n"
                    + "      <encoding-date>2017-09-19</encoding-date>" + "\r\n"
                    + "      <supports element=\"accidental\" type=\"yes\"/>" + "\r\n"
                    + "      <supports element=\"beam\" type=\"yes\"/>" + "\r\n"
                    + "      <supports element=\"print\" attribute=\"new-page\" type=\"yes\" value=\"yes\"/>" + "\r\n"
                    + "      <supports element=\"print\" attribute=\"new-system\" type=\"yes\" value=\"yes\"/>" + "\r\n"
                    + "      <supports element=\"stem\" type=\"yes\"/>" + "\r\n"
                    + "      </encoding>" + "\r\n"
                    + "    </identification>" + "\r\n"
                    + "  <defaults>" + "\r\n"
                    + "    <scaling>" + "\r\n"
                    + "      <millimeters>7.05556</millimeters>" + "\r\n"
                    + "      <tenths>40</tenths>" + "\r\n"
                    + "      </scaling>" + "\r\n"
                    + "    <page-layout>" + "\r\n"
                    + "      <page-height>1584</page-height>" + "\r\n"
                    + "      <page-width>1224</page-width>" + "\r\n"
                    + "      <page-margins type=\"even\">" + "\r\n"
                    + "        <left-margin>56.6929</left-margin>" + "\r\n"
                    + "        <right-margin>56.6929</right-margin>" + "\r\n"
                    + "        <top-margin>56.6929</top-margin>" + "\r\n"
                    + "        <bottom-margin>113.386</bottom-margin>" + "\r\n"
                    + "        </page-margins>" + "\r\n"
                    + "      <page-margins type=\"odd\">" + "\r\n"
                    + "        <left-margin>56.6929</left-margin>" + "\r\n"
                    + "        <right-margin>56.6929</right-margin>" + "\r\n"
                    + "        <top-margin>56.6929</top-margin>" + "\r\n"
                    + "        <bottom-margin>113.386</bottom-margin>" + "\r\n"
                    + "        </page-margins>" + "\r\n"
                    + "      </page-layout>" + "\r\n"
                    + "    <word-font font-family=\"FreeSerif\" font-size=\"10\"/>" + "\r\n"
                    + "    <lyric-font font-family=\"FreeSerif\" font-size=\"11\"/>" + "\r\n"
                    + "    </defaults>" + "\r\n"
                    + "  <credit page=\"1\">" + "\r\n"
                    + "    <credit-words default-x=\"612\" default-y=\"1527.31\" justify=\"center\" valign=\"top\" font-size=\"24\">" + name + musicSheet.getNumber() + "</credit-words>" + "\r\n"
                    + "    </credit>" + "\r\n"
                    + "  <credit page=\"1\">" + "\r\n"
                    + "    <credit-words default-x=\"1167.31\" default-y=\"1427.31\" justify=\"right\" valign=\"bottom\" font-size=\"12\">" + composer + "</credit-words>" + "\r\n"
                    + "    </credit>" + "\r\n"
                    + "  <part-list>" + "\r\n"
                    + "    <score-part id=\"P1\">" + "\r\n"
                    + "      <part-name>Piano</part-name>" + "\r\n"
                    + "      <part-abbreviation>Pno.</part-abbreviation>" + "\r\n"
                    + "      <score-instrument id=\"P1-I1\">" + "\r\n"
                    + "        <instrument-name>Piano</instrument-name>" + "\r\n"
                    + "        </score-instrument>" + "\r\n"
                    + "      <midi-device id=\"P1-I1\" port=\"1\"></midi-device>" + "\r\n"
                    + "      <midi-instrument id=\"P1-I1\">" + "\r\n"
                    + "        <midi-channel>1</midi-channel>" + "\r\n"
                    + "        <midi-program>1</midi-program>" + "\r\n"
                    + "        <volume>100</volume>" + "\r\n"
                    + "        <pan>0</pan>" + "\r\n"
                    + "        </midi-instrument>" + "\r\n"
                    + "      </score-part>" + "\r\n"
                    + "    </part-list>" + "\r\n"
                    + "  <part id=\"P1\">" + "\r\n"
            );

            //Se guarda lo escrito.
            xml.close();
        } //Si existe un problema al escribir entra aquí.
        catch (Exception e) {
            System.out.println("Error writing basic information in Music Sheet");
        }
    }

    /**
     * FIXME: Definición de {@code endXmlFile}. Escribe en la partitura código para finalizarla.
     *
     * @param musicSheet
     */
    private void endXmlFile(MusicSheet musicSheet) {
        // Se crea un objeto File que se encarga de crear o abrir acceso a un archivo.
        File archivo = new File(musicSheet.getSource());

        // Intenta abrir el archivo para su edición.
        try ( FileWriter xml = new FileWriter(archivo, true)) {
            // Escribe la linea de finalización de partitura.
            xml.write("      <barline location=\"right\">" + "\r\n"
                    + "        <bar-style>light-heavy</bar-style>" + "\r\n"
                    + "        </barline>" + "\r\n"
                    + "      </measure>" + "\r\n"
            );

            //Se escribe sobre el archivo código xml para finalizar la partitura.
            xml.write("    </part>" + "\r\n"
                    + "  </score-partwise>" + "\r\n"
            );

            //Se guarda lo escrito.
            xml.close();
        } //Si existe un problema al escribir entra aquí
        catch (Exception e) {
            System.out.println("Error ending Music Sheet");
        }
    }

    /**
     * FIXME: Definición de {@code addListToXML}. Agrega los nodos de un objeto de tipo partitura a
     * un archivo de partitura.
     *
     * @param musicSheet
     */
    private void addListToXML(MusicSheet musicSheet) {
        MusicSymbol aux = musicSheet.getFirst();                                 // Crea un objeto apuntando al primer nodo de la lista.

        // Mientras no haya recorrido toda la lista.
        do {
            aux.addToXmlFile(musicSheet.getSource(), musicSheet.getDivisions());// Escriba el simbolo musical en la partitura (compás, tempo o nota).
            aux = aux.getNext();                                                // Avanza al siguiente nodo.
        } while (aux != musicSheet.getFirst());
    }

    /**
     * FIXME: Definición de {@code writeMusicSheetToXmlFile}. Realiza el proceso desde creación
     * hasta finalización del xml.
     *
     * @param musicSheet Es la partitura que se escribirá en un archivo xml.
     */
    public void writeMusicSheetToXmlFile(MusicSheet musicSheet) {
        CreateXMLFile(musicSheet);                                              // Se crea el archivo de partitura y se lleva la ruta a source.
        WriteBasicInfoToXmlFile(musicSheet);                                    // Agrega información básica a la partitura.
        addListToXML(musicSheet);                                               // Agrega la lista al archivo xml.
        endXmlFile(musicSheet);                                                 // Cierra el archivo xml.
    }
}

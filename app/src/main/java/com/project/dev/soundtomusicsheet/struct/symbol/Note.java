/*
 * @fileoverview    {Note}
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
 * TODO: Description of {@code Note}.
 *
 * @author Dyson Parra
 * @since 11
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class Note extends MusicSymbol {

    private String step;                                    // Nombre de la nota (C, D, E, F... o 0 si es silencio).
    private String alter;                                   // Accidente o alteración de la nota (0 = no, 1 = sotenido, -1 = bemol).
    private String octave;                                  // Octava de la nota.
    private int beats;                                      // Cantidad de pulsos que dura la nota.
    private int tie;                                        // Si la nota está ligada con la siguiente de la lista, la anterior, ambas o no está ligada.
    private int dot;                                        // Si la nota lleva puntillo.
    private boolean staccato;                               // Si la nota lleva staccato.
    private int staff;                                      // Número de pentagrama de la nota (simpre se utilizará el primer pentagrama puesto que solo habrá uno).
    private int voice;                                      // Número de voz de la nota en el pentagrama (Siempre se usará la primera voz).

    /**
     * TODO: Description of {@code Note}.
     *
     */
    public Note() {
        this.step = "0";                                    // Inicializa note en "0".
        this.alter = "0";                                   // Inicializa sharp en "0".
        this.octave = "0";                                  // Inicializa octave en "0".
        this.beats = 0;                                     // Inicializa beats en 0.
        this.tie = 0;                                       // Inicializa por defecto sin liga.
        this.dot = 0;                                       // Inicializa por defecto sin puntillo.
        this.staccato = false;                              // Inicializa por defecto sin staccato.
        this.staff = 1;                                     // Inicializa por defecto en el primer pentagrama.
        this.voice = 1;                                     // Inicializa por defecto en la voz 1.
    }

    /**
     * TODO: Description of {@code Note}.
     *
     * @param step
     * @param alter
     * @param staccato
     * @param dot
     * @param octave
     * @param tie
     * @param beats
     */
    public Note(String step, String alter, String octave, int beats, int tie, int dot, boolean staccato) {
        this.step = step;                                   // Asigna valor a altura.
        this.alter = alter;                                 // Asigna valor a accidente.
        this.octave = octave;                               // Asigna valor a octava.
        this.beats = beats;                                 // Asigna valor a pulsos.
        this.tie = tie;                                     // Asigna valor a liga.
        this.dot = dot;                                     // Asigna valor puntillo.
        this.staccato = staccato;                           // Asigna valor staccato.
        this.staff = 1;                                     // Inicializa por defecto en el primer pentagrama.
        this.voice = 1;                                     // Inicializa por defecto en la voz 1.
    }

    /**
     * FIXME: Description of {@code convertBeatsToNote}. Obtiene el tipo de nota que es basado en una
     * cantidad de pulsos recibidos.
     *
     * @param beats     Es la cantidad de pulsos recibidos.
     * @param divisions Indica las divisiones de la partitura.
     * @return El tipo de nota que posee tantos pulsos recibidos como parámetro.
     */
    private String convertBeatsToNote(int beats, int divisions) {
        // Crea un String que tendrá el tipo de nota correspondiente a los pulsos recibidos.
        String type;

        double beatsAux = beats / (double) divisions;
        beats = (int) (beatsAux * 32.0);
        //System.out.printf("div is %d beats is %d aux is %f\n", divisions, beats, beatsAux);

        // Evalúa el valor de beats y modifica el valor de "type" dependiendo de la cantidad de pulsos.
        switch (beats) {
            case 128:           // Redonda.
                type = "whole";
                break;
            case 64:            // Blanca.
                type = "half";
                break;
            case 32:            // Negra.
                type = "quarter";
                break;
            case 16:            // Corchea.
                type = "eighth";
                break;
            case 8:             // Semicorchea.
                type = "16th";
                break;
            case 4:             // Fusa.
                type = "32nd";
                break;
            case 2:             // Semifusa.
                type = "64th";
                break;
            case 1:             // Garrapatea.
                type = "128th";
                break;
            default:
                type = "espec";
                System.out.println("Error converting beats to note");
                break;
        }

        // Devuelve el tipo de nota.
        return type;
    }

    /**
     * FIXME: Description of {@code addToXmlFile}. Escribe una nota dada en una partitura.
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
            // Nombre de la nota basado en su duración.
            String type = convertBeatsToNote(this.getBeats(), divisions);

            // Evalúa si la nota actual es un siencio.
            if ("0".equals(this.getStep())) {
                // Agrega el silencio a la partitura.
                xml.write("      <note>" + "\r\n"
                        + "        <rest/>" + "\r\n"
                        + "        <duration>" + this.getBeats() + "</duration>" + "\r\n"
                        + "        <voice>" + this.getVoice() + "</voice>" + "\r\n"
                        + "        <type>" + type + "</type>" + "\r\n"
                        + "        <staff>" + this.getStaff() + "</staff>" + "\r\n"
                        + "        </note>" + "\r\n"
                );
            } // Si la nota no es un silencio.
            else {
                // Agrega la nota a la partitura.
                xml.write("      <note>" + "\r\n"
                        + "        <pitch>" + "\r\n"
                        + "          <step>" + this.getStep() + "</step>" + "\r\n"
                        + "          <alter>" + this.getAlter() + "</alter>" + "\r\n"
                        + "          <octave>" + this.getOctave() + "</octave>" + "\r\n"
                        + "          </pitch>" + "\r\n"
                        + "        <duration>" + this.getBeats() + "</duration>" + "\r\n"
                );

                // Evalúa la ligadura de nota de la nota actual.
                switch (this.getTie()) {
                    // Si no tiene ligadura.
                    case 0:
                        break;
                    // Si tiene ligadura y empieza la ligadura desde dicha nota hacia las siguientes.
                    case 1:
                        xml.write("      <tie type=\"start\"/>" + "\r\n");
                        break;
                    // Si tiene ligadura, y la anterior y la siguiente nota van ligadas a esta.
                    case 2:
                        xml.write("      <tie type=\"stop\"/>" + "\r\n"
                                + "      <tie type=\"start\"/>" + "\r\n"
                        );
                        break;
                    // Si tiene ligadura y la anterior nota está ligada a esta, pero no la siguiente.
                    case 3:
                        xml.write("      <tie type=\"stop\"/>" + "\r\n");
                        break;
                    // Si hay algún error.
                    default:
                        System.out.println("Error 1 in tie");
                        break;

                }

                // Agrega voz y tipo de nota a la partitura.
                xml.write("        <voice>" + this.getVoice() + "</voice>" + "\r\n"
                        + "        <type>" + type + "</type>" + "\r\n"
                );

                // Evalúa si la nota tiene puntillo, doble puntillo ó no tiene.
                switch (this.getDot()) {
                    case (0):
                        break;
                    case (1):
                        xml.write("        <dot/>" + "\r\n");
                        break;
                    case (2):
                        xml.write("        <dot/>" + "\r\n"
                                + "        <dot/>" + "\r\n");
                        break;
                }

                // Si la nota tiene staccato.
                if (this.isStaccato()) {
                    xml.write("        <notations>" + "\r\n"
                            + "          <articulations>" + "\r\n"
                            + "            <staccato/>" + "\r\n"
                            + "            </articulations>" + "\r\n"
                            + "          </notations>" + "\r\n"
                    );
                } // Las partituras XML definen que si la nota tiene staccato no se incluye el número del instrumento.
                else
                    xml.write("        <staff>" + this.getStaff() + "</staff>" + "\r\n");

                // Evalúa la ligadura de nota de la nota actual.
                switch (this.getTie()) {
                    // Si no tiene ligadura.
                    case 0:
                        break;
                    // Si tiene ligadura y empieza la ligadura desde dicha nota hacia las siguientes.
                    case 1:
                        xml.write("        <notations>" + "\r\n"
                                + "        <tied type=\"start\"/>" + "\r\n"
                                + "          </notations>" + "\r\n"
                        );
                        break;
                    // Si tiene ligadura, y la anterior y la siguiente nota van ligadas a esta.
                    case 2:
                        xml.write("        <notations>" + "\r\n"
                                + "        <tied type=\"stop\"/>" + "\r\n"
                                + "        <tied type=\"start\"/>" + "\r\n"
                                + "          </notations>" + "\r\n"
                        );
                        break;
                    // Si tiene ligadura y la anterior nota está ligada a esta, pero no la siguiente.
                    case 3:
                        xml.write("        <notations>" + "\r\n"
                                + "        <tied type=\"stop\"/>" + "\r\n"
                                + "          </notations>" + "\r\n"
                        );
                        break;
                    // Si hay algún error.
                    default:
                        System.out.println("Error 2 in tie");
                        break;
                }

                // Cierra la nota actual.
                xml.write("        </note>" + "\r\n"
                );
            }

            // Se guarda lo escrito (silencio o nota).
            xml.close();
        } // Si existe un problema al escribir entra aquí
        catch (Exception e) {
            System.out.println("Error inserting note to xml");
        }
    }

    /**
     * FIXME: Description of {@code printMusicSymbol}. Imprime el simbolo musical en pantalla.
     */
    @Override
    public void printMusicSymbol() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();         // Crea objeto de tipo ByteArrayOutputStream.
        PrintStream printer = new PrintStream(os);                      // Crea objeto de tipo PrintStream.
        String output;                                                  // Crea String.

        printer.printf("#%3d (next #%3d) (prev #%3d)  %2s  %2s %2s %3dbt %dtie %ddot %5b stc",
                this.getNumber(), this.getNext().getNumber(), this.getPrev().getNumber(),
                this.getStep(), this.getAlter(), this.getOctave(), this.getBeats(),
                this.getTie(), this.getDot(), this.isStaccato());

        /*
         * -
         * printer.printf("sheet.addSymbol(new Note(\"%2s\", \"%s\", \"%s\", %3d, %d, %d, %b),
         * sheet.getLast(), true);", this.getStep(), this.getAlter(), this.getOctave(),
         * this.getBeats(), this.getTie(), this.getDot(), this.getStaccato());
         */
        output = os.toString();                                         // Asigna a output el buffer os.
        Log.d("printList", output);                                     // Muestra en consola output.
    }
}

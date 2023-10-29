/*
 * @fileoverview    {Sound}
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
import java.io.PrintStream;
import lombok.Data;

/**
 * TODO: Definición de {@code Sound}.
 *
 * @author Dyson Parra
 * @since 11
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class Sound extends MusicSymbol {

    private double frequency;                               // Frecuencia (en hertz) del sonido.
    private double duration;                                // Duración (en segundos) del sonido.
    private double intensity;                               // Intensidad (volumen) del sonido.

    /**
     * TODO: Definición de {@code Sound}.
     *
     */
    public Sound() {
        this.frequency = 0.0f;                               // Inicializa frecuencia en 0.0.
        this.duration = 0.0f;                                // Inicializa duración en 0.0.
        this.intensity = 0.0f;                               // Inicializa intensidad en 0.0.
    }

    /**
     * TODO: Definición de {@code Sound}.
     *
     * @param frequency
     * @param intensity
     * @param duration
     */
    public Sound(double frequency, double duration, double intensity) {
        this.frequency = frequency;                         // Asigna valor a frecuencia.
        this.duration = duration;                           // Asigna valor a duración.
        this.intensity = intensity;                         // Asigna valor a intensidad.
    }

    /**
     * FIXME: Definición de {@code addToXmlFile}. Agrega un sonido a la partitura (una partitura no
     * admite sonidos).
     *
     * @param source    Es la ruta del archivo de partitura.
     * @param divisions Indica las divisiones de la partitura.
     */
    @Override
    public void addToXmlFile(String source, int divisions) {
    }

    /**
     * FIXME: Definición de {@code printMusicSymbol}. Imprime el simbolo musical en pantalla.
     */
    @Override
    public void printMusicSymbol() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();         // Crea objeto de tipo ByteArrayOutputStream.
        PrintStream printer = new PrintStream(os);                      // Crea objeto de tipo PrintStream.
        String output;                                                  // Crea String.

        printer.printf("#%3d (next #%3d) (prev #%3d)  %8.3fHz  %20.17f sec  %5.2f db",
                this.getNumber(), this.getNext().getNumber(), this.getPrev().getNumber(),
                this.getFrequency(), this.getDuration(), this.getIntensity());

        /*
         * -
         * printer.printf("sheet.addSymbol(new Sound(%8.3f, %20.17f, %5.2f), sheet.getLast(),
         * true);", this.getFrequency(), this.getDuration(), this.getIntensity());
         */
        output = os.toString();                                         // Asigna a output el buffer os.
        Log.d("printList", output);                                     // Muestra en consola output.
    }
}

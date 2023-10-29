/*
 * @fileoverview    {AudioProcessor}
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
package com.project.dev.soundtomusicsheet.processor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import lombok.Data;

/**
 * TODO: Definición de {@code AudioProcessor}.
 *
 * @author Dyson Parra
 * @since 11
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class AudioProcessor implements Runnable {

    /**
     * FIXME: Definición de {@code SamplesListener}. Contiene parte del código que se ejecuta en el
     * runnable.
     */
    public interface SamplesListener {

        /**
         *
         * @param freq
         * @param duration
         * @param avgIntensity
         */
        void soundDetected(float freq, float duration, float avgIntensity);
    }

    private SamplesListener samplesListener;                        // Usada para obtener muestras que dirá parte del código a ejecutar en el runnable.
    private AudioRecord audioRecord;                                // Usada para de amplitud del micrófono (grabar).
    private boolean stopRunnable;                                   // Indica al Runnalble si debe detenerse.
    private float minFrequency;                                     // Mínima frecuencia para que una nota no sea ignorada (A♭0 = 25,956).
    private float maxFrequency;                                     // Máxima frecuencia para que una nota no sea ignorada (C#8 = 4434.921).

    /**
     * TODO: Definición de {@code AudioProcessor}.
     *
     */
    public AudioProcessor() {
        this.minFrequency = 26;
        this.maxFrequency = 4436;
    }

    /**
     * TODO: Definición de {@code AudioProcessor}.
     *
     * @param A4Affination
     */
    public AudioProcessor(float A4Affination) {
        this.minFrequency = A4Affination / 16.0f / 1.059463f;
        this.maxFrequency = A4Affination * 8 * 1.059463f * 1.059463f * 1.059463f * 1.059463f;
    }

    /*
     * Crea e inicializa objetos para imprimir en el Log.
     */
    /**
     * FIXME: Definición de {@code init}. Inicializa el grabador de audio.
     */
    public void init() {
        Log.d("printList", String.valueOf("Pasa 1 init"));

        int[] SAMPLE_RATES = {44100, 22050, 16000, 11025, 8000};                                    // Frecuencias de muestreo posibles para grabar.
        int bufSize = 16384;                                                                        // Tamaño mínimo (tentativo) del buffer de audio.
        int i = 0;                                                                                  // Número de frecuencia de muestreo actual.

        // Recorre la matriz con las frecuencias de muestreo.
        do {
            int sampleRate = SAMPLE_RATES[i];                                                       //A sampleRate le lleva la frecuencia de muestreo actual en la matriz.
            int minBufSize = AudioRecord.getMinBufferSize(sampleRate,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);                   // A minBufSize le lleva el tamaño minimo del buffer con la coonfiguración actual.

            if (minBufSize != AudioRecord.ERROR_BAD_VALUE && minBufSize != AudioRecord.ERROR) {     // Si con el tamaño actual de bufer no se produce un error al intentar grabar.
                audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                        AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                        Math.max(bufSize, minBufSize * 4));                                         // Inicializa el grabador de audio.

                //Log.d("printList", "Buffer: " + String.valueOf( minBufSize * 4));
            }

            i++;                                                                                    // Pasa a la siguiente posición del array de frecuencias de muestreo.
        } // Si el grabador de audio se creó exitosamente no intenta con las demás frecuencias de muestreo.
        while (i < SAMPLE_RATES.length && (audioRecord == null || audioRecord.getState() != AudioRecord.STATE_INITIALIZED));
    }

    /**
     * FIXME: Definición de {@code run}. Invocado cuando comienza a grabar audio.
     */
    @Override
    public void run() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();                                 // Crea objeto de tipo ByteArrayOutputStream.
        PrintStream printer = new PrintStream(os);                                              // Crea objeto de tipo PrintStream
        String output;                                                                          // Crea String.

        Log.d("printList", String.valueOf("Pasa 2 run"));

        audioRecord.startRecording();                                                               // Inicia la grabación de audio.
        int bufSize = 8192;                                                                         // Cuantas muestras se leerán simultaneamente.
        final int sampleRate = audioRecord.getSampleRate();                                         // Frecuencia de muestreo del grabador de audio.
        final short[] buffer = new short[bufSize];                                                  // Muestras leídas.
        float freq;                                                                                 // Cada frecuencia obtenida.
        double intensity;                                                                           // Amplitud promedio de las muestras leídas (0 a 32768)
        float avgIntensity;                                                                         // Porcentaje del valor de intensity (0 a 100).
        float duration;                                                                             // Duración en segundos de la cantidad de muestras leídas.
        int zeroCrossing;                                                                           // Cantidad de frames con cambios de signo.

        // Mientras no se haya indcado detener la grabación.
        do {
            final int read = audioRecord.read(buffer, (0), bufSize);                                // Lee la cantidad de muestras indicadas por bufSize.

            if (read > 0) {                                                                         // Si se pudieron leer las muestras.
                intensity = getAverageIntensity(buffer, read);                                      // Almacena la amplitud promedio de las muestras.
                zeroCrossing = getZeroCrossingCount(buffer);                                        // Almacena la cantidad de cambios de signo.
                int maxZeroCrossing = (int) (500 * (read / bufSize) * (sampleRate / 44100.0));      // Almacena el mayor zeroCrossing admitido.

                freq = getPitch(buffer, (read / 4), read, sampleRate, minFrequency, maxFrequency);  // Obtiene la frecuencia actual.
                avgIntensity = (float) ((intensity * 100.0) / 32768.0);                             // Obtiene el porcentaje de intensidad.
                duration = (bufSize / (float) sampleRate);                                          // Obtiene la duración en segundos de las muestras.

                samplesListener.soundDetected(freq, duration, avgIntensity);                        // Ejecuta acción para un sonido detectado.

                // Imprime la frecuencia e intensidad.
                printer.printf("%3d %4d %8.3f %8.3f", maxZeroCrossing, zeroCrossing, intensity, freq);
                output = os.toString();                         // Asigna a output el buffer os.
                Log.d("printList", output);                // Muestra en consola output.
                os = new ByteArrayOutputStream();               // Asigna un nuevo buffer a os.
                printer = new PrintStream(os);                  // Realaciona printer con os.
            }
        } while (!stopRunnable);
    }

    /**
     * FIXME: Definición de {@code stop}. Indica al Runnable (run) que deje de obtener muestras de
     * amplitud (grabar).
     */
    public void stop() {
        Log.d("printList", String.valueOf("Pasa 3 stop"));

        this.stopRunnable = true;                                           // Indica que el Runnable debe detenerse.
        this.audioRecord.stop();                                            // Detiene el grabador de audio.
        this.audioRecord.release();                                         // Reinicia el grabador de audio.
    }

    /**
     * FIXME: Definición de {@code getAverageIntensity}. Obtiene la amplitud promedio de las
     * muestras almacenadas en un array.
     *
     * @param data   Es la matriz con las muestras.
     * @param frames Es la cantidad de muestras.
     * @return
     */
    private double getAverageIntensity(short[] data, int frames) {
        double sum = 0;                                                     // Suma del valor de todas las muestras.
        for (int i = 0; i < frames; i++)                                    // Recorre la matriz con las muestras.
            sum += Math.abs(data[i]);                                       // Almacena el valor absoluto de la muestra actual.

        return sum / frames;                                                // Devuelve el valor de sum dividido entre la cantidad de muestras.
    }

    /**
     * FIXME: Definición de {@code getZeroCrossingCount}. Obtiene la cantidad de saltos entre
     * positivo y negativo en las muestras leídas.
     *
     * @param data Es la matriz con las muestras.
     * @return
     */
    private int getZeroCrossingCount(short[] data) {
        int len = data.length;                                              // Cantidad de muestras.
        int count = 0;                                                      // Número de cambios entre positivo y negativo.
        boolean prevValPositive;                                            // Si la muestra anterior tiene un valor positivo.
        boolean positive;                                                   // Si la muestra actual tiene un valor positivo.

        prevValPositive = data[0] >= 0;                                     // A prevValPositive le lleva si la primera muestra tiene un valor positivo.

        for (int i = 1; i < len; i++) {                                     // Recorre la matriz con las muestras desde la segunda posición.
            positive = data[i] >= 0;                                        // A positive le lleva si la muestra catual es positiva.
            if (prevValPositive == !positive)                               // Si la muestra anterior tiene un signo diferente a la actual.
                count++;                                                    // Aumenta count.
            prevValPositive = positive;                                     // A la prevValPositive le lleva positive.
        }

        return count;                                                       // Devuelve la cantiad de cambios entre positivo y negativo.
    }

    /**
     * FIXME: Definición de {@code getPitch}. Obtiene la frecuencia (en Hertz) de las muestras
     * leídas.
     *
     * @param data       Es la matriz con las muestras.
     * @param windowSize Es el tamaño de la ventana.
     * @param frames     Es la cantidad de muestras.
     * @param sampleRate Es la frecuencia de muestreo del buffer.
     * @param minFreq    Es el valor mínimo de frecuencia para que no sea ignorada.
     * @param maxFreq    Es el valor máximo de frecuencia para que no sea ignorada.
     * @return
     */
    private float getPitch(short[] data, int windowSize, int frames, float sampleRate, float minFreq, float maxFreq) {
        float maxOffset = sampleRate / minFreq;             //882
        float minOffset = sampleRate / maxFreq;             //88

        int minSum = Integer.MAX_VALUE;                     //2.147.483.647
        int minSumLag = 0;
        int[] sums = new int[Math.round(maxOffset) + 2];    //884 positions

        for (int lag = (int) minOffset; lag <= maxOffset; lag++) {
            int sum = 0;
            for (int i = 0; i < windowSize; i++) {
                int oldIndex = i - lag;
                int sample = ((oldIndex < 0) ? data[frames + oldIndex] : data[oldIndex]);
                sum += Math.abs(sample - data[i]);
            }

            sums[lag] = sum;

            if (sum < minSum) {
                minSum = sum;
                minSumLag = lag;
            }
        }

        // quadratic interpolation
        float delta = (sums[minSumLag + 1] - sums[minSumLag - 1]) / ((float) (2 * (2 * sums[minSumLag] - sums[minSumLag + 1] - sums[minSumLag - 1])));

        return sampleRate / (minSumLag + delta);
    }
}

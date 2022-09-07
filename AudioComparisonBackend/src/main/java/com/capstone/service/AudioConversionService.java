package com.capstone.service;

import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.tagtraum.jipes.math.FFTFactory;
import com.tagtraum.jipes.math.Transform;



public class AudioConversionService{
	private File wavFile;
	private double[] samples;
	private double[] frequency;
	private static final float NORMALIZATION_FACTOR_2_BYTES = Short.MAX_VALUE + 1.0f;
	private final int BUFFER_SIZE = 4096;
	private final int MAX_16_BIT = 32768;
	
	// Function that gets Amplitude data from Wavfile
	private double[] getAmplitude(File file) throws IOException, UnsupportedAudioFileException
	{
		System.out.println("Calculating WAV amplitudes");
		
		try (AudioInputStream input = AudioSystem.getAudioInputStream(file))
		{
			AudioFormat baseFormat = input.getFormat();
			System.out.println(baseFormat);
			Encoding encoding = AudioFormat.Encoding.PCM_UNSIGNED;
			float sampleRate = baseFormat.getSampleRate();
			int numChannels = baseFormat.getChannels();
			
			AudioFormat decodedFormat = new AudioFormat(encoding, sampleRate, 16, numChannels, numChannels * 2, sampleRate, false);
			
			try (AudioInputStream pcmDecodedInput = AudioSystem.getAudioInputStream(decodedFormat, input))
			{
				int count = 0;
				System.out.println(pcmDecodedInput.getFormat());
				byte[] buffer = new byte[BUFFER_SIZE];
				
				Queue<Double> queue = new PriorityQueue<>();
				while ((count = pcmDecodedInput.read(buffer, 0, BUFFER_SIZE)) != -1)
				{
					for(int i = 0; i < count / 2; i++)
					{
						double sample = ((short) (((buffer[2*i+1] & 0xFF) << 8) | (buffer[2*i] & 0xFF))) / ((double) MAX_16_BIT);
						queue.add(sample);
					}
				}
				
				double[] finalAmplitudes = queue.stream().mapToDouble(Double::doubleValue).toArray();
				pcmDecodedInput.close();
				return finalAmplitudes;
			}
		}
	}
	
	// Function that gets Frequency from wave file
	private double[] calculateFrequency(File file) throws IOException, UnsupportedAudioFileException
	{
		try (AudioInputStream input = AudioSystem.getAudioInputStream(file))
		{
			final byte[] buf = new byte[2048];
			final int numberOfSamples = buf.length / input.getFormat().getFrameSize();
			System.out.println(numberOfSamples);
			System.out.println(input.getFormat().getFrameSize());
			final Transform fft = FFTFactory.getInstance().create(numberOfSamples);
			Queue<Double> queue = new PriorityQueue<>();
			while(input.read(buf) != -1);
			{
				System.out.println(input.available());
				final float[] samples = decode(buf, input.getFormat());
				final float[][] transformed = fft.transform(samples);
            	final float[] realPart = transformed[0];
            	final float[] imaginaryPart = transformed[1];
            	final double[] magnitudes = toMagnitudes(realPart, imaginaryPart);
            	for (int i = 0; i < magnitudes.length; i++)
            	{
            		queue.add(magnitudes[i]);
            	}
			}
			double[] finalAmplitudes = queue.stream().mapToDouble(Double::doubleValue).toArray();
			input.close();
			return finalAmplitudes;
		}
		
	}
	
    private static float[] decode(final byte[] buf, final AudioFormat format) {
        final float[] fbuf = new float[buf.length / format.getFrameSize()];
        for (int pos = 0; pos < buf.length; pos += format.getFrameSize()) {
            final int sample = format.isBigEndian()
                    ? byteToIntBigEndian(buf, pos, format.getFrameSize())
                    : byteToIntLittleEndian(buf, pos, format.getFrameSize());
            // normalize to [0,1] (not strictly necessary, but makes things easier)
            fbuf[pos / format.getFrameSize()] = sample / NORMALIZATION_FACTOR_2_BYTES;
        }
        return fbuf;
    }

    private static double[] toMagnitudes(final float[] realPart, final float[] imaginaryPart) {
        final double[] powers = new double[realPart.length / 2];
        for (int i = 0; i < powers.length; i++) {
            powers[i] = Math.sqrt(realPart[i] * realPart[i] + imaginaryPart[i] * imaginaryPart[i]);
        }
        return powers;
    }

    private static int byteToIntLittleEndian(final byte[] buf, final int offset, final int bytesPerSample) {
        int sample = 0;
        for (int byteIndex = 0; byteIndex < bytesPerSample; byteIndex++) {
            final int aByte = buf[offset + byteIndex] & 0xff;
            sample += aByte << 8 * (byteIndex);
        }
        return sample;
    }

    private static int byteToIntBigEndian(final byte[] buf, final int offset, final int bytesPerSample) {
        int sample = 0;
        for (int byteIndex = 0; byteIndex < bytesPerSample; byteIndex++) {
            final int aByte = buf[offset + byteIndex] & 0xff;
            sample += aByte << (8 * (bytesPerSample - byteIndex - 1));
        }
        return sample;
    }
    
	
	public void setSamples () throws IOException, UnsupportedAudioFileException 
	{
		this.samples = getAmplitude(getWaveFile());
	}
	
	public double[] getSamples()
	{
		return samples;
	}
	
	public void setFrequency() throws IOException, UnsupportedAudioFileException 
	{
		this.frequency = calculateFrequency(getWaveFile());
	}
	
	public double[] getFrequency() {
		return frequency;
	}
	
	public void setWaveFile(File wavFile)
	{
		this.wavFile = wavFile;
	}
	
	public File getWaveFile() 
	{
		return wavFile;
	}
	
	public void deleteWavFile()
	{
		wavFile.delete();
	}
	
	
}
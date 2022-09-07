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

import com.capstone.domain.RealDoubleFFT;

public class WaveFormService {
	
	private double[] wavAmplitudes;
	private double[] frequency;
 	private File file;
	
	private double[] getWavAmplitude(File file) throws UnsupportedAudioFileException, IOException {
		System.out.println("Calculating WAV amplitudes");
		
		//Get Audio input stream
		try (AudioInputStream input = AudioSystem.getAudioInputStream(file)) {
			AudioFormat baseFormat = input.getFormat();
			//Encoding
			Encoding encoding = AudioFormat.Encoding.PCM_UNSIGNED;
			float sampleRate = baseFormat.getSampleRate();
			int numChannels = baseFormat.getChannels();
			
			AudioFormat decodedFormat = new AudioFormat(encoding, sampleRate, 16, numChannels, numChannels * 2, sampleRate, false);
			
			//Get the PCM Decoded Audio Input Stream
			try (AudioInputStream pcmDecodedInput = AudioSystem.getAudioInputStream(decodedFormat, input)) {
				final int BUFFER_SIZE = 4096; // Size in Bytes
				final int MAX_16_BIT = 32768;
				int count = 0;
				//Create a buffer
				byte[] buffer = new byte[BUFFER_SIZE];
				
				//Now get the average to a smaller array
				Queue<Double> queue = new PriorityQueue<>();
				while ((count = pcmDecodedInput.read(buffer, 0, BUFFER_SIZE)) != -1) {
	                for (int i = 0; i < count/2; i++) {
	                    double sample = ((short) (((buffer[2*i+1] & 0xFF) << 8) | (buffer[2*i] & 0xFF))) / ((double) MAX_16_BIT);
	                    queue.add(sample);
	                }
				}
				
				double[] finalAmplitudes = queue.stream().mapToDouble(Double::doubleValue).toArray();
				pcmDecodedInput.close();
				return finalAmplitudes;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//Error
		return new double[1];
	}
	
	private double[] calculateFrequency(double[] pcm_samples)
	{
		RealDoubleFFT transformer;
	    int blockSize = 256;
		transformer = new RealDoubleFFT(blockSize);
		double[] toTransform = new double[blockSize];
		double[] result = new double[pcm_samples.length];
		int i = 0;
		while(i < pcm_samples.length)
		{
			for(int j = i; j < (i + blockSize) && j < pcm_samples.length ; j++)
				toTransform[j % blockSize] = pcm_samples[j];
			transformer.ft(toTransform);
			for(int j = i; j < (i + blockSize) && j < pcm_samples.length; j++)
				result[j] = toTransform[j % blockSize];
			i += blockSize;	
		}
		
		return result;
	}
	
	public void setWavAmplitudes () {
		try {
			this.wavAmplitudes = getWavAmplitude(this.file);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double[] getWavAmplitudesValues() {
		return wavAmplitudes;
	}
	
	public void setFrequency() {
		this.frequency = calculateFrequency(getWavAmplitudesValues());
	}
	
	public double[] getFrequency() {
		return frequency;
	}
	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file; 
	}
}
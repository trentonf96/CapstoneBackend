package com.capstone.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.service.WaveFormService;

import au.com.bytecode.opencsv.CSVWriter;

@RestController
public class WaveController{
	
	
	@Autowired
	WaveFormService waveFormService;
	/*
	 * Get file from database
	 */
	
	@GetMapping("/getAudioFileData")
	public File getAudioFileData() throws IOException
	{
		File file= new File("C:\\Users\\Admin\\Downloads\\CantinaBand3.wav");
		File outFile = new File("D://graphdata.csv");
		CSVWriter writer = new CSVWriter(new FileWriter(outFile), 
    			CSVWriter.DEFAULT_SEPARATOR, 
    			CSVWriter.NO_QUOTE_CHARACTER,
    			CSVWriter.DEFAULT_ESCAPE_CHARACTER,
    			CSVWriter.DEFAULT_LINE_END);
		double[] ampValues;
		double[] freqValues;
		waveFormService.setFile(file);
		waveFormService.setWavAmplitudes();
		waveFormService.setFrequency();
		
		ampValues = waveFormService.getWavAmplitudesValues();
		freqValues = waveFormService.getFrequency();
		
		String line[] = {"Count" , "Amplitude", "Frequency"};
        writer.writeNext(line);
        
		for(int i = 0; i < ampValues.length; i++)
		{
			line[0] = Integer.toString(i);
			line[1] = Double.toString(ampValues[i]);
			line[2] = Double.toString(freqValues[i]);
            writer.writeNext(line);
		}
		
		writer.flush();
		
		return outFile; 
	}
}
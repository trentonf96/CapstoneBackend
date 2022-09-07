package com.capstone.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.capstone.domain.Audio;
import com.capstone.domain.AudioDTO;
import com.capstone.domain.AudioRepository;
import com.capstone.domain.UserRepository;
import com.capstone.service.AudioConversionService;

import au.com.bytecode.opencsv.CSVWriter;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class AudioController{
	
	@Autowired
	AudioConversionService audioConversionService;
	
	@Autowired
	AudioRepository audioRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/saveAudio")
	@Transactional
	public void saveAudio(@RequestBody AudioDTO audioDTO) throws IOException, UnsupportedAudioFileException, InterruptedException
	{
		//Convert base64 to mp3
		File mp3 = convertEncodedString(audioDTO.base64_audio, audioDTO.file_name);
		// Pause to allow conversion
		Thread.sleep(3000);
		// Convert mp3 to Wav
		getWav(audioDTO.file_name);
		// Pause to allow conversion
		Thread.sleep(3000);
		
		// Get wave file from location
		File wav = new File("/home/bryan.aguiar/audio_file_location/" + audioDTO.file_name);
		
		// Store double arrays for amplitude and frequency
		double[] sampleValues;
		double[] frequencyValues;
		
		// Use audio conversion service to fill arrays
		audioConversionService.setWaveFile(wav);
		audioConversionService.setSamples();
		audioConversionService.setFrequency();
		
		sampleValues = audioConversionService.getSamples();
		frequencyValues = audioConversionService.getFrequency();
		System.out.println(sampleValues);
		System.out.println(frequencyValues);
		System.out.println("End of saveAudio");
		
		// Write audio conversions to csv file and store in users folder
		write(audioDTO.file_name, audioDTO.user_name, sampleValues, frequencyValues);
		Audio audio = new Audio();
		audio.setUser(userRepository.findByUsername(audioDTO.user_name));
		audio.setFile_name(audioDTO.file_name);
		audio.setCSV_location("/home/bryan.aguiar/csv_files/" + audioDTO.user_name + "/" + audioDTO.file_name.substring(0, audioDTO.file_name.length() - 4) + ".csv");
		audioRepository.save(audio);
		
		// Delete audio files to save memory
		mp3.delete();
		wav.delete();
	}
	
	@DeleteMapping("/deleteAudio")
	@Transactional
	public void dropAudioFile(@RequestParam String fileName, @RequestParam String username)
	{
		Audio audio= audioRepository.findByUsernameAndFilename(username, fileName);
		String filepath = "/home/bryan.aguiar/csv_files/" + username + "/" + fileName.substring(0, fileName.length() - 4) + ".csv";
		File delete = new File(filepath);
		if (audio != null && audio.getFile_name().equals(fileName))
		{
			audioRepository.delete(audio);
			delete.delete();		
		}
		else
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fileName + " or " + username + "is invalid.");
	}
	
	//Used to decode and generate the base 64 string
	private File convertEncodedString(String string, String fileName) throws IOException
	{
		byte[] decodedString = DatatypeConverter.parseBase64Binary(string);
		
		File fo = new File("/home/bryan.aguiar/audio_file_location/" + fileName.substring(0, fileName.length() - 4) + ".mp3");
		FileOutputStream fos = new FileOutputStream(fo);
		try {
			fos.write(decodedString);
		} finally {
			fos.close();
		}
		
		return fo;
	}
	
	public static void write (String filename, String user_name, double []x, double []y) throws IOException
	{
		String filepath = "/home/bryan.aguiar/csv_files/" + user_name + "/";
		File path = new File(filepath);
		path.mkdir();
		File outFile = new File(filepath + filename.substring(0, filename.length() - 4) + ".csv");
		CSVWriter writer = new CSVWriter(new FileWriter(outFile), 
    			CSVWriter.DEFAULT_SEPARATOR, 
    			CSVWriter.NO_QUOTE_CHARACTER,
    			CSVWriter.DEFAULT_ESCAPE_CHARACTER,
    			CSVWriter.DEFAULT_LINE_END);
		
		String line[] = {"Count" , "Amplitude", "Frequency"};
        writer.writeNext(line);
        for(int i = 0; i < x.length; i++)
		{
			line[0] = Integer.toString(i);
			line[1] = Double.toString(x[i]);
			if(i < y.length - 1)
				line[2] = Double.toString(y[i]);
			else
				line[2] = Double.toString(-1);
            writer.writeNext(line);
		}
		
		writer.flush();
		writer.close();
	}
	
	
	private void getWav(String fileName) throws IOException
	{
		String mp3 = fileName.substring(0, fileName.length() - 4) + ".mp3";
		String wav = fileName.substring(0, fileName.length() - 4) + ".wav";
		String[] cmd = new String[]{"conversion.py", "/home/bryan.aguiar/audio_file_location/",  mp3, wav};
		ProcessBuilder processBuilder = new ProcessBuilder("python3", "/home/bryan.aguiar/conversion.py", "/home/bryan.aguiar/audio_file_location/", mp3, wav );
		System.out.println(cmd);
		System.out.println("Generating wav");
		processBuilder.start();
		

	}
}
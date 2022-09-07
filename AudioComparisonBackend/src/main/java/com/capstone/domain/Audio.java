package com.capstone.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Audio{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int audio_id;
	private String base64_audio;
	private String file_name;
	private String csv_location;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName="id")
	private User user;
	
	
	public Audio() {
		super();
	}
	
	public int getAudio_id() {
		return audio_id;
	}
	
	public void setAudio_id(int audio_id)
	{
		this.audio_id = audio_id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getAudio() {
		return base64_audio;
	}
	
	public void setAudio(String audio)
	{
		this.base64_audio = audio;
	}
	
	public String getFile_name() {
		return file_name;
	}
	
	public void setFile_name(String file_name)
	{
		this.file_name = file_name;
	}
	
	public String getCSV_location() {
		return csv_location;
	}
	
	public void setCSV_location(String csv_location)
	{
		this.csv_location = csv_location;
	}
	
	@Override
	public String toString() {
		return "Audio [audio_id=" + audio_id + ", user=" + user.getName()+ ", base64_audio=" + base64_audio + ", file_name=" + file_name + ", csv_location="
				+ csv_location + "]";
	}
}
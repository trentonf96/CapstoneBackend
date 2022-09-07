package com.capstone.domain;

public class AudioDTO {
	public int audio_id;
	public String user_name;
	public String base64_audio;
	public String file_name;
	public String csv_location;
	
	
	public AudioDTO()
	{
		this.audio_id = 0;
		this.user_name = null;
		this.base64_audio = null;
		this.file_name = null;
		this.csv_location = null;
	}
	
	public AudioDTO(String file_name, String base64_audio)
	{
		this.audio_id = 0;
		this.user_name = null;
		this.base64_audio = base64_audio;
		this.file_name = file_name;
		this.csv_location = null;
	}
	
	public AudioDTO(String user_email, String base64_audio, String file_name)
	{
		this.audio_id = 0;
		this.user_name = user_email;
		this.base64_audio = base64_audio;
		this.file_name = file_name;
		this.csv_location = null;
	}
	
	public AudioDTO(String user_name, String base64_audio, String file_name, String csv_location)
	{
		this.audio_id = 0;
		this.user_name = user_name;
		this.base64_audio = base64_audio;
		this.file_name = file_name;
		this.csv_location = csv_location;
	}
	
	@Override
	public String toString() {
		return "AudioDTO [audio_id=" + audio_id + ", user_name=" + user_name + ", base64_audio=" + base64_audio + ", file_name=" + file_name
				+ ", csv_location=" + csv_location + "]";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		AudioDTO other = (AudioDTO) obj;
		if (audio_id != other.audio_id)
			return false;
		if (user_name == null) {
			if (other.user_name != null)
				return false;
		} else if (!user_name.equals(other.user_name))
			return false;
		if (base64_audio == null) {
			if (other.base64_audio != null)
				return false;
		} else if (!base64_audio.equals(other.base64_audio))
			return false;
		if (file_name == null) {
			if (other.file_name != null)
				return false;
		} else if (!file_name.equals(other.file_name))
			return false;
		if (csv_location == null) {
			if (other.csv_location != null)
				return false;
		} else if (!csv_location.equals(other.csv_location))
			return false;
		return true;
	}
}
package com.capstone.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface AudioRepository extends CrudRepository <Audio, Integer> {
	
	@Query("select a from Audio a where a.user.username=:user_name and a.file_name=:file_name")
	Audio findByUsernameAndFilename(@Param("user_name") String user_name, @Param("file_name") String file_name);
	
	@Query("select a from Audio a where a.user.username=:user_name")
	Audio findByUsername(@Param("user_name") String user_name);
	
	@SuppressWarnings("unchecked")
	Audio save(Audio a);
}
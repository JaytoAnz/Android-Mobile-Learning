<?php
 	require_once('dbCon.php');

	if($_SERVER['REQUEST_METHOD']=='POST'){

		$file_name = $_FILES['video']['name'];
		$file_size = $_FILES['video']['size'];
		$file_type = $_FILES['video']['type'];
		$temp_name = $_FILES['video']['tmp_name'];
		
		$location = "directoryVideos/";
		
		$upload = move_uploaded_file($temp_name, $location.$file_name);	
		echo "http://192.168.43.44/training/directoryVideos/".$file_name;

		$con->close();

	}else{
		echo "Error";
	}
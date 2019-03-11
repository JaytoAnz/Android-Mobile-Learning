<?php

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		require_once('dbCon.php');
		
		$username = $_POST['username'];
		$email = $_POST['email'];
		$password = $_POST['password'];
		$level = $_POST['level'];
		$image = $_POST['image'];

		$sql ="SHOW TABLE STATUS LIKE 'tb_users'";
		
		$res = mysqli_query($con,$sql);
		
		$data = mysqli_fetch_assoc($res);
		$next_increment = $data['Auto_increment'];
		
		$path = "directoryPhoto/$next_increment.png";
		
		$actualpath = "http://192.168.43.44/training/$path";
		
		$sql = "INSERT INTO tb_users (id, username, email, password, level, foto) VALUES (default, '$username', '$email', '$password', '$level', '$actualpath')";
		
		if(mysqli_query($con,$sql)){
			file_put_contents($path,base64_decode($image));
			echo "Successfully Uploaded";
		}
		
		mysqli_close($con);
	}else{
		echo "Error";
	}

?>
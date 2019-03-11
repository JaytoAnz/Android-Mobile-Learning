<?php
	require_once('dbCon.php');

	$id = $_POST['id'];
	$username = $_POST['username'];
	$email = $_POST['email'];
	$password = $_POST['password'];
	$level = $_POST['level'];
	$image = $_POST['image'];

	$path = "directoryPhoto/$id.png";
	
	$actualpath = "http://192.168.43.44/training/$path";
		
	$sql= " UPDATE tb_users SET username = '$username', email = '$email', password = '$password', level = '$level', foto = '$actualpath' WHERE id = '$id' ";

	if(mysqli_query($con,$sql)){

		$sql = mysqli_query($con, "SELECT * FROM tb_users WHERE username = '$username'") or die ($con->error);
	    $data = mysqli_fetch_array($sql);
	    $foto = $data['foto'];
	 	
	 	$response["foto"]=$foto;
	 	echo json_encode($response);
		
		file_put_contents($path, base64_decode($image));
		echo  "Sukses";

	}else {
		
		echo "Failed";
	}

?>
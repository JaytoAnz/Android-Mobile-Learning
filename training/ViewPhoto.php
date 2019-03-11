<?php
	require_once('dbCon.php');
 
	$username  = $_POST['username'];

	$sql = mysqli_query($con, "SELECT * FROM tb_users WHERE username = '$username'") or die ($con->error);
    $data = mysqli_fetch_array($sql);
    $id = $data['id'];
    $username = $data['username'];
    $email = $data['email'];
    $password = $data['password'];
    $level = $data['level'];
    $foto = $data['foto'];
    $response = array();
	if($data  > 0){
	 	
	 	$response["id"]=$id;
	 	$response["username"]=$username;
	 	$response["email"]=$email;
	 	$response["password"]=$password;
	 	$response["level"]=$level;
	 	$response["foto"]=$foto;

	 	echo json_encode($response);
	}else {

		$response["error"]=true;

		echo json_encode($response);
	}
	mysqli_close($con);
?>
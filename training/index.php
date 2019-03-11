<?php
	require_once('dbCon.php');
 
	$username  = $_POST['username'];
	$password  = $_POST['password'];
	$level = $_POST['level'];
	$sql = mysqli_query($con, "SELECT * FROM tb_users WHERE username = '$username' AND password = '$password' AND level = '$level'") or die ($con->error);
    $data = mysqli_fetch_array($sql);
    $user = $data['username'];
    $pass = $data['password'];
    $email = $data['email'];
    $level = $data['level'];
    $foto = $data['foto'];    
    $response = array();
	if($data  > 0){
	 	
	 	$response["success"]=true;
	 	$response["message"]= 'Success';
	 	
	 	$response["name"]=$user;
	 	$response["pass"]=$pass;
	 	$response["email"]=$email;
	 	$response["level"]=$level;
	 	$response["foto"]=$foto;

	 	echo json_encode($response);
	}else {

		$response["success"]=false;
		$response["message"]= 'Account Not Valid';

		echo json_encode($response);
	}

		mysqli_close($con);
?>
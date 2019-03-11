<?php 
	
	require_once('dbCon.php');

	$level = $_GET['level'];

	$stmt = $con->prepare("SELECT * FROM `tb_users` WHERE level = '$level'");
 
	//executing the query 
	$stmt->execute();

	//binding results to the query 
	$stmt->bind_result($id, $username, $email, $password, $level, $foto);

	$items = array(); 

	//traversing through all the result 
	while($stmt->fetch()){
	$temp = array();
	$temp['id'] = $id; 
	$temp['username'] = $username;
	$temp['email'] = $email; 
	$temp['password'] = $password; 
	$temp['level'] = $level;
	$temp['foto'] = $foto;
	array_push($items, $temp);
	}

	//displaying the result in json format 
	echo json_encode($items);

	$con->close();
?>
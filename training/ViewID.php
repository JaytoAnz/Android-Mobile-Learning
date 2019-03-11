<?php
	require_once('dbCon.php');
 
	$nameUrl  = $_POST['nameUrl'];

	$sql = mysqli_query($con, "SELECT * FROM tb_materi WHERE nameUrl = '$nameUrl'") or die ($con->error);
    $data = mysqli_fetch_array($sql);
    $no_id = $data['no_id'];
    $response = array();
	if($data  > 0){
	 	
	 	$response["no_id"]=$no_id;

	 	echo json_encode($response);
	}else {

		$response["error"]=true;

		echo json_encode($response);
	}

		mysqli_close($con);
?>
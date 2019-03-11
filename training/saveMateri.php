<?php
	require_once('dbCon.php');

	$nameUrl = $_POST['nameUrl'];
	$video = $_POST['video'];


	$sql= "insert into tb_materi values(default, '".$nameUrl."', '".$video."', 'null', 'null', 'null', 'null', now())";
	
	if (mysqli_query($con,$sql)) {
		
		echo  "Sukses";
		
	} else {
		
		echo "Failed";
	}

?>
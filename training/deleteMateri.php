<?php
	require_once('dbCon.php');

	$nameUrl = $_POST['nameUrl'];

	$sql= " DELETE FROM tb_materi WHERE nameUrl = '$nameUrl' ";
	
	if (mysqli_query($con,$sql)) {
		
		echo  "Sudah Di Hapus ";
		
	} else {
		
		echo "Failed";
	}

?>
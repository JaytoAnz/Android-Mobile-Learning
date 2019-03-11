<?php
	require_once('dbCon.php');

	$linkNilai = $_POST['linkNilai'];
	$nameUrl = $_POST['nameUrl'];


	$sql= " UPDATE tb_materi SET linkNilai = '$linkNilai' WHERE nameUrl = '$nameUrl' ";
	
	if (mysqli_query($con,$sql)) {
		
		echo  "Sukses";
		
	} else {
		
		echo "Failed";
	}

?>
<?php
	require_once('dbCon.php');

	$nameUrl2 = $_POST['nameUrl2'];
	$video2 = $_POST['video2'];
	$nameUrl = $_POST['nameUrl'];
	$linkKuis = $_POST['linkKuis'];


	$sql= " UPDATE tb_materi SET nameUrl2 = '$nameUrl2', video2 = '$video2', linkKuis = '$linkKuis' WHERE nameUrl = '$nameUrl' ";
	
	if (mysqli_query($con,$sql)) {
		
		echo  "Sukses";
		
	} else {
		
		echo "Failed";
	}

?>
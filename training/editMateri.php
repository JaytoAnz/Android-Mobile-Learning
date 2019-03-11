<?php
	require_once('dbCon.php');

	$no_id = $_POST['no_id'];
	$nameUrl = $_POST['nameUrl'];
	$video = $_POST['video'];
	$nameUrl2 = $_POST['nameUrl2'];
	$video2 = $_POST['video2'];
	$linkKuis = $_POST['linkKuis'];
	$linkNilai = $_POST['linkNilai'];

	$sql= " UPDATE tb_materi SET nameUrl = '$nameUrl', video = '$video', nameUrl2 = '$nameUrl2', video2 = '$video2', linkKuis = '$linkKuis',linkNilai = '$linkNilai' WHERE no_id = '$no_id' ";
	
	if (mysqli_query($con,$sql)) {
		
		echo  "Sukses";
		
	} else {
		
		echo "Failed";
	}

?>
<?php
	
	require_once('dbCon.php');

	$username = $_POST['username'];

	$sql ="SELECT id FROM tb_users WHERE username = '$username'";
		
	$res = mysqli_query($con,$sql);
		
	$id = 0;
	
	while($row = mysqli_fetch_array($res)){
			$id = $row['id'];
	}

	$path ="directoryPhoto/$id.png";

	if(unlink($path)){ 
		
		$sql= " DELETE FROM tb_users WHERE id = '$id' ";

		if (mysqli_query($con,$sql)) {
			
			echo  "Berhasil Di Hapus";
			
		} else {
			
			echo "Gagal Dihapus";
		}
	}
?>
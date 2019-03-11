<?php 
	
	require_once('dbCon.php');

	$stmt = $con->prepare("SELECT * FROM tb_materi");
 
	//executing the query 
	$stmt->execute();

	//binding results to the query 
	$stmt->bind_result($no_id, $judul, $video, $judul2, $video2, $linkKuis, $linkNilai, $create_date);

	$items = array(); 

	//traversing through all the result 
	while($stmt->fetch()){
	$temp = array();
	$temp['no_id'] = $no_id; 
	$temp['judul'] = $judul; 
	$temp['video'] = $video; 
	$temp['judul2'] = $judul2; 
	$temp['video2'] = $video2;
	$temp['linkKuis'] = $linkKuis;
	$temp['linkNilai'] = $linkNilai;
	$temp['create_date'] = $create_date;
	array_push($items, $temp);
	}

	//displaying the result in json format 
	echo json_encode($items);

	$con->close();
?>
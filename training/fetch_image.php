<?php
 
if($_SERVER['REQUEST_METHOD']=='POST'){
		
		
                $name = $_POST['name'];
$response = array();
	define('DB_USERNAME', 'root');
define('DB_PASSWORD', '');
define('DB_HOST', 'localhost');
define('DB_NAME', 'imageupload');
		
		$conn = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
 
// check for post data

$sql = "SELECT * FROM images WHERE name = '$name'";
$result = $conn->query($sql) or die (mysqli_connect_error());

  
 
    if (!empty($result)) {
        // check for empty result
        if (mysqli_num_rows($result) > 0) {
 
            $result = mysqli_fetch_array($result);
 
            $data = array();
            $data["id"] = $result["id"];
            $data["name"] = $result["name"];
            $data["photo"] = $result["photo"];
           
           
            // success
            $response["success"] = 1;
			$response["image"] = array();
 
            array_push($response["image"], $data);
 
            
            echo json_encode($response);
        } else {
           
            $response["success"] = 0;
            $response["message"] = "No data found";
 
            echo json_encode($response);
        }
    } else {
       
        $response["success"] = 0;
        $response["message"] = "No data found";
 
        // echo no users JSON
        echo json_encode($response);
    }
 }else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>
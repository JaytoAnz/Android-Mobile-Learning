<?php
    require_once('dbCon.php');

    $username = $_POST['username'];
    $password = $_POST['password'];
    $level = $_POST['level'];

    if($level == 'dosen'){
        $sql = mysqli_query($con, "SELECT * FROM tb_users WHERE username = '$username' AND password = '$password' AND level = '$level' ") or die ($con->error);
        $data = mysqli_fetch_array($sql);
        $user = $data['username'];
        $pass = $data['password'];
        $email = $data['email'];
        $cek = mysqli_num_rows($sql);
        $response = array();
        $response['user_type']=$level;
        echo json_encode($response);
        if ($cek > 0) {
            $response["success"]=true;
            $response["name"]=$user;
            $response["pass"]=$pass;
            $response["email"]=$email;
            echo json_encode($response);
        }else {
            $response['Error']=true;
            echo json_encode($response);
        }
    }else if($level == 'mahasiswa'){
        $sql = mysqli_query($con, "SELECT * FROM tb_users WHERE username = '$username' AND password = '$password' AND level = '$level' ") or die ($con->error);
        $data = mysqli_fetch_array($sql);
        $user = $data['username'];
        $pass = $data['password'];
        $email = $data['email'];
        $cek = mysqli_num_rows($sql);
        $response = array();
        $response['user_type']=$level;
        echo json_encode($response);
        if ($cek > 0) {
            $response["success"]=true;
            $response["name"]=$user;
            $response["pass"]=$pass;
            $response["email"]=$email;
            echo json_encode($response);
        }else {
            $response['Error']=true;
            echo json_encode($response);
        }
    }
?>
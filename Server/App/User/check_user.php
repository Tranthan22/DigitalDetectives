<?php
include_once('config.php');
if ($_SERVER['REQUEST_METHOD'] == 'POST'){
    $user_name = $_POST['user_namel'];
    $password = $_POST['passwordl'];

    $checkQuery = "SELECT * FROM Users WHERE user_name = '$user_name'";
    $checkResult = mysqli_query($con, $checkQuery);

    if (mysqli_num_rows($checkResult) > 0) {

        while ($row = mysqli_fetch_assoc($checkResult) ) {
            if(password_verify($password, $row['password'])){
                $response["success"] = true;
                $response["message"] = "Username and password match";
                echo json_encode($response);
                mysqli_close($con);
                exit(); // Thêm exit() để ngừng việc thực thi tiếp
            }
            else{
                // Sai mật khẩu
                $response["success"] = false;
                $response["message"] = "Incorrect password";
                echo json_encode($response);
                mysqli_close($con);
                exit(); // Thêm exit() để ngừng việc thực thi tiếp
            }
        }
    }
    else {
        // Sai tên đăng nhập
        $response["success"] = false;
        $response["message"] = "Incorrect username";
        echo json_encode($response);
        mysqli_close($con);
    }
}
?>

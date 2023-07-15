<?php
include_once('config.php');

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $user_name = $_POST['user_namet'];
    $password = $_POST['passwordt'];
    $email = $_POST['emailt'];
    $phonenumber = $_POST['phonenumbert'];

    // Check if username already exists in the Users table
    $checkQuery = "SELECT * FROM Users WHERE user_name = '$user_name'";
    $checkResult = mysqli_query($con, $checkQuery);

    if (mysqli_num_rows($checkResult) > 0) {
        // Username already exists in the table
        $response["success"] = false;
        $response["message"] = "Username already exists";
        echo json_encode($response);
        mysqli_close($con);
        exit;
    }
    $password = password_hash($password, PASSWORD_DEFAULT);

    // Insert the post into the Users table
    $insertQuery = "INSERT INTO Users (user_name, password, email, phone_number) VALUES ('$user_name', '$password', '$email', '$phonenumber')";
    $insertResult = mysqli_query($con, $insertQuery);

    if (!$insertResult) {
        $response["success"] = false;
        $response["message"] = "Error inserting data: " . mysqli_error($con);
        echo json_encode($response);
        mysqli_close($con);
    } else {
        $response["success"] = true;
        $response["message"] = "Data inserted successfully";
        echo json_encode($response);
        mysqli_close($con);
    }
}
?>
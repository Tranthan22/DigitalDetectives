<?php
include_once('config.php');

$response = array(); // Initialize the response array

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $user_name = $_POST['user_namet'];
    $password = $_POST['passwordt'];
    $email = $_POST['emailt'];
    $phonenumber = $_POST['phonenumbert'];

    // Input validation and sanitation
    $user_name = mysqli_real_escape_string($con, $user_name);
    $email = mysqli_real_escape_string($con, $email);
    // ... sanitize other inputs ...

    // Check if username already exists in the Users table
    $checkQuery = "SELECT * FROM Users WHERE user_name = '$user_name'";
    $checkResult = mysqli_query($con, $checkQuery);

    if (mysqli_num_rows($checkResult) > 0) {
        $response["success"] = false;
        $response["message"] = "Username already exists";
        echo json_encode($response);
        mysqli_close($con);
        exit;
    }

    $password = password_hash($password, PASSWORD_DEFAULT);

    // Insert user data into the Users table
    $insertQuery = "INSERT INTO Users (user_name, password, email, phone_number) VALUES ('$user_name', '$password', '$email', '$phonenumber')";
    $insertResult = mysqli_query($con, $insertQuery);

    if (!$insertResult) {
        $response["success"] = false;
        $response["message"] = "Error inserting data: " . mysqli_error($con);
    } else {
        $response["success"] = true;
        $response["message"] = "Data inserted successfully";

        // Table name creation
        $newTableName = mysqli_real_escape_string($con, $user_name) . '_record';

        // Create table query
        $createQuery = "CREATE TABLE IF NOT EXISTS $newTableName (
            `garden` varchar(255) NOT NULL,
            `node_air` float(3,1) NOT NULL,
            `node_soil` float(3,1) NOT NULL,
            `node_temp` float(3,1) NOT NULL,
            `battery` int(3) NOT NULL,
            `time` time NOT NULL,
            `date` date NOT NULL
        )";

        if (mysqli_query($con, $createQuery)) {
            $response["table_creation"] = "Table '$newTableName' created successfully";
        } else {
            $response["table_creation"] = "Error creating table: " . mysqli_error($con);
        }
    }

    echo json_encode($response);
    mysqli_close($con);
    exit; // Exit after sending response
}
?>

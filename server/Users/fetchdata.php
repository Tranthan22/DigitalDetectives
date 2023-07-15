<?php
    include 'database.php';
     
    $id = '0';
    $pdo = Database::connect();

    $sql = 'SELECT * FROM Users WHERE id="' . $id . '"';
    $result = array("username" => array());

    foreach ($pdo->query($sql) as $row) {
        $hero = array();
        $hero['user_name'] = $row['user_name'];
        $hero['password'] = $row['password'];
        $hero['email'] = $row['email'];
        $hero['phone_number'] = $row['phone_number'];

        array_push($result["username"], $hero);
    }

    echo json_encode($result);

    Database::disconnect();
?>
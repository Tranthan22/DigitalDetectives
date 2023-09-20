<?php
require '../../database.php';

if (!empty($_POST)) {
    $user = $_POST['user'];
    $device = $_POST['device'] . "" . '23';
    $type = $_POST['type'];
    $link = '0'; 

    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Check exist
    $sql_check = "SELECT * FROM device_list WHERE user = ? AND device = ?";
    $q_check = $pdo->prepare($sql_check);
    $q_check->execute(array($user, $device));

    if ($q_check->rowCount() > 0) {
        // change link to 0 if exist
        $sql_update = "UPDATE device_list SET link = ? WHERE user = ? AND device = ?";
        $q_update = $pdo->prepare($sql_update);
        $q_update->execute(array($link, $user, $device));
        echo("successfully (updated)!");
    } else {
        // insert new device
        $sql_insert = "INSERT INTO device_list (user, device, type, link) VALUES (?, ?, ?, ?)";
        $q_insert = $pdo->prepare($sql_insert);
        $q_insert->execute(array($user, $device, $type, $link));
        echo("successfully (inserted)!");
    }

    Database::disconnect();
} else {
    echo("empty_post");
}
?>

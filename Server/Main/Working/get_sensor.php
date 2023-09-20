<?php
include '../../database.php'; // declare subprogram access to the database

if (!empty($_GET)) {
    $user = $_GET['user'];
    $sensor_id = $_GET['sensor_id'];
    $sensor = (object) array();

    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $sql = 'SELECT * FROM garden WHERE user = :user AND sensor_id = :sensor_id';
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(':user', $user);
    $stmt->bindParam(':sensor_id', $sensor_id);
    $stmt->execute();

    $row = $stmt->fetch(PDO::FETCH_ASSOC);
    if ($row) {
        $sensor->airWarning = $row['airWarning'];
        $sensor->soilWarning = $row['soilWarning'];
        $sensor->tempWarning = $row['tempWarning'];
        $sensor->batteryWarning = $row['batteryWarning'];

        $sql = 'SELECT * FROM device_list WHERE user = :user AND device = :sensor_id';
        $stmt = $pdo->prepare($sql);
        $stmt->bindParam(':user', $user);
        $stmt->bindParam(':sensor_id', $sensor_id);
        $stmt->execute();

        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($row) {
            $sensor->linked = $row['link'];
        }
    }

    Database::disconnect();
    echo (json_encode($sensor));
} else {
    echo ("empty_get");
}
?>

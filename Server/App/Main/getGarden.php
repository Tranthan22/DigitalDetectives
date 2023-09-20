<?php

    include '../../database.php'; // Declare subprogram access to database
    if ($_SERVER['REQUEST_METHOD'] == 'POST'){
        $user = $_POST['user'];
        $garden = $_POST['garden'];
        
        $pdo = Database::connect();
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        $sql = "SELECT * FROM garden WHERE user=:user AND garden_id=:garden";
        $stmt = $pdo->prepare($sql);

        $stmt->bindParam(':user', $user);
        $stmt->bindParam(':garden', $garden);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($row !== false) {
            $node[] = $row['node_auto'];
            $node[]= $row['node_air'];
            $node[] = $row['node_soil'];
            $node[]= $row['node_temp'];
            $node[] = $row['pump_1'];
            $node[] = $row['pump_2'];
            $node[] = $row['pump_3'];
            $node[] = $row['battery'];

            
            $node[]= $row['time'];
            $node[] = $row['date'];
            $node[] = $row['airWarning'];
            $node[] = $row['soilWarning'];
            $node[] = $row['batteryWarning'];
            $node[] = $row['pumpTime'];
            $node[] = $row['dailyPump'];
            
            $node[] = $row['sensor_id'];
            $node[] = $row['motor_id'];
            $node[] = $row['mode'];
            $node[] = $row['tempWarning'];

            $response = array('garden' => $node);
            echo json_encode($response);
        } else {
            echo "No data found for user: $user and node_id: $node_id";
        }
        Database::disconnect();
    }
?>
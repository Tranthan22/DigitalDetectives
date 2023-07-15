<?php

    include 'database.php'; // Declare subprogram access to database
    if ($_SERVER['REQUEST_METHOD'] == 'POST'){
        $user = $_POST['user_nameg'];
        $node_id = $_POST['node_idg'];

        $node = array();

        $pdo = Database::connect();
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        $sql = "SELECT * FROM node_status WHERE user=:user AND node_id=:node_id";
        $stmt = $pdo->prepare($sql);

        $stmt->bindParam(':user', $user);
        $stmt->bindParam(':node_id', $node_id);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($row !== false) {
       
            $node[] = $row['node_auto'];
            $node[]= $row['node_air'];
            $node[] = $row['node_soil'];
            $node[]= $row['node_PH'];
            $node[] = $row['bump_1'];
            $node[] = $row['bump_2'];
            $node[] = $row['bump_3'];
            $node[] = $row['battery'];

            $node[] = $row['date'];
            $node[]= $row['time'];
            $node[] = $row['bumpTime'];

            $response = array('garden' => $node);
            echo json_encode($response);
        } else {
            echo "No data found for user: $user and node_id: $node_id";
        }
        Database::disconnect();
    }
?>
<?php

require 'database.php';

if (!empty($_POST)) {
    // Retrieve values from $_POST array and assign them to variables
    $user = $_POST['user'];
    $node_id = $_POST['node_id'];
    $node_auto = $_POST['node_auto'];
    $node_air = $_POST['node_air'];
    $node_soil = $_POST['node_soil'];
    $node_PH = $_POST['node_PH'];
    $bump_1 = $_POST['bump_1'];
    $bump_2 = $_POST['bump_2'];
    $bump_3 = $_POST['bump_3'];
    $battery = $_POST['battery'];

    $table_name = $user . '_record';

    $serverKey = "AAAA1zpcHWA:APA91bFHcPvYrrbfUL_dRZLrdBT90OTZz-X8YBAZtYjmg0F-6NI-awnOMcq4FoO1JY_cJzD1H6UPBORaLsSNiJ0yzi9H9xHY9bWxRH1dGjaV5vr_Y5hswjQdWa_3Njf5vkPtLRFxL04I";

    $deviceToken = "fUAhyYDjRY2K10bAxHSUpo:APA91bHJl3EtqAXW9dGysaPHkq4hCphm3GP6Frn4FdPafH7wP5CpOc88aLrxk629eJEiXIiGzRYWk1s_dAMFiLzTilbuzOa_zT8pf3BgqiaNR6cRywCvJoOIK-e8DZtpaOHZlIdhuP7_";


    // Get the current time and date
    date_default_timezone_set("Asia/Jakarta");
    $tm = date("H:i:s");
    $dt = date("Y-m-d");

    if ($node_auto == 'false') {
        // Update node_status table with the received values
        $pdo = Database::connect();
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $sql = "UPDATE node_status SET node_air = ?, node_soil = ?, node_PH = ?, bump_1 = ?, bump_2 = ?, bump_3 = ?, battery = ?, time = ?, date = ? WHERE node_id = ? and user = ?";
        $q = $pdo->prepare($sql);
        $q->execute(array($node_air, $node_soil, $node_PH, $bump_1, $bump_2, $bump_3, $battery, $tm, $dt, $node_id, $user));

        Database::disconnect();

        // Insert values into node_record table
        $pdo = Database::connect();
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $sql = "INSERT INTO $table_name (node_id,node_auto,node_air,node_soil,node_PH,bump_1,bump_2,bump_3,battery,time,date) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        $q = $pdo->prepare($sql);
        $q->execute(array($node_id, 'no change', $node_air, $node_soil, $node_PH, $bump_1, $bump_2, $bump_3, $battery, $tm, $dt));
        Database::disconnect();

        // Check if any of the values are below certain thresholds and send a notification
        if ($node_air < 50 || $node_soil < 20 || $battery < 20) {
            // Construct notification message
            $notificationTitle = $node_id;
            $notificationBody = "Warning: ";

            if ($node_air < 50) {
                $notificationBody .= "low air humidity ";
            }
            if ($node_soil < 20) {
                $notificationBody .= "low soil humidity ";
            }
            if ($battery < 20) {
                $notificationBody .= "low battery ";
            }

            // Build payload JSON
            $payload = array(
                'to' => $deviceToken,
                'data' => array(
                    'title' => $notificationTitle,
                    'body' => $notificationBody
                )
            );

            // Convert payload to JSON
            $jsonPayload = json_encode($payload);

            // Build headers
            $headers = array(
                'Authorization: key=' . $serverKey,
                'Content-Type: application/json'
            );

            // Initialize cURL
            $ch = curl_init();

            // Configure cURL
            curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $jsonPayload);

            // Send cURL request
            $result = curl_exec($ch);

            // Get HTTP response code
            $responseCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);

            // Close cURL
            curl_close($ch);

            // Handle the result
            if ($responseCode == 200) {
                echo "Notification sent successfully!";
            } else {
                echo "Failed to send notification. Response code: " . $responseCode;
            }
        }
        echo "Successfully";
    }
    else {
        //------------------- node_status -----------------------
        $pdo = Database::connect();
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $sql = "UPDATE node_status SET node_auto = ?, node_air = ?, node_soil = ?, node_PH = ?,bump_1 = ?,bump_2 = ?, bump_3 = ?, battery = ?, time = ?, date = ? WHERE node_id = ? and user = ?";
        $q = $pdo->prepare($sql);
        $q->execute(array($node_auto, $node_air, $node_soil, $node_PH, $bump_1, $bump_2, $bump_3, $battery, $tm, $dt, $node_id, $user));

        //------------ if there's a new node_id ----------- (warning)

        Database::disconnect();

        //-------------------- node_record ----------------------
        $pdo = Database::connect();
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $sql = "INSERT INTO $table_name (node_id,node_auto,node_air,node_soil,node_PH,bump_1,bump_2,bump_3,battery,time,date) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        $q = $pdo->prepare($sql);
        $q->execute(array($node_id, $node_auto, $node_air, $node_soil, $node_PH, $bump_1, $bump_2, $bump_3, $battery, $tm, $dt));
        Database::disconnect();

        echo("successfully!!!");
    }
}
else {
    echo("empty_post");
}
?>

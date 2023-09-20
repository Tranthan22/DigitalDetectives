<?php

  require '../../database.php';

  if (!empty($_POST)) {
    $user = $_POST['user'];
    $sensor_id = $_POST['sensor_id'];
    $node_air = $_POST['node_air'];
    $node_soil = $_POST['node_soil'];
    $node_temp = $_POST['node_temp'];
    $battery = $_POST['battery'];

    $table_name = $user . "" . '_record';

    $serverKey = "AAAA1zpcHWA:APA91bFHcPvYrrbfUL_dRZLrdBT90OTZz-X8YBAZtYjmg0F-6NI-awnOMcq4FoO1JY_cJzD1H6UPBORaLsSNiJ0yzi9H9xHY9bWxRH1dGjaV5vr_Y5hswjQdWa_3Njf5vkPtLRFxL04I";

    $deviceToken = "";

    //........................................ Get the time and date.
    date_default_timezone_set("Asia/Jakarta"); // Look here for your timezone : https://www.php.net/manual/en/timezones.php
    $tm = date("H:i:s");
    $dt = date("Y-m-d");
    
    //---------------------- detect garden ------------------------
    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = 'SELECT link FROM device_list WHERE user = :user AND device = :sensor_id';
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(':user', $user, PDO::PARAM_STR);
    $stmt->bindParam(':sensor_id', $sensor_id, PDO::PARAM_INT);
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    if ($result) {
      $garden = $result['link'];
    }
    else {
      $garden = null;
    }
    
    $sql = 'SELECT * FROM garden WHERE user = :user AND garden_id = :garden_id';
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(':user', $user, PDO::PARAM_STR);
    $stmt->bindParam(':garden_id', $garden, PDO::PARAM_INT);
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    if ($result) {
        $airWarning = $result['airWarning'];
        $soilWarning = $result['soilWarning'];
        $batteryWarning = $result['batteryWarning'];
    }
    
    //------------------- notification table ----------------


    //------------------- node_sensor -----------------------
    $sql = "UPDATE garden SET node_air = ?, node_soil = ?, node_temp = ?, battery = ?, time = ?, date = ? WHERE sensor_id = ? and user = ?";
    $q = $pdo->prepare($sql);
    $q->execute(array($node_air,$node_soil,$node_temp,$battery,$tm,$dt,$sensor_id,$user));

    //-------------------- node_record_sensor ----------------------
    $sql = "INSERT INTO $table_name (garden,node_air,node_soil,node_temp,battery,time,date) values(?, ?, ?, ?, ?, ?, ?)";
    $q = $pdo->prepare($sql);
    $q->execute(array($garden,$node_air,$node_soil,$node_temp,$battery,$tm,$dt));

    //--------------------- get device token-----------------------
    $sql = 'SELECT * FROM Users WHERE user_name="' . $user . '" ';
    foreach ($pdo->query($sql) as $row){
      $deviceToken = $row['deviceToken'];
    }
    // Construct notification message
    $notificationTitle = 'garden' . "" . $garden;
    $notificationBody = "Warning: " . $garden;

    if ($node_air < $airWarning) {
      $notificationBody .= "low air humidity ";
    }
    if ($node_soil < $soilWarning) {
      $notificationBody .= "low soil moisture ";
    }
    if ($battery < $batteryWarning) {
      $notificationBody .= "low battery ";
    }
    if ($node_air < $airWarning || $node_soil < $soilWarning || $battery < $batteryWarning){
        //-------------------- notification history ----------------------
        $sql = "INSERT INTO notification (user,content,time,date) values(?, ?, ?, ?)";
        $q = $pdo->prepare($sql);
        $q->execute(array($user,$notificationBody,$tm,$dt));   
    }

    Database::disconnect();
    
    // Check if any of the values are below certain thresholds and send a notification
    if ($node_air < $airWarning || $node_soil < $soilWarning || $battery < $batteryWarning) {

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
    echo("successfully!!!");
  }
  else{
    echo("empty_post");
  }
?>
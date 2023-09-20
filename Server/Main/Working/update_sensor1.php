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
    

    //------------------- node_sensor -----------------------
    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "UPDATE garden SET node_air = ?, node_soil = ?, node_temp = ?, battery = ?, time = ?, date = ? WHERE sensor_id = ? and user = ?";
    $q = $pdo->prepare($sql);
    $q->execute(array($node_air,$node_soil,$node_temp,$battery,$tm,$dt,$sensor_id,$user));

    //-------------------- node_record_sensor ----------------------
    
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "INSERT INTO $table_name (garden,node_air,node_soil,node_temp,battery,time,date) values(?, ?, ?, ?, ?, ?, ?)";
    $q = $pdo->prepare($sql);
    $q->execute(array($garden,$node_air,$node_soil,$node_temp,$battery,$tm,$dt));
    Database::disconnect();

    echo("successfully!!!");
  }
  else{
    echo("empty_post");
  }
?>
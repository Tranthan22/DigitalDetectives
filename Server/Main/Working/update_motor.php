<?php

  require '../../database.php';

  if (!empty($_POST)) {
    $user = $_POST['user'];
    $motor_id = $_POST['motor_id'];
    $pump_1 = $_POST['pump_1'];
    $pump_2 = $_POST['pump_2'];
    $pump_3 = $_POST['pump_3'];

    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "UPDATE garden SET pump_1 = ?, pump_2 = ?, pump_3 = ? WHERE motor_id = ? and user = ?";
    $q = $pdo->prepare($sql);
    $q->execute(array($pump_1, $pump_2,$pump_3,$motor_id,$user));

    Database::disconnect();

    echo("successfully!!!");
  }
  else{
    echo("empty_post");
  }
?>
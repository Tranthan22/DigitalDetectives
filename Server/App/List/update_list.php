<?php
  require '../../database.php';
  if (!empty($_POST)) {
      $user = $_POST['user'];
      $garden_id = $_POST['garden_id'];
      $motor_id = $_POST['motor_id'];
      $sensor_id = $_POST['sensor_id'];
      $mode = 0;

      //check garden mode
      if($sensor_id == 0){
        $mode = '2';
      }
      else if($motor_id == 0){
        $mode =  '1';
      }
      else{
        $mode = '3';
      }
      $pdo = Database::connect();
      $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
      //-------------- reset link
      $sql = "UPDATE device_list SET link = '0' WHERE link = :garden_id AND user = :user";
      $q = $pdo->prepare($sql);
      $q->bindParam(':garden_id', $garden_id, PDO::PARAM_STR);
      $q->bindParam(':user', $user, PDO::PARAM_STR);
      $q->execute();

      
      //--------------- motor
      //device_líst  table
      $sql = "UPDATE device_list SET link = :garden_id WHERE device = :motor_id AND user = :user";
      $q = $pdo->prepare($sql);
      $q->bindParam(':garden_id', $garden_id, PDO::PARAM_STR);
      $q->bindParam(':motor_id', $motor_id, PDO::PARAM_INT);
      $q->bindParam(':user', $user, PDO::PARAM_STR);
      $q->execute();
      //garden table
      $sql = "UPDATE garden SET motor_id = :motor_id, mode = :mode WHERE garden_id = :garden_id AND user = :user";
      $q = $pdo->prepare($sql);
      $q->bindParam(':garden_id', $garden_id, PDO::PARAM_STR);
      $q->bindParam(':mode', $mode, PDO::PARAM_STR);
      $q->bindParam(':motor_id', $motor_id, PDO::PARAM_INT);
      $q->bindParam(':user', $user, PDO::PARAM_STR);
      $q->execute();


      //--------------------sensor
      //device_líst  table
      $sql = "UPDATE device_list SET link = :garden_id WHERE device = :sensor_id AND user = :user";
      $q = $pdo->prepare($sql);
      $q->bindParam(':garden_id', $garden_id, PDO::PARAM_STR);
      $q->bindParam(':sensor_id', $sensor_id, PDO::PARAM_INT);
      $q->bindParam(':user', $user, PDO::PARAM_STR);
      $q->execute();
      //garden table
      $sql = "UPDATE garden SET sensor_id = :sensor_id,  mode = :mode WHERE garden_id = :garden_id AND user = :user";
      $q = $pdo->prepare($sql);
      $q->bindParam(':garden_id', $garden_id, PDO::PARAM_STR);
      $q->bindParam(':mode', $mode, PDO::PARAM_STR);
      $q->bindParam(':sensor_id', $sensor_id, PDO::PARAM_INT);
      $q->bindParam(':user', $user, PDO::PARAM_STR);
      $q->execute();

      Database::disconnect();

      echo("successfully!!!");
  } else {
      echo("empty_post");
  }

?>
<?php

  require '../../database.php';

  if (!empty($_POST)) {
      
    $user =$_POST['user'];
    $garden =$_POST['garden'];
    $pumpTime =$_POST['pumpTime'];
    $dailyPump =$_POST['dailyPump'];
    $airWarning =$_POST['airWarning'];
    $soilWarning =$_POST['soilWarning'];
    $tempWarning =$_POST['tempWarning'];
    $batteryWarning =$_POST['batteryWarning'];  
      
      
      
      
    // $user = 'tien';//$_POST['user'];
    // $garden = '2'; //$_POST['garden'];
    // $pumpTime = 90; //$_POST['pumpTime'];
    // $dailyPump = 100; //$_POST['dailyPump'];
    // $airWarning = 100;//$_POST['airWarning'];
    // $soilWarning = 100;//$_POST['soilWarning'];
    // $tempWarning = 100;//$_POST['tempWarning'];
    // $batteryWarning = 100;//$_POST['batteryWarning'];

    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "UPDATE garden SET pumpTime = ?, dailyPump = ?, airWarning = ?, soilWarning = ?,tempWarning = ?, batteryWarning = ? WHERE garden_id = ? and user = ?";
    $q = $pdo->prepare($sql);
    $q->execute(array($pumpTime, $dailyPump,$airWarning,$soilWarning,$tempWarning,$batteryWarning,$garden,$user));
    
    
    echo("successfully!!!");
    Database::disconnect();

    
  }
  else{
    echo("empty_post");
  }
?>
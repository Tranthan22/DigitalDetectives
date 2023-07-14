<?php
  require 'database.php';
  
  //---------------------------------------- Condition to check that POST value is not empty.
  if (!empty($_POST)) {
    //........................................ keep track post values
    $user = $_POST['user'];
    $node_id = $_POST['node_id'];
    $bump_1 = $_POST['bump_1'];
    $bump_2 = $_POST['bump_2'];
    $bump_3 = $_POST['bump_3'];

    //........................................ 
    
    //........................................ 
    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "UPDATE node_status SET bump_1 = ?, bump_2 = ?, bump_3 = ? WHERE node_id = ? and user = ?";
    $q = $pdo->prepare($sql);
    $q->execute(array($bump_1, $bump_2, $bump_3, $node_id, $user));
    Database::disconnect();
    //........................................ 
  }
  //---------------------------------------- 
?>
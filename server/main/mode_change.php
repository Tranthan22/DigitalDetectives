<?php
  require 'database.php';
  
  //---------------------------------------- Condition to check that POST value is not empty.
  if (!empty($_POST)) {
    //........................................ keep track post values
    $user = $_POST['user'];
    $node_id = $_POST['node_id'];
    $node_auto = $_POST['node_auto'];
    //........................................ 
    
    //........................................ 
    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $sql = "UPDATE node_status SET node_auto = ? WHERE node_id = ? and user = ?";
    $q = $pdo->prepare($sql);
    $q->execute(array($node_auto,$node_id, $user));
    Database::disconnect();
    //........................................ 
  }
  //---------------------------------------- 
?>
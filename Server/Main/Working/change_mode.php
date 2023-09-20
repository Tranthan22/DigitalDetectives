<?php
  require '../../database.php';
  
  //---------------------------------------- Condition to check that POST value is not empty.
  if (!empty($_POST)) {
    //........................................ keep track post values
    $user = $_POST['user'];
    $garden_id = $_POST['garden_id'];
    $node_auto = $_POST['node_auto'];
    
    // $user = 'tien';
    // $garden_id = '2';
    // $node_auto = '1';
    
    //........................................ 
    
    //........................................ 
    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "UPDATE garden SET node_auto = ? WHERE garden_id = ? and user = ?";
    $q = $pdo->prepare($sql);
    $q->execute(array($node_auto,$garden_id, $user));
    echo("Success");
    Database::disconnect();
    //........................................ 
  }
  //---------------------------------------- 
?>
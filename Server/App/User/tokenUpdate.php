<?php
  include '../../database.php';
  

  if (!empty($_POST)) {
    $user = $_POST['user'];
    $deviceToken = $_POST['deviceToken'];

    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "UPDATE Users SET  deviceToken = ? WHERE user_name = ?";
    $q = $pdo->prepare($sql);
    $q->execute(array($deviceToken,$user));

    Database::disconnect();
    
  }
  else{
    echo("empty_post");
  }
?>
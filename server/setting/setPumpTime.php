<?php
  //this program update data from gateway(station raspberry pi) to database 
  //example: http://localhost/.iot/update_data.php?node_id=node_01&node_auto=false&node_temp=30.7&node_humidity=70&node_PH=8&bump_1=OFF&bump_2=ON

  require 'database.php';

  if (!empty($_POST)) {
    $user = $_POST['user'];
    $node_id = $_POST['node_id'];
    $bumpTime = $_POST['bumpTime'];

    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "UPDATE node_status SET  bumpTime = ? WHERE node_id = ? and user = ?";
    $q = $pdo->prepare($sql);
    $q->execute(array($bumpTime,$node_id,$user));

      //------------ if there's a new node_id ----------- (warning)
    Database::disconnect();
    echo("successfully!!!");
  }
  else{
    echo("empty_post");
  }
?>
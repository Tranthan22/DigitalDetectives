<?php
  //this program update data from gateway(station raspberry pi) to database 
  //example: http://localhost/.iot/update_data.php?node_id=node_01&node_auto=false&node_temp=30.7&node_humidity=70&node_PH=8&bump_1=OFF&bump_2=ON

  require 'database.php';

  if (!empty($_POST)) {
    $node_id = $_POST['node_id'];
    $node_auto = $_POST['node_auto'];
    $node_temp = $_POST['node_temp'];
    $node_humidity = $_POST['node_humidity'];
    $node_PH = $_POST['node_PH'];
    $bump_1 = $_POST['bump_1'];
    $bump_2 = $_POST['bump_2'];

    //........................................ Get the time and date.
    date_default_timezone_set("Asia/Jakarta"); // Look here for your timezone : https://www.php.net/manual/en/timezones.php
    $tm = date("H:i:s");
    $dt = date("Y-m-d");
    
    //------------------- node_status -----------------------
    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "UPDATE node_status SET node_auto = ?, node_temp = ?, node_humidity = ?, node_PH = ?,bump_1 = ?,bump_2 = ?, time = ?, date = ? WHERE node_id = ?";
    $q = $pdo->prepare($sql);
    $q->execute(array($node_auto,$node_temp,$node_humidity,$node_PH,$bump_1,$bump_2,$tm,$dt,$node_id));

    //------------ if there's a new node_id ----------- (warning)

    Database::disconnect();

    //-------------------- node_record ----------------------
    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $sql = "INSERT INTO node_record (node_id,node_auto,node_temp,node_humidity,node_PH,bump_1,bump_2,time,date) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    $q = $pdo->prepare($sql);
    $q->execute(array($node_id,$node_auto,$node_temp,$node_humidity,$node_PH,$bump_1,$bump_2,$tm,$dt));
    Database::disconnect();

    echo("successfully!!!");

  }
  else{
    echo("empty_post");
  }
?>
<?php
  //this program update data from gateway(station raspberry pi) to database 
  //example: http://localhost/.iot/update_data.php?node_id=node_01&node_auto=false&node_temp=30.7&node_humidity=70&node_PH=8&bump_1=OFF&bump_2=ON

  require 'database.php';

  if (!empty($_POST)) {
    $user = $_POST['user'];
    $node_id = $_POST['node_id'];
    $node_auto = $_POST['node_auto'];
    $node_air = $_POST['node_air'];
    $node_soil = $_POST['node_soil'];
    $node_PH = $_POST['node_PH'];
    $bump_1 = $_POST['bump_1'];
    $bump_2 = $_POST['bump_2'];
    $bump_3 = $_POST['bump_3'];
    $battery = $_POST['battery'];

    $table_name = $user . "" . '_record';
    //........................................ Get the time and date.
    date_default_timezone_set("Asia/Jakarta"); // Look here for your timezone : https://www.php.net/manual/en/timezones.php
    $tm = date("H:i:s");
    $dt = date("Y-m-d");
    
    if($node_auto == 'false'){
      //------------------- node_status -----------------------
      $pdo = Database::connect();
      $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
      $sql = "UPDATE node_status SET  node_air = ?, node_soil = ?, node_PH = ?,bump_1 = ?,bump_2 = ?, bump_3 = ?, battery = ?, time = ?, date = ? WHERE node_id = ? and user = ?";
      $q = $pdo->prepare($sql);
      $q->execute(array($node_air,$node_soil,$node_PH,$bump_1,$bump_2,$bump_3,$battery,$tm,$dt,$node_id,$user));

      //------------ if there's a new node_id ----------- (warning)

      Database::disconnect();

      //-------------------- node_record ----------------------
      $pdo = Database::connect();
      $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
      $sql = "INSERT INTO $table_name (node_id,node_auto,node_air,node_soil,node_PH,bump_1,bump_2,bump_3,battery,time,date) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      $q = $pdo->prepare($sql);
      $q->execute(array($node_id,'no change',$node_air,$node_soil,$node_PH,$bump_1,$bump_2,$bump_3,$battery,$tm,$dt));
      Database::disconnect();

      echo("successfully!!!");
    }
    else{
      //------------------- node_status -----------------------
      $pdo = Database::connect();
      $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
      $sql = "UPDATE node_status SET node_auto = ?, node_air = ?, node_soil = ?, node_PH = ?,bump_1 = ?,bump_2 = ?, bump_3 = ?, battery = ?, time = ?, date = ? WHERE node_id = ? and user = ?";
      $q = $pdo->prepare($sql);
      $q->execute(array($node_auto,$node_air,$node_soil,$node_PH,$bump_1,$bump_2,$bump_3,$battery,$tm,$dt,$node_id,$user));

      //------------ if there's a new node_id ----------- (warning)

      Database::disconnect();

      //-------------------- node_record ----------------------
      $pdo = Database::connect();
      $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
      $sql = "INSERT INTO $table_name (node_id,node_auto,node_air,node_soil,node_PH,bump_1,bump_2,bump_3,battery,time,date) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      $q = $pdo->prepare($sql);
      $q->execute(array($node_id,$node_auto,$node_air,$node_soil,$node_PH,$bump_1,$bump_2,$bump_3,$battery,$tm,$dt));
      Database::disconnect();

      echo("successfully!!!");
    }
  }
  else{
    echo("empty_post");
  }
?>
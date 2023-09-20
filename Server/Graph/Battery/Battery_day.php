<?php
    // functions.php
    include '../../database.php';
    $date= $_GET['date'];
    $user = $_GET['user'];
    $garden_id = $_GET['garden_id'];
    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $table_name = $user . '_record';

    // Lấy dữ liệu ngày từ bảng dữ liệu của người dùng
    $dayData = array();
    $cnt = 0;
    $sum = 0;
    $query = $pdo->prepare("SELECT * FROM $table_name WHERE date='". $date ."' AND garden='". $garden_id ."'");
    $query->execute();
    while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
        $Data[] = array(
            "y" => $row['battery'],
            "label" => $row['time']
        );
        $cnt = $cnt + 1;
        $sum = $sum + intval($row['battery']);
    }

    Database::disconnect();
    if($cnt == 0){
        $average = 0;
    }
    else{
        $average = $sum / $cnt;
    }
    if(empty($Data)){
        $Data = array(array('x' => '0', 'y' => '0'));
    }
    $dayData['graph'] = $Data;
    $dayData['node'] = $average;

    echo json_encode($dayData, JSON_NUMERIC_CHECK);
?>
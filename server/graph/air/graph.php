<?php
    // functions.php
    include 'database.php';
    $date= $_GET['date'];
    $user = $_GET['user'];
    $node_id = $_GET['node_id'];
    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $table_name = $user . '_record';

    // Lấy dữ liệu ngày từ bảng dữ liệu của người dùng
    $dayData = array();
    $cnt = 0;
    $sum = 0;
    $query = $pdo->prepare("SELECT * FROM $table_name WHERE date='". $date ."' AND node_id='". $node_id ."'");
    $query->execute();
    while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
        $Data[] = array(
            "y" => $row['node_air'],
            "label" => $row['time']
        );
        $cnt = $cnt + 1;
        $sum = $sum + intval($row['node_air']);
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
    $dayData['node_air'] = $average;

    echo json_encode($dayData, JSON_NUMERIC_CHECK);
?>
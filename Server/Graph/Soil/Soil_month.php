<?php
    // functions.php
    include '../../database.php';
    $month = $_GET['month']; // Tháng muốn lọc dữ liệu (vd: '2023-06')
    $user = $_GET['user'];
    $garden_id = $_GET['garden_id'];
    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $table_name = $user . '_record';

    // Lấy dữ liệu trong tháng từ bảng dữ liệu của người dùng
    $monthData = array();
    $query = $pdo->prepare("SELECT DATE(date) AS date, AVG(node_soil) AS average FROM $table_name WHERE DATE_FORMAT(date, '%Y-%m') = :month AND garden = :garden_id GROUP BY DATE(date)");
    $query->bindParam(':month', $month);
    $query->bindParam(':garden_id', $garden_id);
    $query->execute();

    $sum = 0;
    $cnt = 0;

    while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
        $monthData[] = array(
            "y" => $row['average'],
            "label" => $row['date']
        );

        $sum += $row['average'];
        $cnt++;
    }

    Database::disconnect();

    if (empty($monthData)) {
        $monthData = array(array('x' => '0', 'y' => '0'));
        $average = 0;
    } else {
        $average = $sum / $cnt;
    }

    $monthResult['graph'] = $monthData;
    $monthResult['node'] = $average;

    echo json_encode($monthResult, JSON_NUMERIC_CHECK);
?>

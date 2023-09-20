<?php
	include '../database.php'; // declare subprogram access to database

	if(!empty($_POST)){
		$user = $_POST['user'];
		$notifications = array();

	    $pdo = Database::connect();
	    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	    $sql = 'SELECT * FROM notification WHERE user="' . $user . '" ';
	    foreach ($pdo->query($sql) as $row) {
		    $content = $row['content'];
		    $date = $row['date'];
		    $time = $row['time'];
		    $notification = $content . '*' . $date . ' ' . $time;
		    $notifications[] = $notification;
	    }

	    $data = array('notification' => $notifications);
	    echo json_encode($data);
        
	    Database::disconnect();
	}
	else{
		echo("emty_get");
	}
?>
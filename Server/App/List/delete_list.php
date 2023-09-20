<?php
    require '../../database.php';
    
    if (!empty($_POST)) {
        $user = $_POST['user'];
    
        $pdo = Database::connect();
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
        // Delete clearly list for specific user
        $sql = "DELETE FROM device_list WHERE user = ?";
        $q = $pdo->prepare($sql_delete);
        $q->execute(array($user));
    
        echo "Successfully deleted list!";
        
        Database::disconnect();
    } else {
        echo "empty_post";
    }
?>

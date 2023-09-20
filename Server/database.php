<?php
	class Database {
		private static $dbName = 'gatamspc_digitaldetectives';
		private static $dbHost = 'hfn52pro-22290'; /*'hfn52pro-22290';*/
		private static $dbUsername = 'gatamspc_digitaldetectives';
		private static $dbUserPassword = 'Tranbathan02'; 
		 
		private static $cont  = null;
		 
		public function __construct() {
			die('Init function is not allowed');
		}
		 
		public static function connect() {
      // One connection through whole application
      if ( null == self::$cont ) {     
        try {
          self::$cont =  new PDO( "mysql:host=".self::$dbHost.";"."dbname=".self::$dbName, self::$dbUsername, self::$dbUserPassword); 
        } catch(PDOException $e) {
          die($e->getMessage()); 
        }
      }
      return self::$cont;
		}
		 
		public static function disconnect() {
			self::$cont = null;
		}
	}
?>
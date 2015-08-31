<?php

	function connect($user,$passwd){
		$lconnexio = mysql_connect("localhost", "$user", "$passwd") or die("Error a la connexio amb el servidor") ;
		$db = mysql_select_db("BDII_01", $lconnexio) or die ("Error a la connexio amb la base de dades");
		return($lconnexio);
	}

	function disconnect($connexio){
		mysql_close($connexio);
	}
?>

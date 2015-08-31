<?php
    include "err_handler.php";
    if(isset($_GET['login'])){
        session_start();
        include "connect.php";
        $conn = connect("root","");
        $pass = sha1($_GET['Password']);
        $sql = "select UserID,Nom,Password,Tipo from Usuari where UserID='".$_GET['UserID']."' AND Password='".$pass."'";
        $res = mysql_query($sql,$conn) or err("S'ha produit un error intentant validar el vostre id");
        $reg = mysql_fetch_array($res);
        disconnect($conn);
        if(!$res || mysql_num_rows($res)!=1){
            err("Usuari o contrassenya incorrectes");
        }
        else{
            $_SESSION['UserID'] = $reg['UserID'];
            $_SESSION['Nom'] = $reg['Nom'];
            $_SESSION['Password'] = $reg['Password'];
            $_SESSION['Tipo'] = $reg['Tipo'];
        }
    } else {
        err("AH AH AH, no podeu accedir aquí");
    }
?>
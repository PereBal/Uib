<?php
    if(isset($_POST['register'])){
        session_start();
        include "connect.php";
        include "err_handler.php";
        $conn=connect("root","");
        $userID=$_POST['UserID'];
        $nom=$_POST['Nom'];
        $pass=$_POST['Password'];
        $conpass=$_POST['Confirm'];
        if(empty($userID)||empty($nom)||empty($pass)||empty($conpass)){
            
        }else if(strlen($pass)<8 || strlen($pass)>24){
            disconnect($conn);
            err("El password està restringit a 8-24 caracters");
        }else{
            $sql="SELECT UserID FROM Usuari WHERE UserID='".$userID."'";
            $res=mysql_query($sql,$conn) or err("S'ha produit un error intentant validar el vostre ID");
            if(mysql_num_rows($res)!=0){
                disconnect($conn);
                err("Aquest ID ja existeix!");
            } else{
                if($pass!=$conpass){
                    disconnect($conn);
                    err("La contrasenya no coincideix amb la confirmació de la mateixa");
                } else {
                    $sql="INSERT INTO Usuari(UserID,Nom,Password)";
                    $sql=$sql."VALUES('".$userID."','".$nom."','".sha1($pass)."')";
                    mysql_query($sql,$conn) or err("S'ha produit un error intentant registrar el nou usuari.");
                    disconnect($conn);
                    $_SESSION['UserID']=$userID;
                    $_SESSION['Nom'] = $nom;
                    $_SESSION['Password'] = sha1($pass);
                    $_SESSION['Tipo'] = 0; //por defecto
                }
            }
        }
    } 
?>
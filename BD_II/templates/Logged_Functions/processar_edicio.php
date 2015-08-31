<?php
    session_start();
    include "../err_handler.php";
    $pwd = $_SESSION['Password'];
    if(sha1($_POST['Password']) == $pwd){
        $sql="";
        $nm = $_SESSION['Nom'];
        if(isset($_POST['Nom']) && !empty($_POST['Nom']) && $_POST['Nom'] != $nm){
            $sql=$sql."Nom='".$_POST['Nom']."'";
            $_SESSION['Nom'] = $_POST['Nom'];
        }

        if(isset($_POST['newPassword']) && !empty($_POST['newPassword']) && sha1($_POST['newPassword']) != $pwd){
            $npwd = $_POST['newPassword'];
            if(strlen($npwd)<8 || strlen($npwd)>24){
                $_SESSION['Nom'] = $nm;
                err("El password no té entre 8 i 24 caràcters de longitut");
            } else {
                if(!empty($sql))$sql=$sql.", "; 
                $sql=$sql."Password='".sha1($_POST['newPassword'])."'";
                $_SESSION['Password'] = sha1($_POST['newPassword']);
            }
        }

        if(!empty($sql)){
            include "../connect.php";
            $conn=connect("root","");
            $sql="update Usuari set ".$sql." where UserID='".$_SESSION['UserID']."'";
            if(!mysql_query($sql,$conn)){
                $_SESSION['Password'] = $pwd;
                $_SESSION['Nom'] = $nm;
            }
            disconnect($conn);
        }
        
        echo "Els canvis en el seu perfil s'han guardat satisfactòriament";
    } else {
        err("El camp password actual no coincideix amb el password guardat a la base de dades");
    }
?>
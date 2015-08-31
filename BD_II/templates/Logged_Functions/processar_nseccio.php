<?php
    include "../connect.php";
    include "../err_handler.php";
    function warning_handler($errno, $errstr) {
        err("No s'ha pogut carregar la imatge degut a que el directori no poseeix els permisos corresponents. Consultau l'apartat 1 del manual d'usuari");
    }
    $sql = "insert into Seccio (";
    $sql2 = " values (";
    if(isset($_POST['Titol']) && isset($_POST['Descripcio']) && isset($_POST['Preu']) && (isset($_FILES['Imatge']) && !empty($_FILES['Imatge']['name']))){
        $sql=$sql.'Titol,Descripcio,Preu,Imatge';
        $sql2.="'".$_POST['Titol']."', '".$_POST['Descripcio']."', ".$_POST['Preu'].", '".$_FILES['Imatge']['name']."'";
    } else {
        err("No s'ha pogut accedir als camps necessaris per a publicar la seccio");
    }

    $conn = connect("root","");
    if(isset($_POST['Codi_seccio']) && !empty($_POST['Codi_seccio'])){
        $cs = $_POST['Codi_seccio'];
        $sql3 = "select Codi_seccio from Seccio where Codi_seccio=".$cs;
        $reg = mysql_query($sql3,$conn) or die("S'ha produit un error a la query");
        if(!$reg || mysql_num_rows($reg) != 0){
            err("Ja existeix una secció amb el codi: ".$cs);
        } else {
            $sql=$sql.',Codi_seccio';
            $sql2=$sql2.", ".$cs;
        }
    }
    $sql = $sql.")".$sql2.")";
    set_error_handler("warning_handler", E_WARNING);
    move_uploaded_file($_FILES['Imatge']['tmp_name'], $_SERVER['DOCUMENT_ROOT']."/Anuncis_Pershe/data/uploads/" . $_FILES['Imatge']['name']) or err("S'ha produit un error carregant la imatge");
    restore_error_handler();
    mysql_query($sql,$conn) or err("No s'ha pogut publicar la secció degut a un error, comprovau que el títol de la mateixa sigui únic");
    echo "La secció s'ha publicat satisfactòriament";
    disconnect($conn);
?>
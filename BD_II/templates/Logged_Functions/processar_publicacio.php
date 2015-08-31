<?php
    session_start();
    include "../connect.php";
    include "../err_handler.php";
    function warning_handler($errno, $errstr) {
        err("No s'ha pogut carregar la imatge degut a que el directori no poseeix els permisos corresponents. Consultau l'apartat 1 del manual d'usuari");
    }
    $conn = connect("root","");

    if(isset($_POST['Titol_curt']) && isset($_POST['Data_web']) && isset($_POST['Data_no_web']) && isset($_POST['Telefon']) && isset($_POST['Seccio'])){
        $res = mysql_query("select `Preu`,`Codi_seccio`,`Imatge` from Seccio where Seccio.Titol='".$_POST['Seccio']."'",$conn) or err("S'ha produit algún error greu, per favor, torneu a intentar-ho més tard");
        $reg2 = mysql_fetch_array($res);
        $sql="insert into Anunci(Titol_curt,Data_web,Data_no_web,Telefon,Usuari,Seccio";
        $sql2="'".$_POST['Titol_curt']."', '".$_POST['Data_web']."', '".$_POST['Data_no_web']."', ".$_POST['Telefon'].", '".$_SESSION['UserID']."', ".$reg2['Codi_seccio'];
    } else {
        err("Algun dels camps no s'ha emplenat correctament");
    }
    
    if(isset($_POST['Text']) && $_POST['Text'] != ""){
        $sql=$sql.",Text";
        $sql2=$sql2.", '".$_POST['Text']."'";
    }

    $sql=$sql.",Imatge";
    if (isset($_FILES['Imatge'])) {
        if($_FILES['Imatge']['name'] != ""){
            $sql2=$sql2.", '".$_FILES['Imatge']['name']."'";
            set_error_handler("warning_handler", E_WARNING);
            move_uploaded_file($_FILES['Imatge']['tmp_name'], $_SERVER['DOCUMENT_ROOT']."/Anuncis_Pershe/data/uploads/" . $_FILES['Imatge']['name']) or err("S'ha produit un error carregant la imatge");
            restore_error_handler();
        } else {
            $sql2=$sql2.", '".$reg2['Imatge']."'";
        }
    } else {
        $sql2=$sql2.", '".$reg2['Imatge']."'";
    }

    $sql=$sql.",Data_publicacio,Num_canvis,Preu";
    $sql2=$sql2.",\" ".date("Y-m-d")."\", 0, ".$reg2['Preu'];
    

	$sql=$sql.") values (".$sql2.")";
    mysql_query($sql,$conn) or err("S'ha produit algún error greu, per favor, torneu a intentar-ho més tard");
    echo "L'anunci s'ha publicat satisfactòriament, els ".$reg2['Preu']."€ se us han carregat al compte";
    disconnect($conn);
?>

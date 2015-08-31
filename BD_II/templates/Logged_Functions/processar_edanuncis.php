<?php
    session_start();
    include "../connect.php";
    include "../err_handler.php";
    function warning_handler($errno, $errstr) {
        err("No s'ha pogut carregar la imatge degut a que el directori no poseeix els permisos corresponents. Consultau l'apartat 1 del manual d'usuari");
    }
    $conn = connect("root","");

    if($_POST['Operation']==="modificar"){
    if(isset($_POST['Titol_curt']) && isset($_POST['Data_web']) && isset($_POST['Data_no_web']) && isset($_POST['Telefon']) && isset($_POST['Seccio'])){
        $sql="UPDATE Anunci ";
        $sql.="SET `Titol_curt`='".$_POST['Titol_curt']."', `Data_web`=\"".$_POST['Data_web']."\", `Data_no_web`= \"".$_POST['Data_no_web']."\", `Telefon`= ".$_POST['Telefon'].", `Seccio`= ".$_POST['Seccio'];
        unset($_POST['Titol_curt']); unset($_POST['Data_web']); unset($_POST['Data_no_web']); unset($_POST['Telefon']);
    } else {
        err("S'ha produit algún error greu, per favor, torneu a intentar-ho");
    }
       
    $conn = connect("root","");

    if(isset($_POST['Text']) && $_POST['Text'] != ""){
        $sql.=", `Text`= '".$_POST['Text']."'";
        unset($_POST['Text']);
    }
//falta afegir imatge per defecte de la seccio si no n'hi ha cap
    if (isset($_FILES['Imatge'])) {
        if($_FILES['Imatge']['name'] != ""){
            $sql.=",  Imatge='".$_FILES['Imatge']['name']."'";
            set_error_handler("warning_handler", E_WARNING);
            move_uploaded_file($_FILES['Imatge']['tmp_name'], $_SERVER['DOCUMENT_ROOT']."/Anuncis_Pershe/data/uploads/" . $_FILES['Imatge']['name']) or err("S'ha produit un error carregant la imatge");
            restore_error_handler();
        } 
        unset($_POST['Seccio']); unset($_FILES['Imatge']);
    }

    $sql.=" WHERE Id='".$_POST['AnunciID']."';";
    mysql_query($sql,$conn) or err("S'ha produit algún error greu, per favor, torneu a intentar-ho més tard");
         ?> 
        <script>
	       ajax('inici');
        </script>
    <?php  
    }elseif($_POST['Operation']==="cancelar"){
    ?> 
        <script>
	       ajax('inici');
        </script>
    <?php  
    }elseif($_POST['Operation']==="eliminar"){
        $sql="DELETE FROM Anunci WHERE Id='".$_POST['AnunciID']."';";
        mysql_query($sql,$conn) or err("S'ha produit algún error greu, per favor, torneu a intentar-ho més tard");
        ?>
        <script>
	       ajax('inici');
        </script>
        <?php
    }
        disconnect($conn);
?>

<?php
    session_start();
    include "../connect.php";
    include "../err_handler.php";
    $conn = connect("root","");

    if($_POST['Operation']==="modificar"){
    if(isset($_POST['Titol']) && isset($_POST['Descripcio']) && isset($_POST['Preu'])){
        $sql="UPDATE Seccio ";
        $sql.="SET `Titol`='".$_POST['Titol']."', `Descripcio`=\"".$_POST['Descripcio']."\", `Preu`= \"".$_POST['Preu']."\"";
        unset($_POST['Titol_curt']); unset($_POST['Descripcio']); unset($_POST['Preu']); 
    } else {
        err("S'ha produit algún error greu, per favor, torneu a intentar-ho");
    }
       
    $conn = connect("root","");

//falta afegir imatge per defecte de la seccio si no n'hi ha cap
    if (isset($_FILES['Imatge'])) {
        if($_FILES['Imatge']['name'] != ""){
            $sql.=",  Imatge='".$_FILES['Imatge']['name']."'";
            move_uploaded_file($_FILES['Imatge']['tmp_name'], $_SERVER['DOCUMENT_ROOT']."/Anuncis_Pershe/data/uploads/" . $_FILES['Imatge']['name']) or err("S'ha produit un error carregant la imatge");
        } 
        unset($_FILES['Imatge']);
    }

    $sql.=" WHERE Codi_seccio='".$_POST['codi']."';";
    mysql_query($sql,$conn) or err("S'ha produit algún error greu, per favor, torneu a intentar-ho més tard");
         ?> 
        <script>
	        ajax('modificar');
        </script>
    <?php  
    }elseif($_POST['Operation']==="cancelar"){
    ?> 
        <script>  
            ajax('modificar');
        </script>
    <?php  
    }
        disconnect($conn);
?>

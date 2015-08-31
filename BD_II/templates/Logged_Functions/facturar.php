<?php
    include "../connect.php";
    include "../err_handler.php";
    $conn = connect("root","");
    $pr="<div class=\"box\"><h3>Facturacio</h3><table><thead><td class=\"header\">Títol</td><td class=\"header\">Preu</td><td class=\"header\">Data de publicació</td></thead>";
    $ttl = 0;
    $sql = "START TRANSACTION";
    mysql_query($sql,$conn);
    $sql = "BEGIN";
    mysql_query($sql,$conn);

    $retval = 1;
    foreach($_POST['aid'] as $id){
        $pr .="<tr class=\"fact\"><td>".$id['t_c']."</td><td>".$id['pr']."</td><td>".$id['dp']."</td></tr>";
        $ttl = $ttl + $id['pr'];
        
        $sql = "update Anunci set pagat=1, Num_canvis = 0 where Anunci.Id=".$id['id']." ";
        mysql_query($sql,$conn);
        if(mysql_affected_rows() == 0){ $retval = 0; }
    }
    if($retval == 1){
        $sql = "COMMIT";
        mysql_query($sql,$conn);
    } else {
        $sql = "ROLLBACK";
        mysql_query($sql,$conn);
        err("S'ha produit un error, el cobr dels anuncis no s'ha portat a terme");
    }
    $pr .= "<tr></tr><tr><td></td><td class=\"fact\">TOTAL:</td><td>".$ttl."€</td></tr></table></div>";
    echo $pr;
    disconnect($conn);
?>
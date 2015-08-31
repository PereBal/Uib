<?php
    include "../err_handler.php";
    include "../connect.php";

    //el left outer es carrega el deute
    $sql = "select `UserID`,`Nom`,COUNT(Id) as NAnuncis,SUM(Anunci.Preu) as Deute, `Tipo` from Usuari left outer join Anunci on Usuari.UserID=Anunci.Usuari group by Usuari.UserID having Usuari.Tipo=0";
    if(isset($_GET['data']) && isset($_GET['filterBy'])){
        $f = $_GET['filterBy'];
        $d = $_GET['data'];
        switch($f){
            case "UserID":
                if(!empty($d)){
                    $sql.=" and UserID like '".$d."%'";    
                }
                $sql .= " order by UserID";
                break;
            case "Nom":
                if(!empty($d)){
                    $sql.=" and Nom like '".$d."%'";    
                }
                $sql .= " order by Nom";
                break;
            case "Deute":
                $sql .= " and Deute>0 order by Deute desc";
                break;
        }
    }

    $conn = connect("root","");
    $res = mysql_query($sql,$conn) or err("S'ha produit un error a la connexió amb la base de dades");
    if(!$res){
        err("S'ha produit un error a la connexio amb la base de dades,2");
    }
    if(mysql_num_rows($res) == 0){
        echo "<h3 align=\"center\">No hi ha usuaris Registrats<h3>";
    } else {
        while($reg = mysql_fetch_array($res)){
            if($reg['Deute'] == 0){
                $reg['Deute'] = 0.0;
            }   
?>        
    <div id='box' class="box" onclick="facturar(this);">
        <h3><label class="nom"><?php echo $reg['Nom'];?></label><?php if($reg['Tipo']) echo " - Administrador"?></h3>
        <div class="content" >
            <h4>ID d'usuari: <label class="id"><?php echo $reg['UserID'];?></label></h4>
            <h4 class="nAnuncis">Anuncis Publicats: <label><?php echo $reg['NAnuncis'];?></label></h4>
            <h4>Deute: <label class="deute"><?php echo $reg['Deute'];?></label>€</h4>
        </div>
    </div>
<?php
        }
    }
?>
<script>
    var facturar = function(elem){
                
        var deute = $(elem).find('.deute').text();
        
        if(deute > 0){
            if(confirm("Voleu passar a facturació?")){
                var fd = { UserID: $(elem).find('.id').text() };
                
                $.ajax({
                    url: 'templates/Logged_Functions/filter_facturar.php',
                    method: 'GET',
                    success: completeHandler = function(data) {
                        //$('#output').empty();
                        $('#output').html(data);
                    },
                    error: errorHandler = function(data) {
                        try{
                            var response = JSON.parse(data.responseText);
                            alert(response.message);
                        } catch (Exception){
                            $('#output').empty();
                            alert("FALLBACK MODE, RECARREGUI LA PÀGINA PER TORNAR A LA NORMALITAT: "+data.responseText);
                            document.location.replace("/Anuncis_Pershe/");
                        }
                    },
                    data: fd
                },'json');
            }
        } else {
            alert("No es pot cobrar a qui no deu");
        }
    }
</script>
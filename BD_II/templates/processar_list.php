<?php
	session_start();
	include "connect.php";
	include "includes.php";
    include "err_handler.php";

	$conn = connect("root","");
    $sql="SELECT `Id`, `Titol_curt`, `Text`, `Data_publicacio`, `Telefon`, Anunci.Imatge, `Usuari`, Anunci.Seccio FROM `Anunci`";
    if(!empty($_SESSION['UserID']) && $_SESSION['Tipo'] == 0){
        $and = " and";
        $uid = "Anunci.Usuari='".$_SESSION['UserID']."'";
    } 

    if(!empty($_GET['dinici']) && !empty($_GET['dfi'])){
            $and = " and";
            $uid = " Anunci.Data_web BETWEEN '".$_GET['dinici']."' and '".$_GET['dfi']."' ";
            if(empty($_SESSION['UserID'])){
                $uid.="and Anunci.Data_no_web>='".date("Y-m-d")."'";
            }
        }else{ 
            if(empty($_SESSION['Tipo'])&&empty($_SESSION['UserID'])){
                $and = " and";
                $uid = " Anunci.Data_web <= '".date("Y-m-d")."' and Anunci.Data_no_web>= '".date("Y-m-d")."' ";
            }else{
                //El admin ve todos los anuncios
                $and = "";
                $uid = "";
            }
            
        }


        $sec = $_GET["section"];
        $ord = $_GET["order"];
        $src = $_GET["search"];
        if($sec==""){
            $sql.=" WHERE ".$uid.$and." Anunci.Titol_curt LIKE '".$src."%'";
        } else {
            $sql.=" INNER JOIN `Seccio` ON Anunci.Seccio=Seccio.Codi_seccio WHERE ".$uid.$and." Seccio.Titol='".$sec."'";
            if($src!=""){
                $sql.=" AND Anunci.Titol_curt LIKE '".$src."%'";
            }
        }
        $sql.=" ORDER BY ".$ord." ASC";
   
    $i = 0; //s'hauria de poder evitar emprant jquery pero sua de jo i no tenc ganes de perdre mes temps
	$res = mysql_query($sql,$conn) or err("S'ha produit un error intentant llistar els anunicis.");
    if(mysql_num_rows($res)==0){
        echo "<h3 align=\"center\">No hi ha anuncis publicats<h3>";
    }else{
        while($rows=mysql_fetch_array($res)){ 
    ?>
            <div class="box" id="<?php echo "$i"?>">
                <input type="hidden" class="id" value="<?php echo $rows['Id'];?>">
                <input type="hidden" class="cs" value="<?php echo $rows['Seccio'];?>">
                <h3><?php echo $rows['Titol_curt'];?></h3>
                <div class="subtitle">
                    <h4 class="userid">Usuari: <?php echo $rows['Usuari'];?></h4>
                    <h4 class="tel">telèfon: <?php echo $rows['Telefon'];?></h4>
                    <h4 class="date">Data: <?php echo $rows['Data_publicacio'];?></h4>
                </div>
                <div class="anunci_content">
                    <img src="/Anuncis_Pershe/data/uploads/<?php echo $rows['Imatge'];?>" width="150">
                    <p><?php echo $rows['Text'];?></p>
                </div>
            </div>
<?php
        $i++;
        }
	}
?>
<script>
    window.clicks = 0;
    $.each($('#list_anuncis').find('.box'), function(){
        
        $(this).click(function(){
            var ad = {};
            ad.AnunciID = $(this).find('.id').val();
            ad.Codi_seccio = $(this).find('.cs').val();
            ad.Titol_curt = $(this).find('h3').text();
            
            var idiv = $(this).find('.subtitle');
            ad.UserID = idiv.find('.userid').text();
            ad.Data_publicacio = idiv.find('.date').text();
            ad.Telefon = idiv.find('.tel').text();
            
            idiv = $(this).find('.anunci_content');
            ad.Imatge = idiv.find('img').attr("src");
            ad.Text = idiv.find('p').text();
            var divid=$(this).attr('id');
            clicks++;
            if(clicks==1){
                $.ajax({
                    url: '/Anuncis_Pershe/templates/show_anunci.php',  //server script to process data
                    method: 'POST',
                    //Ajax events
                    success: completeHandler = function(data) {
                        $($('#'+divid).html(data));

                    },
                    error: errorHandler = function(data) {
                        try{
                            var response = JSON.parse(data.responseText);
                            $('#err').html("ERR: "+response.message);
                        } catch (Exception) {
                            alert("FALLBACK MODE ERROR, refrescau la pagina per tornar a la normalitat:\n"+data.responseText);
                        }
                    },
                    // Form data
                    data: ad
                }, 'json');
            }});
        
    
    });
</script>
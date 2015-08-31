<?php
	session_start();
	include "connect.php";
	include "includes.php";
    include "err_handler.php";
	$conn = connect("root","");
    $sql="SELECT `Codi_seccio`, `Titol`, `Descripcio`, `Preu`,`Imatge` FROM `Seccio`";
    if(isset($_GET["order"]) && isset($_GET["searchby"]) && isset($_GET["search"])){
        $ord = $_GET["order"];
        $sby = $_GET["searchby"];
        $src = $_GET["search"];
            $sql.=" WHERE ".$sby." LIKE '".$src."%'";
            $sql.=" ORDER BY ".$ord." ASC";
        }
        $i = 0; //s'hauria de poder evitar emprant jquery pero sua de jo i no tenc ganes de perdre mes temps
	$res = mysql_query($sql,$conn) or err("S'ha produit un error intentant llistar els anunicis.");

    if(mysql_num_rows($res)==0){
        echo "<h3 align=\"center\">No hi ha seccions creats<h3>";
    }else{
        while($rows=mysql_fetch_array($res)){ 
    ?>
            <div class="box" id="<?php echo "$i"?>">
                <h3><?php echo $rows['Titol'];?></h3>
                <div class="subtitle">
                    <h4 class="codi">Codi: <?php echo $rows['Codi_seccio'];?></h4>
                    <h4 class="preu">Preu: <?php echo $rows['Preu'];?></h4>
                </div>
                <div class="anunci_content">
                    <img src="/Anuncis_Pershe/data/uploads/<?php echo $rows['Imatge'];?>" width="150">
                    <p><?php echo $rows['Descripcio'];?></p>
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
            ad.Titol = $(this).find('h3').text();
            
            var idiv = $(this).find('.subtitle');
            ad.Codi_seccio  = idiv.find('.codi').text();
            ad.Preu = idiv.find('.preu').text();
            
            idiv = $(this).find('.anunci_content');
            ad.Imatge = idiv.find('img').attr("src");
            ad.Descripcio = idiv.find('p').text();
            var divid=$(this).attr('id');
            clicks++;
            if(clicks==1){
                $.ajax({
                    url: '/Anuncis_Pershe/templates/show_seccio.php',  //server script to process data
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
   
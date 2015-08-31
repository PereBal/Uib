<?php
    include "../connect.php";
    include "../err_handler.php";
    if(isset($_GET['uid'])){
        $uid = $_GET['uid'];
        $sql = "SELECT `Id`, `Titol_curt`, `Text`, `Data_publicacio`, `Telefon`, Anunci.Imatge, `Preu` FROM `Anunci` where";
        $conn = connect('root','');
        $sql2 = "";
        
        if(isset($_GET['type'])){
            $t = $_GET['type'];
            if($t == "pendents"){
                $sql2 = " having Preu>0"; 
            }
            if(isset($_GET['di']) && !empty($_GET['di'])){
                $sql .= " Anunci.Data_web >= '".$_GET['di']."' and";
            } 
            if(isset($_GET['df']) && !empty($_GET['df'])) {
                $sql .= " Anunci.Data_web <= '".$_GET['df']."' and";
            } 
        }
        
        $sql .= " Anunci.Usuari='".$uid."'".$sql2;
        $res = mysql_query($sql,$conn) or err("S'ha produit un error a la connexio amb la BD");
        if(mysql_num_rows($res)==0){
            echo "<h3 align=\"center\">No hi ha anuncis al rang especificat<h3>";
        }else{
        while($rows = mysql_fetch_array($res)){

?>
        <div class="box">
            <input type="hidden" class="anunciid" value="<?php echo $rows['Id'];?>">
            <h3 class="head"><?php echo $rows['Titol_curt']." - ".$rows['Preu']."€";?></h3>
            <div class="subtitle">
                <h4 class="userid">Usuari: <label><?php echo $_GET['uid'];?></label></h4>
                <h4>Data: <label class="date"><?php echo $rows['Data_publicacio'];?></label></h4>
            </div>
            <div class="anunci_content">
                <img src="/Anuncis_Pershe/data/uploads/<?php echo $rows['Imatge'];?>" width="150">
                <p><?php echo $rows['Text'];?></p>
            </div>
        </div>
<?php
        }
        }
        disconnect($conn);
    } 
?>

<script>
    var cobrar = function() {
        if(confirm("Estau segur que voleu facturar el deute?")){
            var fd = {uid: $('#userid').text(), aid: [{}]};
            var i = 0, t;
            $.each($('#list_anuncis').find('.box'),function(key,val){
                t = $(this).find('.head').text().split(" - ");
                t[1] = t[1].slice(0,-1); //elim el €
                console.log(t);
                if(t[1]>0){
                    fd.aid[i++] = {id: $(this).find('.anunciid').val(), t_c:t[0], pr:t[1], dp:$(this).find(".date").text()}; 
                }
            });
            
            $.ajax({
                url: 'templates/Logged_Functions/facturar.php',  //server script to process data
                type: 'POST',
                //Ajax events
                success: completeHandler = function(data) {
                    $('#list_anuncis').empty();
                    $('#output').html(data);
                },
                error: errorHandler = function(data) {
                    try{
                        var response = JSON.parse(data.responseText);
                        alert(response.message);
                        ajax('lusrs');
                    } catch (Exception){
                        $('#output').empty();
                        alert("FALLBACK MODE, RECARREGUI LA PÀGINA PER TORNAR A LA NORMALITAT: "+data.responseText);
                        document.location.replace("/Anuncis_Pershe/");
                    }
                },
                // Form data
                data: fd
            }, 'json');
        }    
    }
</script>
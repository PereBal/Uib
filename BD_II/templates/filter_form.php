<div id="filter-form" class="submenu">
    <form id="options" >
        
         <table>
            <tr>
                <td>
                    Secció: 
        <select class="opt" id="section" name="section" onchange="return ajaxCall();">
            <option selected value="">  </option>
            <?php
                $conn = connect("root","");
                $sql="SELECT `Titol` FROM `Seccio`";
                $res = mysql_query($sql,$conn) or die("S'ha produit un error intentant llistar les seccions.");
                while($rows=mysql_fetch_array($res)){ 
                    ?><option value="<?php echo "$rows[Titol]"?>"><?php echo "$rows[Titol]";?>  </option>
                    <?php
                }
            ?>
        </select>
                    </td>
                <td>
        <label>Ordenació:</label>
        <select class="opt" id="order" name="order" onchange="return ajaxCall();">
            <option value ='Titol_curt' selected > Titol </option>
            <option value='Data_publicacio'> <label> Data Publicacio </label></option>
        </select>
                    </td>
                <td>
        <label>Cercar:</label>
            <input type="text" id="search" name="search" onkeyup="return ajaxCall();">
                    </td>
             </tr>
             <tr>
             <td>
                 <label>Interval de dates:</label>
                    <input type="text" id="dinici" name="dinici" style="width: 70px" onchange="return ajaxCall();">
                 </td>
                 <td>
                    <input type="text" id="dfi" name="dfi" style="width: 70px" onchange="return ajaxCall();">
                 </td>
             
             </tr>
        </table>
    </form>
    
    
    <script>     
    var val="", val2="";

   $(function() {
        $("#dinici").datepicker({
        dateFormat: "yy-mm-dd",
            
    onSelect: function (dateValue, inst) {
        $("#dfi").datepicker("option", "minDate", dateValue)
    }
});
        $("#dfi").datepicker({
            dateFormat: "yy-mm-dd",
        }); 
    }); 

        
        if(!document.getElementById('list_anuncis')){
            var la = document.createElement("div");
            la.id = "list_anuncis";
            $('#ajax_response').find('.contenido').append(la);
        }

        var ajaxCall = function(){
            var fd = {};
        
            $.each($("#options")[0], function(key, val){
                fd[val.name] = val.value;
            });
            console.log(fd);
            $.ajax({
                url: '/Anuncis_Pershe/templates/processar_list.php',  //server script to process data
                method: 'GET',
                //Ajax events
                success: completeHandler = function(data) {
                    $('#list_anuncis').html(data);
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
                // Form data
                data: fd
            }, 'json');
        }
        ajaxCall();
    </script>
</div>
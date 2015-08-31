<div id="filter-form" class="submenu">
    <input type="hidden" id ="h" value="<?php echo $_GET['UserID'];?>">
    <form id="options">
        <table>
            <tr>
                <td>
                    <input type="button" value="Cobra-ho tot" onclick="cobrar();">
                </td>
                <td>
                    <label>Mètode de filtrat:</label>
                </td>
                <td>
                    <select id="pendents" onchange="ajax_call(this);">
                        <option onclick="$('#options').find('.hidden').hide(); $('#options').find('.hidden2').hide();">Des de l'origen dels temps</option>
                        <option onclick="$('#options').find('.hidden').hide(); $('#options').find('.hidden2').hide();">Pendents de Pagar</option>
                        <option onclick="$('#options').find('.hidden').show();">Rang de dates</option>
                    </select>
                </td>
                <td class="hidden">
                    <label>Inici:</label>
                    <input type="text" id="dinici" style="width: 70px" onchange="ajax_call(this);">
                </td>
                <td class="hidden2">
                    <label>Fi:</label>
                    <input type="text" id="dfi" style="width: 70px" onchange="ajax_call(this);">
                </td>
            </tr>
        </table>
    </form>
</div>
<script>
    var val="", val2="";

   $(function() {
    $( "#dinici" ).datepicker({
            dateFormat: "yy-mm-dd",
        }); 
            
        $("#dfi").datepicker({
            dateFormat: "yy-mm-dd",
        }); 
    });
    $('#options').find('.hidden').hide();
    $('#options').find('.hidden2').hide();

    var ajax_call = function(input){
        if(input.id == "dinici") {
            val = input.value;
            $('#options').find('.hidden2').show();
        } else if(input.id =="dfi"){
            val2 = input.value;
        }
        var data = {type: input.id, di: val, df: val2, uid: $('#h').val()};

        
        if((data.di!="" && data.df!="") && data.di >= data.df){
            alert("La data de fi del rang ha de ser major que la data d'inici");
        } else {
            $.ajax({
                url: 'templates/Logged_Functions/processar_filtre_factura.php',  //server script to process data
                type: 'GET',
                //Ajax events
                success: completeHandler = function(data) {
                    $('#list_anuncis').html(data);
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
                data: data
            }, 'json'); 
        }
    }
    ajax_call({}); 
</script>
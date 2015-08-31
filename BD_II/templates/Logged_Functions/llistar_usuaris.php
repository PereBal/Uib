<div class="submenu">
    <form id="filter-form">
        <label>Filtre:</label> 
        <select name="filterBy" onchange="ajax_call();">
            <option selected onclick="$('#filter-form [name=inputField]').hide()">Deute</option>
            <option onclick="$('#filter-form [name=inputField]').show()">Nom</option>
            <option onclick="$('#filter-form [name=inputField]').show()">UserID</option>
        </select>
        <input type="text" value="" onkeyup="ajax_call();" name="inputField">
    </form>
</div>

<script>
    if(!document.getElementById('list_anuncis')){
        var la = document.createElement("div");
        la.id = "list_anuncis";
        $('#ajax_response').find('.contenido').append(la);
    }
    
    $('#filter-form [name=inputField]').hide();
    
    var ajax_call = function() {
        var formElem = $('#filter-form [name=inputField]')[0];
        var fd = {data: formElem.value, filterBy: $("#filter-form [name=filterBy]").val()};

        $.ajax({
            url: 'templates/Logged_Functions/processar_lusrs.php',
            method: 'GET',
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
            data: fd
        },'json');
    }
    
    ajax_call();
</script>
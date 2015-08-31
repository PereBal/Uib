<div id="filter-form" class="submenu">
    <form id="options" >
        Ordenació:
        <select class="opt" id="order" name="order" onchange="return ajaxCall();">
            <option value ='Codi_seccio' selected > Codi </option>
            <option value='Titol'>  Títol </option>
            <option value='Preu'> Preu </option>
        </select>
        Cercar per :
            <select class="opt" id="searchby" name="searchby" onchange="return ajaxCall();">
            <option value ='Codi_seccio'  > Codi </option>
            <option value='Titol' selected>  Títol </option>
            <option value='Descripcio'> Descripció </option>
        </select>
            <input type="text" id="search" name="search" onkeyup="return ajaxCall();">
    </form>
    
    <script>        
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
            console.log(fd['search']);
            if(fd['searchby']=='Codi_seccio' && (!$.isNumeric(fd['search'])&& $.trim(fd['search']).length)){
                alert("El codi ha de ser un valor numeric.");
            }else {
                $.ajax({
                    url: '/Anuncis_Pershe/templates/processar_list_seccio.php',  //server script to process data
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
        }
        ajaxCall();
        
    </script>
</div>
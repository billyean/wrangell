$.ajax({
    url:  '/service/list',
    type:  'get',
    dataType:  'json',
    success: function  (data) {
        let rows =  '';
        data.services.forEach(service => {
        rows += `
        <tr>
            <td class="col-1">${service.name}</td>
            <td class="col-3">${service.time_type}</td>
            <td class="col-1">${service.rate}</td>
            <td class="col-3">
                <button class="btn deleteBtn btn-primary" data-id="${service.id}"">Delete</button>
                <button class="btn updateBtn btn-primary" data-id="${service.id}"">Update</button>
            </td>
        </tr>`;
    });
    $('#myTable tbody').append(rows);
    $('.deleteBtn').each((i, elm) => {
        $(elm).on("click",  (e) => {
            deleteService($(elm))
        })
    })
    $('.updateBtn').each((i, elm) => {
        $(elm).on("click",  (e) => {
            updateService($(elm))
        })
    })
    }
});

function deleteService(el){
    serviceId  =  $(el).data('id')
    $.ajax({
        url:  `/service/${serviceId}`,
        type:  'delete',
        dataType:  'json',
        success:  function (data) {
            $(el).parents()[1].remove()
        }
    });
}

function newService(){
    let form = `<br><br>
           <form action="service" method="post">
                <label for="name">Name: </label>
                <input type="text" class="form-control" name="name"><br>
                <label for="time_type">Time Type: </label>
                <input type="text" class="form-control" name="time_type"><br>
                <label for="rate">Rate:</label>
                <input type="text" class="form-control" name="rate"><br>
                <button type="submit" class="btn btn-primary">New</button>&nbsp;<button type="reset" class="btn btn-primary">Clear</button>&nbsp;<button type="reset" class="btn btn-primary" onclick="cancelForm()">Cancel</button>
            </form>`;
    $('#serviceform').empty()
    $('#serviceform').append(form)
}

function updateService(el){
    serviceId  =  $(el).data('id')
    $.ajax({
        url:  `/service/${serviceId}`,
        type:  'get',
        dataType:  'json',
        success:  function (data) {
            let form = `<br><br>
                   <form action="service/${serviceId}" method="post">
                        <label for="name">Name: </label>
                        <input type="text" class="form-control" name="name" value="${data.name}"><br>
                        <label for="time_type">Time Type: </label>
                        <input type="text" class="form-control" name="time_type" value="${data.time_type}"><br>
                        <label for="rate">Rate:</label>
                        <input type="text" class="form-control" name="rate" value="${data.rate}"><br>
                        <button type="submit" class="btn btn-primary">Update</button>&nbsp;<button type="reset" class="btn btn-primary">Clear</button>&nbsp;<button type="reset" class="btn btn-primary" onclick="cancelForm()">Cancel</button>
                    </form>`;
            $('#serviceform').empty()
            $('#serviceform').append(form)
        }
    });
}

function cancelForm() {
    $('#serviceform').empty();
}
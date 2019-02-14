$.ajax({
    url:  '/service/list',
    type:  'get',
    dataType:  'json',
    success: function  (data) {
        let rows =  '';
        data.services.forEach(service => {
        rows += `
        <tr id="tr_${service.id}">
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
    bindBtn();
    }
});

function bindBtn() {
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
           <div>
                <label for="name">Name: </label>
                <input id="form_name" type="text" class="form-control" name="name"><br>
                <label for="time_type">Time Type: </label>
                <input id="form_time_type" type="text" class="form-control" name="time_type"><br>
                <label for="rate">Rate:</label>
                <input id="form_rate" type="text" class="form-control" name="rate"><br>
                <button class="btn submitBtn btn-primary">New</button>&nbsp;<button type="reset" class="btn btn-primary">Clear</button>&nbsp;<button type="reset" class="btn btn-primary" onclick="cancelForm()">Cancel</button>
            </div>`;
    $('#serviceform').empty();
    $('#serviceform').append(form);
    $('.submitBtn').bind("click", () => {
        let name = $('#form_name').val();
        let time_type = $('#form_time_type').val();
        let rate = $('#form_rate').val();
        ajaxNewService({
            'name': name,
            'time_type': time_type,
            'rate': rate
        })
    });
}

function ajaxNewService(service) {
    $.ajax({
        url:  '/service/list',
        type:  'post',
        dataType:  'json',
        data: service,
        success:  function (data) {
            let row = `<tr id="tr_${data.id}">
                            <td class="col-1">${data.name}</td>
                            <td class="col-3">${data.time_type}</td>
                            <td class="col-1">${data.rate}</td>
                            <td class="col-3">
                                <button class="btn deleteBtn btn-primary" data-id="${data.id}"">Delete</button>
                                <button class="btn updateBtn btn-primary" data-id="${data.id}"">Update</button>
                            </td>
                        </tr>`;
            $('#myTable tbody').append(row);
            $('#serviceform').empty();
            bindBtn();
        }
    });
}

function updateService(el){
    serviceId  =  $(el).data('id')
    $.ajax({
        url:  `/service/${serviceId}`,
        type:  'get',
        dataType:  'json',
        success:  function (data) {
            let form = `<br><br>
                   <div>
                        <input id="form_id" type="hidden" value="${data.id}">
                        <label for="name">Name: </label>
                        <input id="form_name" type="text" class="form-control" name="name" value="${data.name}"><br>
                        <label for="time_type">Time Type: </label>
                        <input id="form_time_type" type="text" class="form-control" name="time_type" value="${data.time_type}"><br>
                        <label for="rate">Rate:</label>
                        <input id="form_rate" type="text" class="form-control" name="rate" value="${data.rate}"><br>
                        <button class="btn submitBtn btn-primary">Update</button>&nbsp;<button type="reset" class="btn btn-primary">Clear</button>&nbsp;<button type="reset" class="btn btn-primary" onclick="cancelForm()">Cancel</button>
                    </div>`;
            $('#serviceform').empty()
            $('#serviceform').append(form)
            $('.submitBtn').bind("click", () => {
                let id = $('#form_id').val();
                let name = $('#form_name').val();
                let time_type = $('#form_time_type').val();
                let rate = $('#form_rate').val();
                ajaxUpdateService({
                    'id': id,
                    'name': name,
                    'time_type': time_type,
                    'rate': rate
                }, el)
            });
        }
    });
}


function ajaxUpdateService(service, elm) {
    $.ajax({
        url:  `/service/${service.id}`,
        type:  'post',
        dataType:  'json',
        data: service,
        success:  function (data) {
            let rows =  '';
            data.services.forEach(service => {
                rows += `
                <tr id="tr_${service.id}">
                    <td class="col-1">${service.name}</td>
                    <td class="col-3">${service.time_type}</td>
                    <td class="col-1">${service.rate}</td>
                    <td class="col-3">
                        <button class="btn deleteBtn btn-primary" data-id="${service.id}"">Delete</button>
                        <button class="btn updateBtn btn-primary" data-id="${service.id}"">Update</button>
                    </td>
                </tr>`;
            });
            $('#myTable tbody').empty();
            $('#myTable tbody').append(rows);
            $('#serviceform').empty();
            bindBtn();
        }
    });
}

function cancelForm() {
    $('#serviceform').empty();
}
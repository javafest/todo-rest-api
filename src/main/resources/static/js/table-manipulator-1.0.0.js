function removeTask(taskList, id) {
    return jQuery.grep(taskList, function (item) {
        return item.id != id;
    });
}

function destroyTable() {
    var body = document.getElementsByTagName('tbody')[0];
    while (body.firstChild) {
        body.removeChild(body.lastChild);
    }
}

function populateTable(tableData) {
    var body = document.getElementsByTagName('tbody')[0];
    for (var i = 0; i < tableData.length; i++) {
        var task = tableData[i];
        var tr = document.createElement('tr');

        appendTextNode(tr, (i + 1).toString());
        appendTextNode(tr, task.name);
        appendLinkNode(tr, task.id, true);
        appendLinkNode(tr, task.id, false);

        body.appendChild(tr);
    }
}

function appendTextNode(tableRow, text) {
    var td = document.createElement('td');
    td.appendChild(document.createTextNode(text));
    tableRow.appendChild(td);
}

function appendLinkNode(tableRow, id, editColumn) {
    var td = document.createElement('td');
    var a = document.createElement('a');

    if (editColumn) {
        a.href = '/task/edit?id=' + id;
        a.innerHTML = '<i class="fas fa-pen"></i>';
    } else {
        a.className = 'link';
        a.innerHTML = '<i class="fas fa-trash-alt"></i>';
        a.onclick = function () {
            ajaxDeleteAction(id);
        };
    }

    td.appendChild(a);
    tableRow.appendChild(td);
}
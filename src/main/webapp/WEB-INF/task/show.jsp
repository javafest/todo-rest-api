<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%--
  User: erfan
  Date: 4/16/21
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Task List</title>

    <meta name="${_csrf.parameterName}" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="/js/util-1.0.0.js"></script>
    <script type="text/javascript" src="/js/table-manipulator-1.0.0.js"></script>
</head>
<body>

<div class="container">
    <div class="row content">

        <div class="col-md-4"></div>

        <div class="col-md-4">
            <div class="mb-3">
                <legend style="margin-bottom: 10%">
                    <a href="/auth/welcome" class="link">
                        Todo <i class="fas fa-home"></i>
                    </a>
                </legend>

                <form id="task-form" class="row g-3">

                    <div class="col-md-8">
                        <input id="task-name"
                               type="text"
                               name="name"
                               class="form-control"
                               placeholder="what to do.."
                               autofocus="true"/>

                    </div>

                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary mb-3">
                            <i class="fas fa-plus-circle"></i>
                        </button>
                    </div>

                    <div style="margin-top: 5px">
                        <span class="alert alert-danger" style="display: none"></span>
                    </div>

                </form>

                <div class="row">
                    <div class="mb-3 offset-md-4">
                        <c:out value="OR"/>
                    </div>
                </div>

                <form id="fileUploadForm" enctype="multipart/form-data" method="POST" class="row g-4">

                    <div class="col-md-8">
                        <input id="loadFileBtn"
                               class="form-control"
                               type="button"
                               value="Upload a photo"
                               onclick="document.getElementById('file').click();"/>

                        <input id="file"
                               type="file"
                               name="file"
                               class="form-control"
                               onChange="ajaxUploadAction()"
                               style="display: none"/>
                    </div>

                    <div class="col-md-2" style="display: none">
                        <button id="convertBtn" class="btn btn-primary">
                            <i class="fas fa-sync"></i>
                        </button>
                    </div>
                </form>
            </div>

            <div class="mb-3 col-md-10">
                <legend>My tasks</legend>

                <c:if test="${taskList.size() == 0}">
                    <div class="alert alert-info" role="alert">
                        You have no tasks!
                    </div>
                </c:if>

                <table class="table">
                    <thead>
                    <tr>
                        <th>Sl.</th>
                        <th>Task</th>
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                    </thead>

                    <tbody></tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
    var $convertBtn = $('div:hidden'),
        $loadFileBtn = $('#loadFileBtn'),
        taskContent = "",
        tableData = [];

    $('#task-form').submit(function (e) {
        e.preventDefault();
        var data = $(this).serializeArray();
        $(this).trigger("reset");

        ajaxPostAction(JSON.stringify(getFormData(data)));
    });

    function ajaxGetAllAction() {
        $.ajax({
            url: '/api/v1/tasks',
            type: 'GET',
            beforeSend: setRequestHeader,
            success: function (data) {
                tableData = data._embedded.taskList;
                destroyTable();
                populateTable(tableData);
            },
            error: function () {
                alert('Couldn\'t fetch data!');
            }
        });
    }

    function ajaxPostAction(data) {
        $.ajax({
            url: '/api/v1/tasks',
            type: 'POST',
            data: data,
            contentType: 'application/json',
            beforeSend: setRequestHeader,
            success: function (data) {
                tableData.push(data);
                destroyTable();
                populateTable(tableData);
            },
            error: function (data) {
                var response = JSON.parse(data.responseText);
                var $error = $('.alert:hidden');
                $error.toggle();
                $error.text(response.errors[0]);
            }
        });
    }

    function ajaxDeleteAction(id) {
        if (confirm("Are you sure, you want to delete it?")) {
            $.ajax({
                url: '/api/v1/tasks/' + id,
                type: 'DELETE',
                beforeSend: setRequestHeader,
                success: function () {
                    tableData = removeTask(tableData, id);
                    destroyTable();
                    populateTable(tableData);
                },
                error: function () {
                    alert("Error while deleting!");
                }
            });
        }
    }

    function ajaxUploadAction() {
        var data = new FormData($('#fileUploadForm')[0]);
        $loadFileBtn.val("Uploading...");
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/api/v1/image",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            beforeSend: setRequestHeader,
            success: function (data) {
                taskContent = data;
                $loadFileBtn.val("Upload Complete!")
                $convertBtn.show();
            },
            error: function () {
                alert('Error while uploading photo!');
                $loadFileBtn.val("Upload a photo to detect text");
            }
        });
    }

    $('#convertBtn').click(function (e) {
        e.preventDefault();
        $("#task-name").val(taskContent);
        $loadFileBtn.val("Upload a photo to detect text");
    });

    ajaxGetAllAction();
</script>
</body>
</html>

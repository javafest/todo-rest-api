<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%--
  User: erfan
  Date: 4/17/21
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Task Edit</title>

    <meta name="${_csrf.parameterName}" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="/js/util-1.0.0.js"></script>
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

                <form id="task-form">
                    <h5>Edit task</h5>

                    <div class="mb-3">
                        <div>
                            <input id="task-name"
                                        type="text"
                                        name="name"
                                        class="form-control"
                                        autofocus="true"/>

                        </div>
                    </div>

                    <button class="btn btn-lg btn-primary btn-block" type="submit">Update</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    $(function () {
        var id = ${id};
        ajaxGetAction(id);

        $('#task-form').submit(function (e) {
            e.preventDefault();
            var data = $(this).serializeArray();

            ajaxUpdateAction(getFormData(data));
        });

        function ajaxGetAction(id) {
            $.ajax({
                url: '/tasks/' + parseInt(id),
                type: 'GET',
                beforeSend: setRequestHeader,
                success: function (data) {
                    $('#task-name').val(data.name);
                },
                error: function () {
                    alert('Couldn\'t fetch data!');
                }
            });
        }

        function ajaxUpdateAction(data) {
            $.ajax({
                url: '/tasks/' + id,
                type: 'PUT',
                data: JSON.stringify(data),
                contentType: 'application/json',
                beforeSend: setRequestHeader,
                success: function () {
                    window.location.replace('http://localhost:8080/task/show');
                },
                error: function (data) {
                }
            });
        }
    });
</script>
</body>
</html>

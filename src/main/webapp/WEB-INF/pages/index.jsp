<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <script src="<%=basePath%>src/angular.min.js"></script>
    <script src="<%=basePath%>src/tm.pagination.js"></script>
    <script src="<%=basePath%>src/ui-bootstrap-tpls.min.js"></script>
    <link rel="stylesheet" href="<%=basePath%>src/bootstrap.min.css">
    <link rel="stylesheet" href="<%=basePath%>src/css/demo.css">
    <link rel="stylesheet" href="<%=basePath%>src/css/font-awesome.min.css">
    <link rel="stylesheet" href="<%=basePath%>src/css/normalize.css">
    <link rel="stylesheet" href="<%=basePath%>src/css/style.css">
    <script src="<%=basePath%>src/jquery.min.js"></script>
    <script src="<%=basePath%>src/bootstrap.min.js"></script>
    <SCRIPT language=JavaScript src="<%=basePath%>src/Player.js"></SCRIPT>
    <style>
        .page-list .pagination {
            float: left;
        }
        .page-list .pagination span {
            cursor: pointer;
        }
        .page-list .pagination .separate span {
            cursor: default;
            border-top: none;
            border-bottom: none;
        }
        .page-list .pagination .separate span:hover {
            background: none;
        }
        .page-list .page-total {
            float: left;
            margin: 25px 20px;
        }
        .page-list .page-total input, .page-list .page-total select {
            height: 26px;
            border: 1px solid #ddd;
        }
        .page-list .page-total input {
            width: 40px;
            padding-left: 3px;
        }
        .page-list .page-total select {
            width: 50px;
        }
    </style>
    <style type="text/css">
        table.hovertable {
            font-family: verdana,arial,sans-serif;
            font-size:11px;
            color:#333333;
            border-width: 1px;
            border-color: #999999;
            border-collapse: collapse;
        }
        table.hovertable th {
            background-color:#c3dde0;
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #a9c6c9;
        }
        table.hovertable tr {
            background-color:#d4e3e5;
        }
        table.hovertable td {
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #a9c6c9;
        }
    </style>
</head>
<body>
<div ng-app="myApp" ng-controller="customersCtrl">
    <%--搜索框--%>
        <div class="search d1">
            <form>
                <input type="text" ng-model="fastname" placeholder="搜索从这里开始...">
                <button ng-click="sousuo()" style="width: 80px">搜索</button>
            </form>
        </div>
    <table class="hovertable">
        <tr>
            <th style="width: 15%">名称</th>
            <th style="width: 40%">先锋播放地址</th>
            <th style="width: 15%">封面图</th>
            <th style="width: 15%">在线播放</th>
        </tr>
        <tr ng-repeat="x in names" onmouseover="this.style.backgroundColor='#ffff66';" onmouseout="this.style.backgroundColor='#d4e3e5';">
            <td>{{ x.name }}</td>
            <td>{{ x.urlavi }}</td>
            <td><button class="btn btn-primary btn-lg" ng-click="openModal(x.urlimg)">点击浏览图片</button></td>
            <td>
            <form action="<%=basePath%>play" method="get">
                <input type="text" name="url" value="{{x.urlavi}}" style="display: none"/>
                <input type="submit" class="btn btn-primary btn-lg" value="在线播放" >
            </form>
            </td>
        </tr>
    </table>
    <tm-pagination conf="paginationConf"></tm-pagination>
        <!-- 模态框（Modal） -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
            <div class="modal-dialog" style="width: 850px">
                <div class="modal-content">
                    <div class="modal-body"><img style="width: 800px" src="{{img}}"></div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary">提交更改</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        </div>
<script type="text/javascript">
    var app = angular.module('myApp', ['tm.pagination','ui.bootstrap']);
    app.controller('customersCtrl', ['$scope', 'BusinessService','$http', function ($scope,BusinessService,$http) {
        var GetAllEmployee = function () {
            var postData = {
                pageIndex: $scope.paginationConf.currentPage,
                pageSize: $scope.paginationConf.itemsPerPage,
                name:$scope.fastname
            }
            BusinessService.list(postData).success(function (response) {
                $scope.paginationConf.totalItems = response.total;
                $scope.names = response.list
            });
        }
        $scope.openModal = function (openModal) {
            $scope.img=openModal;
            $('#myModal').modal('show')
        }
        $scope.sousuo = function () {
            var postData={
                name:$scope.fastname,
                pageIndex: $scope.paginationConf.currentPage,
                pageSize: $scope.paginationConf.itemsPerPage
            }
            $http.post('<%=basePath%>selectall', postData).success(function (abc) {
                $scope.paginationConf.totalItems = abc.total;
                $scope.names = abc.list
            })
        }
        //配置分页基本参数
        $scope.paginationConf = {
            currentPage: 1,
            itemsPerPage: 20,
            perPageOptions: [10, 20, 30, 40, 50]
        };
        $scope.$watch('paginationConf.currentPage + paginationConf.itemsPerPage', GetAllEmployee);
    }]);
    app.factory('BusinessService', ['$http', function ($http) {
        var list = function (postData) {
            return $http.post('<%=basePath%>selectall', postData);
        }
        return {
            list: function (postData) {
                return list(postData);
            }
        }
    }]);
</script>
</body>
</html>
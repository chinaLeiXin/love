<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>播放界面</title>
    <meta charset="utf-8">
    <SCRIPT language=JavaScript src="<%=basePath%>src/Player.js"></SCRIPT>
</head>
<body>
<!-- 播放框（Modal） -->
<div class="modal fade" id="PlayModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
    <div class="modal-dialog" style="width: 900px">
        <div class="modal-content">
            <div class="modal-body">
                <table style="BACKGROUND-COLOR: #000000" border=0 cellSpacing=0 cellPadding=0 align=center>
                    <td id=tdxfplay width="900" height="550">
                        <iframe id=xframe_mz name=xframe_mz style="MARGIN: 0px; WIDTH: 100%; DISPLAY: none; HEIGHT: 100%" src="http://error.xfplay.com/error.htm" frameBorder=0 scrolling=no></iframe>
                        <!--先锋播放器-->
                        <object id=Xfplay name=Xfplay
                                onerror="document.getElementById('Xfplay').style.display='none';document.getElementById('xframe_mz').style.display='';document.getElementById('xframe_mz').src='http://error.xfplay.com/error.htm';"
                                CLASSID="CLSID:E38F2429-07FE-464A-9DF6-C14EF88117DD" width="900" height="550">
                            <!--IE 浏览器-->
                            <param NAME="URL" value="${url}" width="900" height="550">
                            <!--非IE浏览器 火狐，谷歌 等浏览器-->
                            <embed type="application/xfplay-plugin" id="Xfplay2" name="Xfplay2" PARAM_URL="${url}"  width="900" height="550"></embed>
                        </object>
                    </td>
                </table>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
</html>

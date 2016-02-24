<%@page language="java" pageEncoding="gb2312"%>
<html>
<head>
<script type="text/javascript" src="jquery-2.1.3.js"></script>
<script src="jquery.cookie.js" type="text/javascript"></script>
<title>用户登录页面</title>
</head>
<body>
	<script type="text/javascript">
		$(function(){
			$("#login").click(function(){
				saveUserName();
			});
		});
		function saveUserName() {
			var username = $('#username').val();
			$.cookie("username", username);
		};
	</script>
	<form action="Login" method="post">
		<table>
			<tr>
				<td colspan="2"><s:actionerror /></td>
			</tr>
			<tr>
				<td>用户名：</td>
				<td><input type="text" name="username" id="username" /></td>
			</tr>
			<tr>
				<td>密码：</td>
				<td><input type="password" name="upassword" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="登录"  id="login" /></td>
			</tr>
		</table>
	</form>
</body>
</html>
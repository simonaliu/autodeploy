<%@page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<script type="text/javascript" src="jquery-2.1.3.js"></script>
<script src="jquery.cookie.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#username").attr("value", getUserName());
	});

	function getUserName() {
		console.log($.cookie('username'));
		return $.cookie('username');
	};
</script>
<title>用户信息</title>
</head>

<body>
	<p>登录成功</p>
	用户名${username}已登录！
	<p></p>
	<p>请上传您需要运行的war包</p>
	<form action="FileUpload" method="post" enctype="multipart/form-data">
		<span>请上传war包</span> <input type="file" id="war_file" name="war_file">
		<p></p>
		<span>请输入想要运行的实例数</span> <input type="text" id="ins_num"
			name="ins_num" size="5px;">
		<p></p>
		<input type="text" name="username" id="username" style="display: none">
		<input type="submit" value="上传" id="submit">
	</form>
</body>
</html>
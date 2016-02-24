<%@page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<title>文件上传成功</title>

<script type="text/javascript" src="jquery-2.1.3.js"></script>
<script src="jquery.cookie.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="css.css">
<link rel="stylesheet" type="text/css" href="progressbar.css">
<script type="text/javascript">
	var interval;
	$(document).ready(function() {
		$("#loading> div").hide()
		interval = setInterval("getCurrent()", 1000);
	});
	
	function getCurrent() {
		var data = {username:$.cookie('username')};
		$.ajax({
			type : 'POST',
			url : '/AutoDeploy/dockerDeploy',
			dataType : 'text',
			data : data,
			success : function(response, status) {
				console.log(response);
				var obj = eval("(" + response + ")");
				if(obj.progress>0){
					$("#loading> div").show();
				}
				$('#counter').html(obj.progress);
				SetProgress(obj.progress);
				$('.ins_num').html("已成功上传"+ obj.current+"实例数" );
				$('#time_cost').html("耗时：" + obj.time + "s");
				if (obj.progress >= 100){
					clearInterval(interval);
				}
			}
		});
	};

	function SetProgress(progress) {
		if (progress) {
			$("#loading> div").css("width", String(progress) + "%"); //控制#loading div宽度 
			$("#loading > div").html(String(progress) + "%"); //显示百分比 
		}
	}
</script>
</head>
<body>
	<p id="message">${message}</p>
	<div class="wrapper">
		<div id="center">
			<div id="message"></div>
			<div id="loading">
				<div></div>
			</div>
		</div>
		<div class="ins_num"></div>
		<div id="time_cost" style="margin-top:10px;"></div>
	</div>
</body>
</html>
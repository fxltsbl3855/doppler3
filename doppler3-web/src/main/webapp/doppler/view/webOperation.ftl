<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "doppler/common_doppler.ftl">
<style type="text/css">
#exceptionTable .link-info{
display:none;
}
#exceptionTable .active .link-info{
display:block;
/* padding-left: 75%; */
}

</style>
</head>
<body>
<!-- Header Start -->
<#include "doppler/top.ftl">
<!-- Header End -->
<!-- Page-sidebar Start -->
<#include "doppler/menu.ftl">
<!-- Page-sidebar End -->
<div class="page-content">
	<!-- Web操作列表页面	start-->
	<div id="mainDiv">
		<!-- Content Start -->
		<ol class="breadcrumb">
			<li><a href="javascript:void(0)">首页</a></li>
			<li class="active">WEB操作查询</li>
		</ol>
		<div class="padd">
			<form class="form-horizontal">
				<div class="row">
					<div class="col-xs-6 col-md-4">
						<label class="col-sm-4" style="line-height:30px;">开始日期：</label>
						<div class="col-sm-7 input-group date form_datetime" data-date-format="yyyy-MM-dd hh:mm" data-link-field="dtp_start_time" data-initialDate="new Date()">
							<input class="form-control" size="16" type="text" readonly id="startDate"><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						</div>
					</div>
					<div class="col-xs-6 col-md-4">
						<label class="col-sm-4" style="line-height:30px;">结束日期：</label>
						<div class="col-sm-7 input-group date form_datetime" data-date-format="yyyy-MM-dd hh:mm" data-link-field="dtp_end_time" data-initialDate="new Date()">
							<input class="form-control" size="16" type="text" readonly id="endDate"> <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						</div>
					</div>
					<div class="col-xs-6 col-md-4">
						<label class="col-sm-4" style="line-height:30px;">&nbsp;&nbsp;&nbsp;应用名：</label>
						<select class="sino-select" id="appName" style="height: 30px;width: 200px" data-url="${base}/doppler/web/getWebAppList.shtml" onchange="WebOperation.appSelectChangeHandler()">
	 					</select>
					</div>
				</div>
				<div class="row" style="margin-top: 5px">
					<div class="col-xs-6 col-md-5">
						<label class="col-sm-4" style="line-height:30px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;URL：</label>
						<div class="input-group">
							<select class="sino-select" id="moduleName" style="height: 30px;width: 250px;margin-left:-32px;" data-url="" data-url-params="appName">
							</select>
						</div>
					</div>
					<div class="col-xs-6 col-md-4" style="margin-left:-97px;">
						<label class="col-sm-4" style="padding-left: 42px;line-height:30px;">账号：</label>
						<input id="username" name="username" style="width: 200px;" type="text" >
					</div>
					<div class="col-xs-6 col-md-3">
						<button class="btn btn-primary" style="margin-left:217px;width: 100px;" type="button"
							onclick="WebOperation.queryHandler()">查询</button>
					</div>
				</div>
			</form>
			<div style="height:5px;"></div>
			<table id="WebOperationTable" data-toggle="table" class="table table-striped table-condensed"
				data-unique-id="id">
				<thead>
					<tr>
						<th data-field="reqTime">请求时间</th>
						<th data-field="appName">应用名称</th>
						<th data-field="host">主机IP</th>
						<th data-field="brower">浏览器</th>
						<th data-field="username">账号</th>
						<th data-field="className">请求URL</th>
						<th data-field="time">耗时(ms)</th>
						<th data-field="code">HTTP Code</th>
						<th data-formatter="WebOperation.detailHandler"><span style="color:white;" id="detailSpan">明细</span></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<!-- Web操作列表页面	 end -->
	<!-- Web操作明细列表页面	 start -->
	<div id="detailWebOperationDiv" style="display: none">
		<!-- Content Start -->
		<ol class="breadcrumb">
			<li><a href="javascript:void(0)">首页</a></li>
			<li><a href="javascript:void(0)" onclick="WebOperation.showMainHandler()">WEB操作查询</a></li>
			<li class="active">详细信息</li>
		</ol>
		<div class="padd">
			<div style="margin-top:10px;">
				<div style="font-size:18px;font-weight:bold;">参数信息</div>
				<div id="reqParam" style="margin-top:10px;background-color:#F0F0F0;word-wrap:break-word;line-height:25px;"></div>
			</div>
			<hr>
			<div style="margin-top:10px;">
				<div style="font-size:18px;font-weight:bold;">Header</div>
				<div id="reqHeader" style="margin-top:10px;background-color:#F0F0F0;word-wrap:break-word;line-height:25px;"></div>
			</div>
		</div>
	</div>
	<!-- Web操作明细列表页面	 end -->
</div>

<script type="text/javascript" src="${base}/doppler/js/webOperation.js"></script>
</body>
<script type="text/javascript">
	var currentDate = new Date();
	$('#startDate').val(currentDate.Format("yyyy-MM-dd") + " 00:00");
	currentDate.setHours(23);
	currentDate.setMinutes(59);
	$('#endDate').val(currentDate.Format("yyyy-MM-dd hh:mm"));
	$('.form_datetime').datetimepicker({
		language : 'zh-CN',
		format : "yyyy-mm-dd hh:ii",
		weekStart : 7,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 0,
		forceParse : 0,
		//minView: "month",//此项删除可选择时间
		pickerPosition : "bottom-left"
	});
</script>

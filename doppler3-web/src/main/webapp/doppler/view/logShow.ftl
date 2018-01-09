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
			<li class="active">业务对象查询</li>
		</ol>
		<div class="padd">
			<form class="form-horizontal">
				<div class="row">
					<div class="col-xs-4 col-md-3" style="width:380px;">
						<label class="col-sm-4" style="line-height:30px;width:120px;">操作时间从：</label>
						<div class="col-sm-7 input-group date form_datetime" data-date-format="yyyy-MM-dd hh:mm:ss" data-link-field="dtp_start_time" data-initialDate="new Date()">
							<input class="form-control" size="16" type="text" readonly id="startDate"><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						</div>
					</div>
					<div class="col-xs-4 col-md-3" style="width:380px;">
						<label class="col-sm-2" style="line-height:30px;">到</label>
						<div class="col-sm-7 input-group date form_datetime" data-date-format="yyyy-MM-dd hh:mm:ss" data-link-field="dtp_end_time" data-initialDate="new Date()" style="margin-left: 70px;">
							<input class="form-control" size="16" type="text" readonly id="endDate"> <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						</div>
					</div>
					<div class="col-xs-4 col-md-3" style="width:350px;margin-left: -60px;">
							<label class="col-sm-2" style="line-height:30px;width:100px;">&nbsp;&nbsp;&nbsp;应用名：</label>
							<select class="sino-select" id="appName" style="height: 33px;width: 215px;" data-url="">
		 					</select>
					</div>
					
				</div>
				<div class="row" style="margin-top: 5px">
					<div class="col-xs-4 col-md-3" style="width:350px;">
							<label class="col-sm-3" style="line-height:30px;width:120px;">&nbsp;&nbsp;&nbsp;操作类型：</label>
							<select class="sino-select" id="opType" style="height: 30px;width: 200px" data-url="" >
		 					</select>
						</div>
					<div class="col-xs-4 col-md-3" style="width:350px;">
						<label class="col-sm-3" style="line-height:30px;width:100px;">操作对象：</label>
						<div class="input-group">
							<select class="sino-select" id="opObj" style="height: 30px;width: 200px;" data-url="" data-url-params="opType">
							</select>
						</div>
					</div>
					<div class="col-xs-4 col-md-3" style="width:350px;">
						<label class="col-sm-3" style="line-height:30px;width:100px;">业务字段：</label>
						<input id="busField" name="busField" style="width: 215px;height: 30px;" type="text" placeholder="e.g : key1=value1,key2=value2">
					</div>
					
					<div class="col-xs-4 col-md-3" style="width:150px;">
						<button class="btn btn-primary" style="width: 100px;" type="button"
							onclick="LogShow.queryHandler()">查询</button>
					</div>
				</div>
			</form>
			<div style="height:5px;"></div>
			<table id="LogShowTable" data-toggle="table" class="table table-striped table-condensed"
				data-unique-id="id">
				<thead>
					<tr>
						<th data-field="timestamp" data-formatter="LogShow.formatterTime">操作时间</th>
						<th data-field="host">主机IP</th>
						<th data-field="appName">应用名</th>
						<th data-field="opType">操作类型</th>
						<th data-field="opObj">操作对象</th>
						<th data-field="busParam">业务字段</th>
						<th data-field="opRes">操作结果</th>
						<th data-field="time">耗时(ms)</th>
						<th data-formatter="LogShow.detailHandler"><span style="color:white;" id="detailSpan">明细</span></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<!-- Web操作列表页面	 end -->
	<!-- Web操作明细列表页面	 start -->
	<div id="detailLogShowDiv" style="display: none">
		<!-- Content Start -->
		<ol class="breadcrumb">
			<li><a href="javascript:void(0)">首页</a></li>
			<li><a href="javascript:void(0)" onclick="LogShow.showMainHandler()">业务对象查询</a></li>
			<li class="active">业务对象详细信息</li>
		</ol>
		<div class="padd">
						<form class="form-horizontal">
				<div class="row">
					<div class="col-xs-6 col-md-12">
						<label class="col-sm-1">操作时间:</label>
						<label class="col-sm-2" id="timestampD" style="margin-left:-30px;"></label>
						<label class="col-sm-1">应用名称:</label>
						<label class="col-sm-2" id="appNameD" style="margin-left:-30px;"></label>
						<label class="col-sm-1">操作类型:</label>
						<label class="col-sm-1" id="opTypeD" style="margin-left:-30px;"></label>
						<label class="col-sm-1">操作对象:</label>
						<label class="col-sm-1" id="opObjD" style="margin-left:-30px;"></label>
						<label class="col-sm-1">主机IP:</label>
						<label class="col-sm-2" id="hostD" style="margin-left:-30px;"></label>
						
					</div>
				</div>
			</form>
			<hr>
			<div style="margin-top:10px;">
				<div style="font-size:18px;font-weight:bold;">方法参数</div>
				<div id="requstParam" style="margin-top:10px;background-color:#F0F0F0;word-wrap:break-word;line-height:25px;"></div>
			</div>
			<div style="margin-top:10px;">
				<div style="font-size:18px;font-weight:bold;">业务参数</div>
				<div id="busParam" style="margin-top:10px;background-color:#F0F0F0;word-wrap:break-word;line-height:25px;"></div>
			</div>
		</div>
	</div>
	<!-- Web操作明细列表页面	 end -->
</div>

<script type="text/javascript" src="${base}/doppler/js/logShow.js"></script>
</body>
<script type="text/javascript">
	var currentDate = new Date();
	$('#startDate').val(currentDate.Format("yyyy-MM-dd") + " 00:00:00");
	currentDate.setHours(23);
	currentDate.setMinutes(59);
	$('#endDate').val(currentDate.Format("yyyy-MM-dd hh:59:59"));
	$('.form_datetime').datetimepicker({
		language : 'zh-CN',
		format : "yyyy-mm-dd hh:ii:ss",
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

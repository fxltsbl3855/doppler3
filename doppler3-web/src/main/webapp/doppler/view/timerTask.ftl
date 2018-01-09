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
	<!-- 定时任务列表页面	start-->
	<div id="mainDiv">
		<!-- Content Start -->
		<ol class="breadcrumb">
			<li><a href="javascript:void(0)">首页</a></li>
			<li class="active">定时任务</li>
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
						<select class="sino-select" id="appName" style="height: 30px;width: 200px" data-url="${base}/doppler/job/getAppList.shtml" onchange="TimerTask.appSelectChangeHandler()">
	 					</select>
					</div>
				</div>
				<div class="row" style="margin-top: 5px">
					<div class="col-xs-6 col-md-5">
						<label class="col-sm-4" style="line-height:30px;">&nbsp;&nbsp;&nbsp;模块名：</label>
						<div class="input-group">
							<select class="sino-select" id="moduleName" style="height: 30px;width: 250px;margin-left:-32px;" data-url="" data-url-params="appName">
							</select>
						</div>
					</div>
					<div class="col-xs-6 col-md-4" style="margin-left:-97px;">
						<label class="col-sm-4" style="line-height:30px;">执行结果：</label> 
						<select class="sino-select" id="result" style="height: 30px;width: 200px" data-url-params="result">
							<option value="-1">请选择</option>
							<option value="SUCCESS">成功</option>
							<option value="EXCEPTION">异常</option>
							<option value="UNKNOWN">未知</option>
	 					</select>
					</div>
					<div class="col-xs-6 col-md-3">
						<button class="btn btn-primary" style="margin-left:217px;width: 100px;" type="button"
							onclick="TimerTask.queryHandler()">查询</button>
					</div>
				</div>
			</form>
			<div style="height:5px;"></div>
			<table id="timerTaskTable" data-toggle="table" class="table table-striped table-condensed"
				data-unique-id="id">
				<thead>
					<tr>
						<th data-field="appName">应用</th>
						<th data-field="host">主机IP</th>
						<th data-field="className" data-formatter="TimerTask.taskNameFormatter">任务名</th>
						<th data-field="result" data-formatter="TimerTask.resultFormatter">执行结果</th>
						<th data-field="executeNum">执行次数</th>
						<th data-field="maxTime">最大耗时(ms)</th>
						<th data-field="minTime">最小耗时(ms)</th>
						<th data-field="avgTime">平均耗时(ms)</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<!-- 定时任务列表页面	 end -->
	<!-- 定时任务明细列表页面	 start -->
	<div id="detailTimerTaskDiv" style="display: none">
		<!-- Content Start -->
		<ol class="breadcrumb">
			<li><a href="javascript:void(0)">首页</a></li>
			<li><a href="javascript:void(0)" onclick="TimerTask.showMainHandler()">定时任务</a></li>
			<li class="active">明细(<label id="titleValue"></label>)</li>
		</ol>
		<div class="padd">
			<form class="form-horizontal">
				<div class="row">
					<div class="col-xs-6 col-md-7">
						<label class="col-sm-2">时间段:</label>
						<label class="col-sm-3" id="detailStartDate" style="margin-left:-40px;"></label>
						<label class="col-sm-1">到</label>
						<label class="col-sm-3" id="detailEndDate"></label>
					</div>
				</div>
				<div class="row" style="margin-top: 10px">
					<div class="col-xs-6 col-md-5">
						<label class="col-sm-3">应用名：</label>
						<label class="col-sm-5" id="detailAppName" style="margin-left:-40px;"></label>
						<label class="col-sm-3">任务名：</label> 
						<label class="col-sm-3" id="detailClassName" style="margin-left:-40px;"></label>
					</div>
					<div class="col-xs-6 col-md-5" id="detailResultDiv" style="margin-left:150px;">
						<label class="col-sm-3">状态：</label> 
						<label class="col-sm-3" id="detailResult"  style="margin-left:-40px;"></label>
					</div>
				</div>
			</form>
	        <table id="timerTaskDetailTable" data-toggle="table">
				<thead>
					<tr>
						<th data-field="executeTime">执行时间</th>
						<th data-field="appName">应用名称</th>
						<th data-field="host">主机IP</th>
						<th data-field="className">任务名</th>
						<th data-field="result" data-formatter="TimerTask.detailResultFormatter">执行结果</th>
						<th data-field="param">执行参数</th>
						<th data-field="time">耗时(ms)</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<!-- 定时任务明细列表页面	 end -->
</div>

<script type="text/javascript" src="${base}/doppler/js/timerTask.js"></script>
</body>
<script type="text/javascript">
var currentDate = new Date().Format("yyyy-MM-dd");
$('#startDate').val(currentDate + " 00:00");
$('#endDate').val(currentDate + " 23:59");
$('.form_datetime').datetimepicker({
    language:  'zh-CN',
    format: "yyyy-mm-dd hh:ii",
    weekStart: 7,
    todayBtn:  1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    forceParse: 0,
    //minView: "month",//此项删除可选择时间
    pickerPosition: "bottom-left"
  });
</script>

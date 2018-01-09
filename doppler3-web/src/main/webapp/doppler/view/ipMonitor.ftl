<!DOCTYPE html>
<head>
	<#include "doppler/common_doppler.ftl">
</head>
<body>
	<!-- Header Start -->
	<#include "doppler/top.ftl">
	<!-- Header End -->
	<!-- Page-sidebar Start -->
	<#include "doppler/menu.ftl">
	<!-- Page-sidebar End -->
	<div class="page-content">
		<div id="mainDiv">
		<!-- Content Start -->
		<ol class="breadcrumb">
			<li><a href="javascript:void(0)">首页</a></li>
			<li class="active">HOST统计</li>
		</ol>
		<div class="padd">
			<form class="form-inline">
				<div class="form-group">
					<label for="date">开始日期：</label>
					<div class="input-group date form_datetime" data-date-format="yyyy-MM-dd" data-link-field="dtp_input1" data-initialDate="new Date()">
				        <input class="form-control" size="16" type="text" readonly id="startDate">
				        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
				</div>
				<div class="form-group" style="margin-left:40px;">
					<label for="date">结束日期：</label>
					<div class="input-group date form_datetime" data-date-format="yyyy-MM-dd" data-link-field="dtp_input1" data-initialDate="new Date()">
				        <input class="form-control" size="16" type="text" readonly id="endDate">
				        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				    </div>
				</div>
				<button class="btn btn-primary" style="width:100px;" type="button" onclick="IpMonitor.query()">查询</button>
			</form>
			<div style="height:5px;"></div>
	        <table id="ipTable" data-toggle="table" 
			       data-query-params="IpMonitor.queryParams"
			       data-unique-id="host">
				<thead>
					<tr>
						<th data-field="host">主机IP</th>
						<th data-field="reqNum" data-halign="center" data-align="center" data-sortable="true">调用量</th>
						<th data-field="qps">QPS</th>
						<th data-field="maxTime" data-formatter="IpMonitor.maxTimeFormatter">最大耗时(ms)</th>
						<th data-field="minTime">最小耗时(ms)</th>
						<th data-field="avgTime">平均耗时(ms)</th>
						<th data-field="errorNum">异常</th>
						<th data-field="errorPercent" data-halign="center" data-align="center" data-formatter="IpMonitor.toPercent">异常百分比</th>
					</tr>
				</thead>
			</table>
		</div>
		</div>
		<div id="topReqDiv" style="display: none">
			<#include "doppler/view/maxTimeDetail.ftl">
		</div>
	</div>
<script type="text/javascript" src="${base}/doppler/js/ipMonitor.js"></script>   	
</body>
<script type="text/javascript">
$('#startDate').val(new Date(new Date() - 10*60*1000*6*24).Format("yyyy-MM-dd"));
$('#endDate').val(new Date().Format("yyyy-MM-dd"));
$('.form_datetime').datetimepicker({
    language:  'zh-CN',
    format: "yyyy-mm-dd",
    weekStart: 7,
    todayBtn:  1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    forceParse: 0,
    minView: "month",//此项删除可选择时间
    pickerPosition: "bottom-left"
  });

</script>

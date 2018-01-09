<!DOCTYPE html>
<head><#include "doppler/common_doppler.ftl">
</head>
<body>
	<!-- Header Start -->
	<#include "doppler/top.ftl">
	<!-- Header End -->
	<!-- Page-sidebar Start -->
	<#include "doppler/menu.ftl">
	<!-- Page-sidebar End -->
	<div class="page-content">
		<!-- 异常页面 start -->
		<div id='mainDiv'>
			<!-- Content Start -->
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)">首页</a></li>
				<li class="active">问题汇总</li>
			</ol>
			<div class="padd">
				<form class="form-horizontal">
					<div class="row">
						<div class="col-xs-4 col-md-3"  style="margin-left: 30px;width:330px;">
							<label class="col-sm-5"
								style="line-height: 30px; margin-right: -25px;">开始日期：</label>
							<div title="最早30天之前" class="col-sm-7 input-group date form_datetime"
								data-date-format="yyyy-MM-dd" data-link-field="dtp_input1"
								data-initialDate="new Date()">
								<input class="form-control" size="16" type="text" style="width:160px;"
									id="startDate" readonly> <span class="input-group-addon"><span
									class="glyphicon glyphicon-calendar"></span></span>
							</div>
						</div>
						<div class="col-xs-4 col-md-3" style="margin-left: 40px;width:330px;">
							<label class="col-sm-5"
								style="line-height: 30px; margin-right: -25px;">结束日期：</label>
							<div title="最晚当天" class="col-sm-7 input-group date form_datetime"
								data-date-format="yyyy-MM-dd" data-link-field="dtp_end_time"
								data-initialDate="new Date()">
								<input class="form-control" size="16"  style="width:160px;" type="text" id="endDate" readonly>
								<span class="input-group-addon"><span
									class="glyphicon glyphicon-calendar"></span></span>
							</div>
						</div>
						<div class="col-xs-4 col-md-4" style="margin-left: 70px;">
							<label class="col-sm-4" style="line-height:30px;margin-right: -45px;width:100px;">&nbsp;&nbsp;&nbsp;应用名：</label>&nbsp;
							<select class="sino-select" id="appName" style="height: 33px;width: 200px;margin-left: 40px;" data-url="${base}/doppler/components/options/getAppList.shtml">
		 					</select>
						</div>
						</div><div class="row">
						<div class="col-xs-4 col-md-4" style="margin-left: 30px;">
							<label class="col-sm-4"
								style="line-height: 30px;width:100px;">&nbsp;&nbsp;&nbsp;异常名：</label>
							<input type="text" title="模糊匹配,大小写敏感" id="exName" name="exName" style="width: 200px;height: 33px;" />
						</div>
						<div class="col-xs-4 col-md-4">
							<button class="btn btn-primary"
								style="width: 100px;margin-left: 460px;" type="button"
								onclick="ProblemList.query()">查询</button>
						</div>
						<div class="col-xs-4 col-md-1" style="margin-left: 200px;">
							<div id="importDiv" ></div>
						</div>
					</div>
				</form>
				<div style="height: 5px;"></div>
				<table id="exTable" data-toggle="table" data-show-export="true">
					<thead>
						<tr>
							<th data-field="exName">异常名</th>
							<th data-field="appName">应用名</th>
							<th data-field="classNameSim" data-formatter="ProblemList.titleFormatter">类名</th>
							<th data-field="methodName">方法名</th>
							<th data-field="lineNum">行号</th>
							<th data-field="num" data-sortable="true"
								data-formatter="ProblemList.nameFormatter">数量</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<!-- 异常页面 end -->
		<!-- 异常明细页面 start -->
		<div id="detailProblemDiv" style="display: none">
			<!-- Content Start -->
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)">首页</a></li>
				<li><a href="#" onclick="ProblemList.backHandler();">问题汇总</a></li>
				<li class="active">问题明细</li>
			</ol>
			<div class="padd">
				<form class="form-inline">
					<div class="row">
						<div class="col-xs-6 col-md-2">
							<label for="date">日期从：</label>
							<label for="date" id="detail_startDate"></label>
						</div>
						<div class="col-xs-6 col-md-2" style="margin-left:-30px;">
							<label for="date">到&nbsp;&nbsp;&nbsp;</label>
							<label for="date" id="detail_endDate"></label>
						</div>
						<div class="col-xs-6 col-md-2" style="margin-left:-30px;">
							<label for="date">应用名：</label>
							<label for="date" id="detail_appName"></label>
						</div>
						<div class="col-xs-6 col-md-4" style="margin-left:-30px;">
							<label for="date">异常名：</label>
							<label for="date" id="detail_exName"></label>
						</div>
						
					</div>	
				</form>
				<div style="height:5px;"></div>
				<table id="detailProblemTable" data-toggle="table">
					<thead>
						<tr>
							<th data-field="timestamp" data-sortable="true"
								data-formatter="ProblemList.formatterTime">日期</th>
							<th data-field="exName">异常名</th>
							<th data-field="appName">应用名</th>
							<th data-field="host">主机</th>
							<th data-field="classNameSim" data-formatter="ProblemList.titleFormatter">类名</th>
							<th data-field="methodName">方法名</th>
							<th data-field="lineNum">行号</th>
							<th data-field="id" data-sortable="true"
								data-formatter="ProblemList.detailFormatter"><span
								style="color: white;" id="detailSpan">明细</span></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<!-- 异常明细页面 end -->
		<!-- 弹出Modal start -->
		<div class="modal fade" id="myModal">
			<div class="modal-dialog" style="width: 98%">
				<div class="modal-content" align="center">
					<div class="modal-header" style="padding: 2px">
						<button type="button" class="close" data-dismiss="modal">
							&times;</button>
						<h4 class="modal-title" id="myModalLabel">问题日志明细</h4>
					</div>
					<div align="center">
						<a class="link-info"
							href="javascript:ProblemList.previousProblemHandler()">↑↑
							显示前15条记录 ↑↑</a>
					</div>
					<div id="myDiv" class="modal-body"
						style="height: 525px; overflow: auto;">
						<table id="detailTable" style="width: 2000px;">
						</table>
					</div>
					<div align="center" style="padding-bottom: 5px">
						<a class="link-info"
							href="javascript:ProblemList.nextProblemHandler()">↓↓ 显示后15条记录 ↓↓</a>
					</div>
				</div>
			</div>
		</div>
		<!-- 弹出Modal end -->
	</div>
	<script type="text/javascript" src="${base}/doppler/js/problemList.js"></script>
</body>
<script type="text/javascript">

	$('#startDate').val(new Date(new Date()).Format("yyyy-MM-dd"));
	$('#endDate').val(new Date().Format("yyyy-MM-dd"));
	$('.form_datetime').datetimepicker({
		language : 'zh-CN',
		format : "yyyy-mm-dd",
		weekStart : 7,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		forceParse : 0,
		minView : "month",//此项删除可选择时间
		pickerPosition : "bottom-left"
	});
</script>
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

#floatDiv{	
	position:absolute;
	z-index:1;
	display:none;
	font-size:14px;
	width:130px;
	height:20px;
	line-height:20px;
	border-radius:3px;
	background-color:#8E8E8E;
	font-weight:bold;
	text-align:center;
	float:left;
}

#floatDiv a{
	filter:alpha(opacity=100);
	-moz-opacity:1; 
	opacity:1;
	color : white;
	text-decoration:underline;
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
<div class="page-content" onscroll="ExceptionMonitor.hideFloatDiv()">
	<!-- 异常列表页面	start-->
	<div id="mainDiv">
		<!-- Content Start -->
		<ol class="breadcrumb">
			<li><a href="javascript:void(0)">首页</a></li>
			<li class="active">日志查询</li>
		</ol>
		<div class="padd">
			<form class="form-horizontal">
				<div class="row">
					<div class="col-xs-6 col-md-3">
						<label class="col-sm-3" style="line-height:30px;padding-left: 10px;width:70px;">日期：</label>
						<div class="col-sm-7 input-group date form_datetime" data-date-format="yyyy-MM-dd hh:mm" data-link-field="dtp_start_time" data-initialDate="new Date()">
							<input class="form-control" size="16" type="text" style="width:150px;" readonly id="startDate"><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						</div>
					</div>
					<div class="col-xs-6 col-md-3" style="margin-left: -35px;">
					<label class="col-sm-3" style="line-height:30px;">到</label>
						<div class="col-sm-7 input-group date form_datetime"  style="width:150px;" data-date-format="yyyy-MM-dd hh:mm" data-link-field="dtp_end_time" data-initialDate="new Date()">
							<input class="form-control" size="16" type="text" style="width:150px;margin-left: -20px;" readonly id="endDate"> <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						</div>
					</div>
					<div class="col-xs-6 col-md-3" style="margin-left: -10px;">
						<label class="col-sm-3" style="line-height:30px;width:100px;">应用名：</label>
						<select class="sino-select" id="appName" style="height: 30px;width: 160px;" data-url="${base}/doppler/components/options/getAppList.shtml">
	 					</select>
					</div>
					<div class="col-xs-6 col-md-3" style="margin-left: -10px;width:300px;">
						<label class="col-sm-4" style="line-height:30px;width:100px;">错误级别：</label> 
						<select class="sino-select" id="errorLevel" style="height: 30px;width: 170px" data-url-params="errorLevel">
							<option value="-1">请选择</option>
							<option value="DEBUG">DEBUG</option>
							<option value="INFO">INFO</option>
							<option value="WARN">WARN</option>
							<option value="ERROR">ERROR</option>
	 					</select>
					</div>
				</div>
				<div class="row" style="margin-top: 5px">
					
					<div class="col-xs-6 col-md-6">
						<label class="col-sm-2" style="line-height:30px;width:70px;padding-left: 10px;">地址：</label>
						<input id="addr" name="addr" style="width: 190px;" type="text" >
					</div>
					<div class="col-xs-6 col-md-3" style="margin-left: -45px;">
						<label class="col-sm-3" style="line-height:30px;width:100px;">关键字：</label>
						<input id="keys" name="keys" style="width: 160px;"  type="text" >
					</div>
					<div class="col-xs-6 col-md-3">
						<button class="btn btn-primary" style="width: 100px;margin-left:90px;" type="button"
							onclick="ExceptionMonitor.queryHandler()">查询</button>
					</div>
				</div>
			</form>
			<div style="height:5px;"></div>
			<div style="height:580px;overflow:auto;">
			<div style="width:2200px;overflow:auto;">
				<table id="exceptionTable" data-toggle="table" class="table table-striped table-condensed"
					data-unique-id="id" >
					<thead>
						<tr>
							<th data-formatter="ExceptionMonitor.detailNameFormatter">日志内容 </th>
						</tr>
					</thead>
				</table>
			</div>
			</div>
		</div>
	</div>
	<!-- 异常列表页面	 end -->
	<!-- 弹出Modal start -->
	<div class="modal fade" id="myModal">
	   <div class="modal-dialog" style="width: 98%">
	      <div class="modal-content" align="center">
	         <div class="modal-header" style="padding: 2px">
	            <button type="button" class="close" 
	               data-dismiss="modal">
	                  &times;
	            </button>
	            <h4 class="modal-title" id="myModalLabel">
	               	异常日志明细
	            </h4>
	         </div>
	         <div align="center">
	          	<a class="link-info" href="javascript:ExceptionMonitor.previousErrorHandler()">↑↑ 显示前15条记录 ↑↑</a>
	         </div>
	         <div id="myDiv" class="modal-body" style="height:525px;overflow:auto;">
	           	<table id="detailTable" style="width: 2000px;">
				</table>
	         </div>
	         <div align="center" style="padding-bottom: 5px">
	         	<a class="link-info" href="javascript:ExceptionMonitor.nextErrorHandler()">↓↓ 显示后15条记录 ↓↓</a>
	         </div>
	      </div>
		</div>
	</div>
	<!-- 弹出Modal end -->
	<!-- 提示框 start -->
	<div class="modal fade" id="myErrorModal" style="padding-top: 10%;margin-left: 30%">
	   <div class="modal-dialog">
	      <div class="modal-content" align="center" style="width: 300px">
	         <div class="modal-header">
	            <button type="button" class="close" 
	               data-dismiss="modal">
	                  &times;
	            </button>
	            <h4 class="modal-title">
	               	<font color="red">请选择一条异常日志明细！</font>
	            </h4>
	         </div>
	      </div>
		</div>
	</div>
	<!-- 提示框 end -->
</div>
<div id="floatDiv">
	<a href="#">显示前后15条记录</a>
</div>
<script type="text/javascript" src="${base}/doppler/js/exceptionMonitor.js"></script>
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

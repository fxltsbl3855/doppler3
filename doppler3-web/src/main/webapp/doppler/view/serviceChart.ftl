<!-- Content Start -->
<ol class="breadcrumb">
	<li><a href="javascript:void(0)">首页</a></li>
	<li><a href="javascript:void(0)">应用监控</a></li>
	<li><a href="javascript:void(0)">模块统计</a></li>
	<li class="active">接口方法走势图</li>
</ol>
<div class="padd">
	<form class="form-inline">
		<div id="legend" class="">
	        <legend class="" id="legendName"></legend>
	    </div>
	    <input type="hidden" id="serviceChartStartDate" name="serviceChartStartDate">
	    <input type="hidden" id="serviceChartEndDate" name="serviceChartEndDate">
	    <input type="hidden" id="appName" name="appName">
	    <input type="hidden" id="moduleChartName" name="moduleChartName">
	    <input type="hidden" id="methodName" name="methodName">
	</form>
	 <div class="panel-body">
          <div id="chart1" style="height:300px;"></div>
        </div>
</div>
<script type="text/javascript" src="${base}/doppler/js/serviceChart.js"></script>   
<ol class="breadcrumb">
  <li><a href="javascript:void(0)">首页</a></li>
  <li><a href="javascript:void(0)" onclick="App.showSecondLevelTitle()">应用统计</a></li>
  <li class="active">模块统计</li>
</ol>
<div class="padd">
	<form class="form-inline">
		<div id="legend" class="">
	       
	    </div>
	    <div class="row">
			<input type="hidden" id="appName" name="appName">
			<div class="col-xs-6 col-md-2">
				<label for="port">应用：</label>
				<div class="input-group" style="height: 30px;line-height:28px;font-weight:bold;" id="appNameCondition">
				</div>
			</div>
			<div class="col-xs-6 col-md-5">
				<label for="port">模块：</label>
				<div class="input-group">
					<select class="sino-select" id="moduleName" style="height: 30px;width: 350px" data-url="" data-url-params="appName">
					</select>
				</div>
			</div>
		</div>
		<div class="row" style="margin-top:10px;">
			<div class="col-xs-6 col-md-4">
				<label for="date">开始日期：</label>
				 <div class="input-group date form_datetime" data-date-format="yyyy-MM-dd" data-link-field="dtp_input1">
		            <input class="form-control" size="16" type="text" readonly id="serviceStartDate">
		            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
		        </div>
			</div>
			<div class="col-xs-6 col-md-4">
				<label for="date">结束日期：</label>
				 <div class="input-group date form_datetime" data-date-format="yyyy-MM-dd" data-link-field="dtp_input1">
		            <input class="form-control" size="16" type="text" readonly id="serviceEndDate">
		            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
		        </div>
			</div>
			<button class="btn btn-primary" style="width:100px;" type="button" onclick="Services.query()">查询</button>
		</div>
	</form>
	<div style="height:5px;"></div>
	<table id="servicesTable" data-toggle="sino-grid" 
                     data-click-to-select="true"
	       		  data-unique-id="spanViewId">
        <thead>
        <tr>
            <th data-field="methodName" data-formatter="Services.nameFormatter">模块</th>
            <th data-field="reqNum">调用量</th>
            <th data-field="qps">QPS</th>
            <th data-field="maxTime" data-formatter="Services.maxTimeFormatter">最大耗时(ms)</th>
			<th data-field="minTime">最小耗时(ms)</th>
			<th data-field="avgTime">平均耗时(ms)</th>
            <th data-field="errorNum">异常</th>
            <th data-field="errorPercent" data-formatter="Services.toPercent">异常百分比</th>
        </tr>
        </thead>
    </table>
</div>
<script type="text/javascript" src="${base}/doppler/js/services.js"></script>   

<script type="text/javascript">
$('#serviceStartDate').val(new Date(new Date() - 10*60*1000*6*24).Format("yyyy-MM-dd"));
$('#serviceEndDate').val(new Date().Format("yyyy-MM-dd"));
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
    pickerPosition: "bottom-left",
  });
</script>

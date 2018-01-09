<ol class="breadcrumb">
  <li><a href="javascript:void(0)">首页</a></li>
  <li><a href="javascript:void(0)" id="secondLevelTitle"></a></li>
  <li class="active">最大耗时Top</li>
</ol>
<div class="padd">
	<form class="form-inline">
		<div id="legend" class="">
	       
	    </div>
		<div class="row">
			<div class="col-xs-6 col-md-4">
				<label for="date">开始日期：</label>
				<label for="date" id="maxTimeStartDate"></label>
			</div>
			<div class="col-xs-6 col-md-4" style="margin-left:-180px;">
				<label for="date">结束日期：</label>
				<label for="date" id="maxTimeEndDate"></label>
			</div>
		</div>
		<div class="row" id="maxTimeAppDiv" style="margin-top:10px;">
			<div class="col-xs-6 col-md-4">
				<label for="date">应用：</label>
				<label for="date" id="maxTimeAppName"></label>
			</div>
		</div>
		<div class="row" id="maxTimeHostDiv" style="margin-top:10px;">
			<div class="col-xs-6 col-md-4">
				<label for="date">HOST：</label>
				<label for="date" id="maxTimeHost"></label>
			</div>
		</div>
		<div class="row" id="maxTimeModuleDiv" style="margin-top:10px;">
			<div class="col-xs-6 col-md-4">
				<label for="date">模块名：</label>
				<label for="date" id="maxTimeModuleName"></label>
			</div>
			<div class="col-xs-6 col-md-4">
				<label for="date">方法名：</label>
				<label for="date" id="maxTimeMethodName"></label>
			</div>
		</div>
	</form>
	<table id="topRequestTable" data-toggle="sino-grid" 
                     data-click-to-select="true"
	       		  data-unique-id="spanViewId">
        <thead>
        <tr>
            <th data-field="reqTime">请求日期</th>
            <th data-field="host">主机IP</th>
            <th data-field="appName">应用</th>
            <th data-field="moduleName">模块名</th>
			<th data-field="methodName">方法名</th>
			<th data-field="param" data-formatter="MaxTimeDetail.paramFormatter">参数</th>
            <th data-field="time">耗时</th>
        </tr>
        </thead>
    </table>
</div>
<script type="text/javascript" src="${base}/doppler/js/maxTimeDetail.js"></script>   
<script type="text/javascript">
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

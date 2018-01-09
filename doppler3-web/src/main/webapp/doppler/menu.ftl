<#assign base=request.contextPath />
<div class="page-sidebar" style="width:160px;">
	<div class="nav-collapse sidebar-nav">
		<ul class="nav nav-tabs nav-stacked main-menu">
			<li>
				<a href="#" onclick="Menu.menuClickHandler('${base}/doppler/view/index.shtml')" id="indexPage"> 
					<span class="glyphicon glyphicon-home"></span> 
					<span class="hidden-tablet">DashBoard</span>
				</a>
			</li>
			
			<li>
				<a href="#" onclick="Menu.showChildMenu('exceptionLi','exception');" id="exception"> 
					<span class="glyphicon glyphicon-star"></span> 
					<span class="hidden-tablet">异常管理</span>
				</a>
			</li>
			<li id='exceptionLi' style='background-color:#666666;display:none;'>
				<a href="#" onclick="Menu.menuClickHandler('${base}/doppler/view/errorList.shtml')" id="errorListPage"> 
					<span class="glyphicon glyphicon-star-empty" style="margin-left:20px;"></span> 
					<span class="hidden-tablet">异常列表</span>
				</a>
				<a href="#" onclick="Menu.menuClickHandler('${base}/doppler/view/problemList.shtml')" id="problemListPage">
					<span class="glyphicon glyphicon-star" style="margin-left:20px;"></span> 
					<span class="hidden-tablet">问题汇总</span>
				</a>
			</li>
			
			<li>
				<a href="#" onclick="Menu.menuClickHandler('${base}/doppler/view/timerTask.shtml')" id="timerTaskPage"> 
					<span class="glyphicon glyphicon-star"></span> 
					<span class="hidden-tablet">定时任务</span>
				</a>
			</li>
			<li>
				<a href="#" onclick="Menu.menuClickHandler('${base}/doppler/view/exceptionMonitor.shtml.shtml', 'exceptionMonitorPage')" id="exceptionMonitorPage"> 
					<span class="glyphicon glyphicon-star"></span> 
					<span class="hidden-tablet">日志查询</span>
				</a>
			</li>
			<li>
				<a href="#" onclick="Menu.showChildMenu('requestCountLi','requestCount');" id="requestCount"> 
					<span class="glyphicon glyphicon-star"></span> 
					<span class="hidden-tablet">访问统计</span>
				</a>
			</li>
			<li id='requestCountLi' style='background-color:#666666;display:none;'>
				<a href="#" onclick="Menu.menuClickHandler('${base}/doppler/view/app.shtml')" id="appPage">
					<span class="glyphicon glyphicon-star-empty" style="margin-left:20px;"></span> 
					<span class="hidden-tablet">应用统计</span>
				</a>
				<a href="#" onclick="Menu.menuClickHandler('${base}/doppler/view/ipMonitor.shtml')" id="ipMonitorPage">
					<span class="glyphicon glyphicon-star" style="margin-left:20px;"></span> 
					<span class="hidden-tablet">HOST统计</span>
				</a>
			</li>
			
			<li>
				<a href="#" onclick="Menu.showChildMenu('recordLi','recordCount');" id="recordCount"> 
					<span class="glyphicon glyphicon-star"></span> 
					<span class="hidden-tablet">操作查询</span>
				</a>
			</li>
			<li id='recordLi' style='background-color:#666666;display:none;'>
				<a href="#" onclick="Menu.menuClickHandler('${base}/doppler/view/webOperation.shtml')" id="webOperationPage">
					<span class="glyphicon glyphicon-star-empty" style="margin-left:20px;"></span> 
					<span class="hidden-tablet">Web操作查询</span>
				</a>
				<a href="#" onclick="Menu.menuClickHandler('${base}/doppler/view/logShow.shtml')" id="logShowPage">
					<span class="glyphicon glyphicon-star" style="margin-left:20px;"></span> 
					<span class="hidden-tablet">业务对象查询</span>
				</a>
			</li>
		</ul>
	</div>
</div>
<script type="text/javascript" src="${base}/doppler/js/menu.js"></script>
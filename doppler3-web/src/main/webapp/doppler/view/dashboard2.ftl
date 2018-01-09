      <#assign base=request.contextPath />
		<ol class="breadcrumb">
			<li><a href="#">首页</a></li>
			<li class="active">DashBoard</li>
		</ol>
		<div class="padd-t">
			<div class="container-fluid">
				<div class="row">
					<div class="col-md-3">
						<div class="statbox bd-box">
							<span class="icon4"></span>
							<div class="title">最新24小时异常总数</div>
							<div class="number" id="errorNum"></div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="statbox bd-box">
							<span class="icon1"></span>
							<div class="title">系统总数</div>
							<div class="number" id="appNum"></div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="statbox bd-box">
							<span class="icon2"></span>
							<div class="title">系统请求量</div>
							<div class="number" id="appReqNum"></div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="statbox bd-box">
							<span class="icon3"></span>
							<div class="title">模块总数</div>
							<div class="number" id="moduleNum"></div>
						</div>
					</div>
				</div>
				<div class="blank15"></div>
				<div class="row">
				<div class="col-md-6">
					<div class="panel panel-default">
						<div class="panel-heading">
							最新24小时异常分类饼图
							<div class="panel-icon pull-right">
								<a href="#" class="btn-minimize"><i
									class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
									class="btn-close"><i class="glyphicon glyphicon-remove"></i></a>
							</div>
						</div>
						<div class="panel-body">
							<div id="errorStatChart" style="height:350px;"></div>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="panel panel-default">
						<div class="panel-heading">
							异常总量 曲线图
							<div class="panel-icon pull-right">
								<a href="#" class="btn-minimize"><i
									class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
									class="btn-close"><i class="glyphicon glyphicon-remove"></i></a>
							</div>
						</div>
						<div class="panel-body">
							<div id="errorChart" style="height:350px;"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="panel panel-default">
						<div class="panel-heading">
							系统请求量 饼图
							<div class="panel-icon pull-right">
								<a href="#" class="btn-minimize"><i
									class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
									class="btn-close"><i class="glyphicon glyphicon-remove"></i></a>
							</div>
						</div>
						<div class="panel-body">
							<div id="appChart" style="height:350px;"></div>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="panel panel-default">
						<div class="panel-heading">
							模块请求量 饼图
							<div class="panel-icon pull-right">
								<a href="#" class="btn-minimize"><i
									class="glyphicon glyphicon-chevron-up"></i></a> <a href="#"
									class="btn-close"><i class="glyphicon glyphicon-remove"></i></a>
							</div>
						</div>
						<div class="panel-body">
							<div id="moduleChart" style="height:350px;"></div>
						</div>
					</div>
				</div>
			</div>
				
			</div>
		</div>
<script type="text/javascript" src="${base}/doppler/js/index.js"></script>

